package com.cornershop.counterstest.counters.viewmodel

import com.cornershop.counterstest.base.BaseViewModelTests
import com.cornershop.counterstest.counter.domain.CountersInteractor
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.counter.viewmodel.CountersViewModel
import com.cornershop.counterstest.utils.data.NetworkError
import com.cornershop.counterstest.utils.data.StateMachineEvent
import com.cornershop.counterstest.utils.data.UnauthorizedError
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifySequence
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import junit.framework.Assert.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CountersViewModelUnitTest : BaseViewModelTests() {

    @MockK(relaxed = true)
    lateinit var mockInteractor: CountersInteractor

    private lateinit var viewModel: CountersViewModel

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = CountersViewModel(mockInteractor)
    }

    @Test
    fun `should post counters value with success`() {
        // given
        val publish = SingleSubject.create<List<Counter>>()
        val expected = listOf(Counter("id1", "title1", 1))
        stubInteractorGetCounters(publish)

        // when
        viewModel.getCounters()

        assertThat(viewModel.countersState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        publish.onSuccess(expected)

        // then
        val result = viewModel.countersState.value as StateMachineEvent.Success<List<Counter>>
        assertThat(viewModel.countersState.value).isExactlyInstanceOf(result.javaClass)
        assertEquals(result.value, expected)
    }

    @Test
    fun `should throw error when get counters without success`() {
        // given
        val publish = SingleSubject.create<List<Counter>>()
        val expectedException = UnauthorizedError
        stubInteractorGetCounters(publish)

        // when
        viewModel.getCounters()

        assertThat(viewModel.countersState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        publish.onError(expectedException)

        // then
        val result = viewModel.countersState.value as StateMachineEvent.Failure
        assertThat(viewModel.countersState.value).isExactlyInstanceOf(result.javaClass)
        assertEquals(result.exception, expectedException)
        verify { mockInteractor.getCounters(fetchFromRemote = true) }
    }

    @Test
    fun `should fetch data from local store when throw network error trying to get counters`() {
        // given
        val publishFromRemote = SingleSubject.create<List<Counter>>()
        val publishFromLocal = SingleSubject.create<List<Counter>>()
        val expected = listOf(Counter("id1", "title1", 1))
        val expectedException = NetworkError
        stubInteractorGetCounters(publishFromRemote, fetchFromRemote = true)
        stubInteractorGetCounters(publishFromLocal, fetchFromRemote = false)

        // when
        viewModel.getCounters()

        assertThat(viewModel.countersState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        publishFromRemote.onError(expectedException)
        publishFromLocal.onSuccess(expected)

        // then
        val result = viewModel.countersState.value as StateMachineEvent.Success<List<Counter>>
        assertThat(viewModel.countersState.value).isExactlyInstanceOf(result.javaClass)
        assertEquals(result.value, expected)
        verifySequence {
            mockInteractor.getCounters(fetchFromRemote = true)
            mockInteractor.getCounters(fetchFromRemote = false)
        }
    }

    private fun stubInteractorGetCounters(counters: Single<List<Counter>>) {
        every { mockInteractor.getCounters(any()) } returns counters
    }

    private fun stubInteractorGetCounters(
        counters: Single<List<Counter>>,
        fetchFromRemote: Boolean
    ) {
        every { mockInteractor.getCounters(fetchFromRemote) } returns counters
    }
}