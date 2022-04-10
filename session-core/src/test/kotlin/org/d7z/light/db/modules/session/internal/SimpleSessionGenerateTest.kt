package org.d7z.light.db.modules.session.internal

import org.d7z.light.db.modules.session.utils.SimpleSessionGenerate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class SimpleSessionGenerateTest {
    @Test
    fun test() {
        val simpleSessionGenerate = SimpleSessionGenerate()
        for (z in 0..10) {
            for (i in 0..100) {
                val nextInt = Random.nextInt()
                val message = simpleSessionGenerate.generate("users-$z-$nextInt")
                assertEquals(simpleSessionGenerate.getSessionTypeName(message), "users-$z-$nextInt")
            }
        }
    }
}
