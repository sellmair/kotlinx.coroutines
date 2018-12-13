/*
 * Copyright 2016-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("DeferredResultUnused")

package kotlinx.coroutines.scheduling

import kotlinx.coroutines.*
import org.junit.*
import java.util.concurrent.*
import java.util.concurrent.atomic.*

class BlockingCoroutineDispatcherStressTest : SchedulerTestBase() {

    init {
        corePoolSize = 2
        idleWorkerKeepAliveNs = 100000
    }

    @Test
    fun testBlockingTasksStarvation() = runBlocking {
        corePoolSize = 2 // Easier to reproduce race with unparks
        val iterations = 50_000 * stressTestMultiplier
        val blockingLimit = 4 // CORES_COUNT * 3
        val blocking = blockingDispatcher(blockingLimit)

        repeat(iterations) {
            val barrier = CyclicBarrier(blockingLimit + 1)
            // Should eat all limit * 3 cpu without any starvation
            val tasks = (1..blockingLimit).map { async(blocking) { barrier.await() } }

            tasks.forEach { require(it.isActive) }
            barrier.await()
            tasks.joinAll()
        }
    }
}
