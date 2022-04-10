package org.d7z.light.db.modules.session.api

/**
 * Session id 生成器
 */
interface ISessionGenerate {
    /**
     * 根据会话分组名称生成特定 SESSION ID
     */
    fun generate(sessionType: String): String

    /**
     * 根据Session ID 获取 Session 名称
     *
     * @param sessionId String Session ID
     * @return Optional<String> Session 会话分组名称
     */
    fun getSessionTypeName(sessionId: String): String
}
