package com.cornershop.counterstest.base

import com.cornershop.counterstest.utils.MockServerDispatcher
import com.cornershop.counterstest.utils.RxSchedulerOverrideRule
import io.mockk.MockKAnnotations
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseTests {

    @Rule
    @JvmField
    val schedulersRule = RxSchedulerOverrideRule()

    private lateinit var mockServer: MockWebServer

    open val isMockServerEnabled: Boolean = false

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
        configureMockServer()
    }

    @After
    open fun tearDown() {
        stopMockServer()
    }

    @Throws
    open fun configureMockServer() {
        if (isMockServerEnabled) {
            mockServer = MockWebServer().apply {
                start(8080)
                dispatcher = MockServerDispatcher()
            }
        }
    }

    @Throws
    open fun stopMockServer() {
        if (isMockServerEnabled) mockServer.shutdown()
    }

}
