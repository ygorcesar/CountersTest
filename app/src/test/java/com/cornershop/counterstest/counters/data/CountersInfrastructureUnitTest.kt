package com.cornershop.counterstest.counters.data

import com.cornershop.counterstest.base.BaseTests
import com.cornershop.counterstest.counter.data.*
import com.cornershop.counterstest.utils.*
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import retrofit2.create

class CountersInfrastructureUnitTest : BaseTests() {

    override val isMockServerEnabled: Boolean get() = true

    @MockK
    lateinit var mockNetworkHandler: NetworkHandler

    private val api: CountersGateway = retrofit.create()
    private val mapper: CounterMapper = CounterMapper()
    private lateinit var infrastructure: CounterInfrastructure

    @Before
    override fun setUp() {
        super.setUp()
        infrastructure = CounterInfrastructure(api, mapper, mockNetworkHandler)
    }

    @Test
    fun `should get counters value with success`() {
        // given
        stubNetworkHandlerIsConnected(isConnected = true)
        val expected = mapper.mapToModel(JsonFile.Counters.COUNTERS.fromJson())

        // when
        val testObserver = infrastructure.getCounters().test()

        // then
        testObserver.assertCompleted(expected)
    }

    @Test
    fun `should throw network error when try to get counters without network connection`() {
        // given
        stubNetworkHandlerIsConnected(isConnected = false)
        val expected = NetworkError

        // when
        val testObserver = infrastructure.getCounters().test()

        // then
        testObserver.assertWithError(expected)
    }

    @Test
    fun `should add a new counter with success`() {
        // given
        stubNetworkHandlerIsConnected(isConnected = true)
        val expected = mapper.mapToModel(JsonFile.Counters.ADD_COUNTER.fromJson())
        val request = CounterRequestAdd("title")

        // when
        val testObserver = infrastructure.addCounter(request).test()

        // then
        testObserver.assertCompleted(expected)
    }

    @Test
    fun `should throw network error when try to add new counter without network connection`() {
        // given
        stubNetworkHandlerIsConnected(isConnected = false)
        val expected = NetworkError
        val request = CounterRequestAdd("title")

        // when
        val testObserver = infrastructure.addCounter(request).test()

        // then
        testObserver.assertWithError(expected)
    }

     @Test
     fun `should delete counter with success`() {
         // given
         stubNetworkHandlerIsConnected(isConnected = true)
         val expected = mapper.mapToModel(JsonFile.Counters.DELETE_COUNTER.fromJson())
         val request = CounterRequest("id_awd")

         // when
         val testObserver = infrastructure.deleteCounter(request).test()

         // then
         testObserver.assertCompleted(expected)
     }

    @Test
    fun `should throw network error when try to delete counter without network connection`() {
        // given
        stubNetworkHandlerIsConnected(isConnected = false)
        val expected = NetworkError
        val request = CounterRequest("id_awd")

        // when
        val testObserver = infrastructure.deleteCounter(request).test()

        // then
        testObserver.assertWithError(expected)
    }

    @Test
    fun `should increment counter with success`() {
        // given
        stubNetworkHandlerIsConnected(isConnected = true)
        val expected = mapper.mapToModel(JsonFile.Counters.INCREMENT_COUNTER.fromJson())
        val request = CounterRequest("id_awd")

        // when
        val testObserver = infrastructure.incrementCounter(request).test()

        // then
        testObserver.assertCompleted(expected)
    }

    @Test
    fun `should throw network error when try to increment counter without network connection`() {
        // given
        stubNetworkHandlerIsConnected(isConnected = false)
        val expected = NetworkError
        val request = CounterRequest("id_awd")

        // when
        val testObserver = infrastructure.incrementCounter(request).test()

        // then
        testObserver.assertWithError(expected)
    }

    @Test
    fun `should decrement counter with success`() {
        // given
        stubNetworkHandlerIsConnected(isConnected = true)
        val expected = mapper.mapToModel(JsonFile.Counters.DECREMENT_COUNTER.fromJson())
        val request = CounterRequest("id_awd")

        // when
        val testObserver = infrastructure.decrementCounter(request).test()

        // then
        testObserver.assertCompleted(expected)
    }

    @Test
    fun `should throw network error when try to decrement counter without network connection`() {
        // given
        stubNetworkHandlerIsConnected(isConnected = false)
        val expected = NetworkError
        val request = CounterRequest("id_awd")

        // when
        val testObserver = infrastructure.decrementCounter(request).test()

        // then
        testObserver.assertWithError(expected)
    }

    private fun stubNetworkHandlerIsConnected(isConnected: Boolean) {
        every { mockNetworkHandler.isConnected } returns isConnected
    }
}