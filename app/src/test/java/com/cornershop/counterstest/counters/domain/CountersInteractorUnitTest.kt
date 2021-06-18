package com.cornershop.counterstest.counters.domain

import com.cornershop.counterstest.base.BaseTests
import com.cornershop.counterstest.counter.data.CounterInfrastructure
import com.cornershop.counterstest.counter.data.CounterRequest
import com.cornershop.counterstest.counter.data.CounterRequestAdd
import com.cornershop.counterstest.counter.domain.CounterBusiness
import com.cornershop.counterstest.counter.domain.CountersInteractor
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.utils.NetworkError
import com.cornershop.counterstest.utils.assertCompleted
import com.cornershop.counterstest.utils.assertWithError
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import io.reactivex.functions.Predicate
import org.junit.Before
import org.junit.Test

class CountersInteractorUnitTest : BaseTests() {

    @MockK(relaxed = true)
    lateinit var mockInfrastructure: CounterInfrastructure

    private lateinit var interactor: CountersInteractor

    @Before
    override fun setUp() {
        super.setUp()
        interactor = CountersInteractor(mockInfrastructure)
    }

    @Test
    fun `should get counters value with success`() {
        // given
        val expected = listOf(Counter("id1", "title1", 1))
        stubInfrastructureCounters(Single.just(expected))

        // when
        val testObserver = interactor.getCounters(fetchFromRemote = true).test()

        // then
        testObserver.assertCompleted(expected)
    }

    @Test
    fun `should throw error when get counters without success`() {
        // given
        val expected = NetworkError
        stubInfrastructureCounters(Single.error(expected))

        // when
        val testObserver = interactor.getCounters(fetchFromRemote = true).test()

        // then
        testObserver.assertWithError(expected)
    }

    @Test
    fun `should add a new counter with success`() {
        // given
        val expected = listOf(Counter("id1", "title1", 1))
        val request = CounterRequestAdd("Title")
        stubInfrastructureAddCounter(Single.just(expected))

        // when
        val testObserver = interactor.addCounter(request).test()

        // then
        testObserver.assertCompleted(expected)
    }

    @Test
    fun `should throw error when try to add counter with empty title`() {
        // given
        val expected = CounterBusiness.InvalidEmptyTitle
        val request = CounterRequestAdd("")
        stubInfrastructureAddCounter(Single.error(expected))

        // when
        val testObserver = interactor.addCounter(request).test()

        // then
        testObserver.assertWithError(expected)
    }

    @Test
    fun `should throw error when try to add counter with invalid title length`() {
        // given
        val expected = CounterBusiness.InvalidTitleSize
        val request = CounterRequestAdd("A")
        stubInfrastructureAddCounter(Single.error(expected))

        // when
        val testObserver = interactor.addCounter(request).test()

        // then
        testObserver.assertWithError(expected)
    }

    @Test
    fun `should delete counter with success`() {
        // given
        val expected = listOf<Counter>()
        val request = CounterRequest("awd")
        stubInfrastructureDeleteCounter(Single.just(expected))

        // when
        val testObserver = interactor.deleteCounter(request).test()

        // then
        testObserver.assertCompleted(expected)
    }

    @Test
    fun `should throw error when try to delete counter with invalid id`() {
        // given
        val expected = CounterBusiness.InvalidId
        val request = CounterRequest("")
        stubInfrastructureDeleteCounter(Single.error(expected))

        // when
        val testObserver = interactor.deleteCounter(request).test()

        // then
        testObserver.assertWithError(expected)
    }

    @Test
    fun `should increment counter with success`() {
        // given
        val expected = listOf<Counter>()
        val request = CounterRequest("awd")
        stubInfrastructureIncrementCounter(Single.just(expected))

        // when
        val testObserver = interactor.incrementCounter(request).test()

        // then
        testObserver.assertCompleted(expected)
    }

    @Test
    fun `should throw error when try to increment counter with invalid id`() {
        // given
        val expected = CounterBusiness.InvalidId
        val request = CounterRequest("")
        stubInfrastructureIncrementCounter(Single.error(expected))

        // when
        val testObserver = interactor.incrementCounter(request).test()

        // then
        testObserver.assertWithError(expected)
    }

    @Test
    fun `should decrement counter with success`() {
        // given
        val expected = listOf<Counter>()
        val request = CounterRequest("awd")
        stubInfrastructureDecrementCounter(Single.just(expected))

        // when
        val testObserver = interactor.decrementCounter(request).test()

        // then
        testObserver.assertCompleted(expected)
    }

    @Test
    fun `should throw error when try to decrement counter with invalid id`() {
        // given
        val expected = CounterBusiness.InvalidId
        val request = CounterRequest("")
        stubInfrastructureDecrementCounter(Single.error(expected))

        // when
        val testObserver = interactor.decrementCounter(request).test()

        // then
        testObserver.assertWithError(expected)
    }

    private fun stubInfrastructureCounters(counters: Single<List<Counter>>) {
        every { mockInfrastructure.getCounters(any()) } returns counters
    }

    private fun stubInfrastructureAddCounter(counters: Single<List<Counter>>) {
        every { mockInfrastructure.addCounter(any()) } returns counters
    }

    private fun stubInfrastructureDeleteCounter(counters: Single<List<Counter>>) {
        every { mockInfrastructure.deleteCounter(any()) } returns counters
    }

    private fun stubInfrastructureIncrementCounter(counters: Single<List<Counter>>) {
        every { mockInfrastructure.incrementCounter(any()) } returns counters
    }

    private fun stubInfrastructureDecrementCounter(counters: Single<List<Counter>>) {
        every { mockInfrastructure.decrementCounter(any()) } returns counters
    }
}