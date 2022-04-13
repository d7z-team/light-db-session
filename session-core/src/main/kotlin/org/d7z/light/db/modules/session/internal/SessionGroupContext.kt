package org.d7z.light.db.modules.session.internal

import org.d7z.light.db.modules.session.LightSession
import org.d7z.light.db.modules.session.api.ISessionContext
import org.d7z.light.db.modules.session.api.ISessionGroupContext
import org.d7z.light.db.modules.session.api.SessionException
import java.util.Optional

class SessionGroupContext(
    private val lightSession: LightSession,
    private val groupName: String,
    override val name: String,
) : ISessionGroupContext {
    override var survivalTime: Long = lightSession.globalTtl
    val mapContext = lightSession.lightDB.withMap(groupName)

    override fun newSession(): ISessionContext {
        var key: String
        do {
            key = lightSession.sessionGenerate.generate(name)
        } while (mapContext.exists(key))
        return getOrCreateSession(key)
    }

    override fun newSession(sessionId: String): ISessionContext {
        if (lightSession.sessionGenerate.getSessionTypeName(sessionId) != name) {
            throw SessionException("Session ID $sessionId 不合法.")
        }
        return getOrCreateSession(sessionId)
    }

    private fun getOrCreateSession(session: String): ISessionContext {
        val sessionContext = SessionContext(
            session,
            this,
            lightSession
        )
        sessionContext.init()
        return sessionContext
    }

    override fun querySession(session: String): Optional<ISessionContext> {
        return lightSession.lightDB.withMap(groupName).get(session, String::class, String::class)
            .map { getOrCreateSession(session) }
    }

    override fun destroy(session: String): Boolean {
        return mapContext.drop(session)
    }
}
