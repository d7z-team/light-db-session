package org.d7z.light.db.modules.session.api

import java.util.Optional

/**
 * Session 会话管理
 */
interface ILightSession {
    /**
     * 获取名称为`name` 的 SessionGroupContext
     *
     * 如果不存在名为 `name` 的 SessionGroupContext 则创建新的的 SessionGroupContext
     *
     * @param group String 会话分组
     * @return SessionGroupContext<T> 会话组上下文
     */
    fun getSessionGroupContext(group: String = "default"): ISessionGroupContext

    /**
     * 根据 Session ID 获取 Session Context
     **
     * @param sessionId String 获取 session id
     */
    fun findSessionContext(sessionId: String): Optional<ISessionContext>

    /**
     * 使用 Session ID 获取 Session 所属的分组
     *
     * 如果未发现 Session Group 则自动创建
     *
     * @param sessionId String 根据配置的规则创建的 Session ID
     * @return ISessionGroupContext Session 的分组
     */
    fun findSessionGroupContext(sessionId: String): ISessionGroupContext

    /**
     * 获取此 Session ID 的分组类型
     * @param sessionId String 会话 ID
     * @return String 会话类型
     */
    fun getSessionType(sessionId: String): String
}
