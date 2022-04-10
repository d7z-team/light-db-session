package org.d7z.light.db.modules.session.utils

import org.d7z.light.db.modules.session.api.ISessionGenerate
import org.d7z.light.db.modules.session.api.SessionException
import java.util.UUID
import kotlin.experimental.xor

class SimpleSessionGenerate : ISessionGenerate {
    override fun generate(sessionType: String): String {
        val session = UUID.randomUUID()
        val seed = (session.leastSignificantBits % Byte.MAX_VALUE).toByte()
        val head = sessionType.toByteArray(Charsets.ISO_8859_1).joinToString(separator = "") {
            String.format("%02x", it xor seed)
        }
        return "$head-$session"
    }

    override fun getSessionTypeName(sessionId: String): String {
        val split = sessionId.split(Regex("-"), 2)
        if (split.size != 2) {
            throw SessionException("无法解析 Session ID :$sessionId")
        }
        val uuid = UUID.fromString(split[1])
        val seed = (uuid.leastSignificantBits % Byte.MAX_VALUE).toByte()
        val head = split.first()
        if (head.length % 2 != 0) {
            throw SessionException("无法解析 Session ID :$sessionId")
        }
        return head.windowed(2, 2)
            .map { (it.toInt(16).toByte() xor seed) }
            .toByteArray().toString(Charsets.ISO_8859_1)
    }
}
