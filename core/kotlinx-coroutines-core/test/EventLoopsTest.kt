/*
 * Copyright 2016-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines

import kotlinx.coroutines.channels.*
import org.junit.*

class EventLoopsTest {
    @Test
    fun testNestedRunBlocking() {
        runBlocking { // outer event loop
            // Produce string "OK"
            val ch = produce { send("OK") }
            // try receive this string in a blocking way:
            println(runBlocking { ch.receive() }) // it hangs here !!!
        }
    }
}