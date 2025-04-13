package com.dhkim.bookmark

import com.dhkim.core.testing.series.FakeAddSeriesBookmarkUseCase
import com.dhkim.core.testing.series.FakeDeleteSeriesBookmarkUseCase
import com.dhkim.core.testing.series.FakeGetSeriesBookmarksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class BookmarkViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    val dispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: BookmarkViewModel

    private val getBookmarksUseCase = FakeGetSeriesBookmarksUseCase()
    private val addBookmarkUseCase = FakeAddSeriesBookmarkUseCase()
    private val deleteBookmarkUseCase = FakeDeleteSeriesBookmarkUseCase()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = BookmarkViewModel(
            getSeriesBookmarksUseCase = getBookmarksUseCase,
            addSeriesBookmarkUseCase = addBookmarkUseCase,
            deleteSeriesBookmarkUseCase = deleteBookmarkUseCase
        )
    }

    @Test
    fun `북마크 가져오기`() = runTest {
        viewModel.uiState.first()
        println("${viewModel.uiState.value}")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}