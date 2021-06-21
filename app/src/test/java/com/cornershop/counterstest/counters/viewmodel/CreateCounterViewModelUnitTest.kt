package com.cornershop.counterstest.counters.viewmodel

import com.cornershop.counterstest.base.BaseViewModelTests
import com.cornershop.counterstest.counter.domain.CountersInteractor
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.counter.viewmodel.CountersViewModel
import com.cornershop.counterstest.counter.viewmodel.CreateCounterViewModel
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
import junit.framework.Assert.assertNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CreateCounterViewModelUnitTest : BaseViewModelTests() {

    @MockK(relaxed = true)
    lateinit var mockInteractor: CountersInteractor

    private lateinit var viewModel: CreateCounterViewModel

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = CreateCounterViewModel(mockInteractor)
    }

    @Test
    fun `should post counters value with success`() {
        // given
        val counterTitle = "title"
        val publish = SingleSubject.create<List<Counter>>()
        val expected = listOf(Counter("id1", "title1", 1))
        stubInteractorAddCounter(publish)

        // when
        viewModel.createCounter(counterTitle)

        assertThat(viewModel.counterState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        publish.onSuccess(expected)

        // then
        val result = viewModel.counterState.value as StateMachineEvent.Success<List<Counter>>
        assertThat(viewModel.counterState.value).isExactlyInstanceOf(result.javaClass)
        assertEquals(result.value, expected)
        verify { mockInteractor.addCounter(any()) }
    }

    @Test
    fun `should throw error when get counters without success`() {
        // given
        val counterTitle = "title"
        val publish = SingleSubject.create<List<Counter>>()
        val expectedException = NetworkError
        stubInteractorAddCounter(publish)

        // when
        viewModel.createCounter(counterTitle)

        assertThat(viewModel.counterState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        publish.onError(expectedException)

        // then
        val result = viewModel.counterState.value as StateMachineEvent.Failure
        assertThat(viewModel.counterState.value).isExactlyInstanceOf(result.javaClass)
        assertEquals(result.exception, expectedException)
        verify { mockInteractor.addCounter(any()) }
    }

    private fun stubInteractorAddCounter(counters: Single<List<Counter>>) {
        every { mockInteractor.addCounter(any()) } returns counters
    }
}