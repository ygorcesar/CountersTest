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
import junit.framework.Assert.assertNull
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
        verify { mockInteractor.getCounters(any()) }
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

    @Test
    fun `should increment counter with success`() {
        // given
        val counter = Counter("awd", "title", 0)
        val publish = SingleSubject.create<List<Counter>>()
        val expected = listOf(counter)
        stubInteractorIncrementCounter(publish)

        // when
        viewModel.incrementCounter(counter)

        assertThat(viewModel.changeCounterCountState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        assertEquals(viewModel.counterToUpdate?.count, counter.count + 1)

        publish.onSuccess(expected)

        // then
        val result =
            viewModel.changeCounterCountState.value as StateMachineEvent.Success<List<Counter>>
        assertThat(viewModel.changeCounterCountState.value).isExactlyInstanceOf(result.javaClass)
        assertEquals(result.value, expected)
        assertNull(viewModel.counterToUpdate)
        verify { mockInteractor.incrementCounter(any()) }
    }

    @Test
    fun `should decrement counter with success`() {
        // given
        val counter = Counter("awd", "title", 1)
        val publish = SingleSubject.create<List<Counter>>()
        val expected = listOf(counter)
        stubInteractorDecrementCounter(publish)

        // when
        viewModel.decrementCounter(counter)

        assertThat(viewModel.changeCounterCountState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        assertEquals(viewModel.counterToUpdate?.count, counter.count - 1)

        publish.onSuccess(expected)

        // then
        val result =
            viewModel.changeCounterCountState.value as StateMachineEvent.Success<List<Counter>>
        assertThat(viewModel.changeCounterCountState.value).isExactlyInstanceOf(result.javaClass)
        assertEquals(result.value, expected)
        assertNull(viewModel.counterToUpdate)
        verify { mockInteractor.decrementCounter(any()) }
    }

    @Test
    fun `should delete counter with success`() {
        // given
        val publish = SingleSubject.create<List<Counter>>()
        val countersSelected = listOf(
            Counter("awd_1", "title_1", 1, true),
            Counter("awd_2", "title_2", 1, true),
            Counter("awd_3", "title_3", 1, false),
            Counter("awd_4", "title_4", 1, true),
            Counter("awd_5", "title_5", 1, false)
        )
        val countersShouldBeDeleted = countersSelected.filter { it.isSelected }.size
        countersSelected.forEach(viewModel::toggleSelectedCounter)
        stubInteractorDeleteCounter(publish)

        // when
        viewModel.deleteSelectedCounters()

        assertThat(viewModel.deleteCountersState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        publish.onSuccess(listOf())

        // then
        val result = viewModel.deleteCountersState.value as StateMachineEvent.Success<List<Counter>>
        assertThat(viewModel.deleteCountersState.value).isExactlyInstanceOf(result.javaClass)
        assertEquals(result.value, listOf<Counter>())
        assertEquals(viewModel.countersSelected.value, listOf<Counter>())
        verify(exactly = countersShouldBeDeleted) { mockInteractor.deleteCounter(any()) }
    }

    @Test
    fun `should filter counters by query text with success`() {
        // given
        val query = "CornerShop"
        val publish = SingleSubject.create<List<Counter>>()
        val counters = listOf(
            Counter("id1", "title1", 1),
            Counter("id2", "CornerShop Orders", 6),
            Counter("id3", "Corner", 1),
            Counter("id4", "CornerShop", 5),
            Counter("id5", "title5", 1),
            Counter("id6", "title5", 1)
        )
        val expected = counters.filter { counter ->
            counter.title.contains(query, ignoreCase = true) || query.isBlank()
        }
        stubInteractorGetCounters(publish)

        // when
        viewModel.getCounters()

        assertThat(viewModel.countersState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        publish.onSuccess(counters)

        viewModel.filterByQuery(query)

        // then
        val result = viewModel.countersState.value as StateMachineEvent.Success<List<Counter>>
        assertThat(viewModel.countersState.value).isExactlyInstanceOf(result.javaClass)
        assertEquals(result.value, expected)
        verify { mockInteractor.getCounters(any()) }
    }

    @Test
    fun `should filter counters by query text and not found any result`() {
        // given
        val expected = true
        val query = "Zerectecis xD"
        val publish = SingleSubject.create<List<Counter>>()
        val counters = listOf(
            Counter("id1", "title1", 1),
            Counter("id2", "CornerShop Orders", 6),
            Counter("id3", "Corner", 1),
            Counter("id4", "CornerShop", 5),
            Counter("id5", "title5", 1),
            Counter("id6", "title5", 1)
        )
        stubInteractorGetCounters(publish)

        // when
        viewModel.getCounters()

        assertThat(viewModel.countersState.value)
            .isExactlyInstanceOf(StateMachineEvent.Start.javaClass)

        publish.onSuccess(counters)

        viewModel.filterByQuery(query)

        // then
        assertEquals(viewModel.counterNotFound.value, expected)
        verify { mockInteractor.getCounters(any()) }
    }

    @Test
    fun `should toggle counters selected with success`() {
        // given
        val expected = listOf<Counter>()
        val counter = Counter("awd", "title", 1, isSelected = true)
        val counters = listOf(counter)

        // when
        viewModel.toggleSelectedCounter(counter)

        counter.isSelected = false
        assertEquals(viewModel.countersSelected.value, counters)

        viewModel.toggleSelectedCounter(counter)

        // then
        assertEquals(viewModel.countersSelected.value, expected)
    }

    @Test
    fun `should clear selected counters with success`() {
        // given
        val expected = listOf<Counter>()
        val counters = listOf(
            Counter("awd_1", "title_1", 1, isSelected = true),
            Counter("awd_2", "title_2", 1, isSelected = true),
            Counter("awd_3", "title_3", 1, isSelected = true),
            Counter("awd_4", "title_4", 1, isSelected = true)
        )

        // when
        counters.forEach(viewModel::toggleSelectedCounter)
        assertEquals(viewModel.countersSelected.value, counters)
        viewModel.clearSelectedCounters()

        // then
        assertEquals(viewModel.countersSelected.value, expected)
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

    private fun stubInteractorIncrementCounter(counters: Single<List<Counter>>) {
        every { mockInteractor.incrementCounter(any()) } returns counters
    }

    private fun stubInteractorDecrementCounter(counters: Single<List<Counter>>) {
        every { mockInteractor.decrementCounter(any()) } returns counters
    }

    private fun stubInteractorDeleteCounter(counters: Single<List<Counter>>) {
        every { mockInteractor.deleteCounter(any()) } returns counters
    }
}