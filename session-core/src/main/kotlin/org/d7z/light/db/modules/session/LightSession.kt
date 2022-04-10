package org.d7z.light.db.modules.session

import org.d7z.light.db.api.LightDB
import org.d7z.light.db.modules.session.api.ILightSession
import org.d7z.light.db.modules.session.api.ISessionContext
import org.d7z.light.db.modules.session.api.ISessionGenerate
import org.d7z.light.db.modules.session.api.ISessionGroupContext
import org.d7z.light.db.modules.session.internal.SessionGroupContext
import org.d7z.light.db.modules.session.utils.SimpleSessionGenerate
import org.d7z.objects.format.api.IDataCovert
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * @property lightDB LightDB LightDB 实例
 * @property namespace String Cache 在 LightDB 下的命名空间
 * @property dataCovert IDataCovert 类型转换器
 * @property globalTtl Long 全局 Session 过期时间
 * @property sessionGenerate ISessionGenerate  Session ID 生成器
 */
class LightSession @JvmOverloads constructor(
    val lightDB: LightDB = LightDB,
    private val namespace: String = "session",
    val dataCovert: IDataCovert = IDataCovert,
    val globalTtl: Long = 60 * 60,
    val sessionGenerate: ISessionGenerate = SimpleSessionGenerate(),
) : ILightSession {
    private val map = ConcurrentHashMap<String, ISessionGroupContext>()
    override fun getSessionGroupContext(group: String): ISessionGroupContext {
        return map.getOrPut(group) {
            SessionGroupContext(this, "$namespace:$group", group)
        }
    }

    override fun findSessionContext(sessionId: String): Optional<ISessionContext> {
        return findSessionGroupContext(sessionId).querySession(sessionId)
    }

    override fun findSessionGroupContext(sessionId: String): ISessionGroupContext {
        val name = sessionGenerate.getSessionTypeName(sessionId)
        return getSessionGroupContext(name)
    }

    override fun getSessionType(sessionId: String): String {
        return sessionGenerate.getSessionTypeName(sessionId)
    }
}
