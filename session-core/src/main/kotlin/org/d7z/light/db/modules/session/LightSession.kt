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

class LightSession private constructor(
    val lightDB: LightDB,
    private val namespace: String,
    val dataCovert: IDataCovert,
    val globalTtl: Long,
    val sessionGenerate: ISessionGenerate,
) : ILightSession {
    private val map = ConcurrentHashMap<String, ISessionGroupContext>()
    override fun getSessionGroupContext(group: String): ISessionGroupContext {
        return map.getOrPut(group) {
            SessionGroupContext(this, "$namespace:$group", group)
        }
    }

    override fun findSessionContext(sessionId: String): Optional<ISessionContext> {
        return findSessionGroupContext(sessionId).flatMap { it.querySession(sessionId) }
    }

    override fun findSessionGroupContext(sessionId: String): Optional<ISessionGroupContext> {
        val name = sessionGenerate.getSessionTypeName(sessionId)
        return if (map.containsKey(name)) {
            Optional.of(getSessionGroupContext(name))
        } else {
            Optional.empty()
        }
    }

    override fun getSessionType(sessionId: String): String {
        return sessionGenerate.getSessionTypeName(sessionId)
    }

    class Builder(override val container: LightDB = LightDB) :
        ILightSession.Builder {
        override var namespace: String = "session"
        override var dataCovert: IDataCovert = IDataCovert
        override var ttl: Long = 60 * 60
        override var sessionIDGenerate: ISessionGenerate = SimpleSessionGenerate()

        override fun build(): ILightSession {
            return LightSession(container, namespace, dataCovert, ttl, sessionIDGenerate)
        }
    }
}
