/*
 * Copyright 2016-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines

import kotlinx.coroutines.channels.*
import org.junit.*
import org.junit.Test
import kotlin.test.*

/**
 * Tests event loops integration.
 * See [https://github.com/Kotlin/kotlinx.coroutines/issues/860].
 */
class EventLoopsTest {
    @Test
    fun testNestedRunBlocking() {
        runBlocking { // outer event loop
            // Produce string "OK"
            val ch = produce { send("OK") }
            // try receive this string in a blocking way:
            assertEquals("OK", runBlocking { ch.receive() }) // it hangs here !!!
        }
    }
}