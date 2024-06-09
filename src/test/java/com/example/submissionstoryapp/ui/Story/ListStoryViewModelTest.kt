package com.example.submissionstoryapp.ui.Story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.submissionstoryapp.DataDummy
import com.example.submissionstoryapp.MainDispatcherRule
import com.example.submissionstoryapp.getOrAwaitValue
import com.example.submissionstoryapp.response.repository.AppRepository
import com.example.submissionstoryapp.response.story.listStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)

class ListStoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var appRepository: AppRepository

    @Test
    fun `when Get Story Should Not Null and Return Sukses`() = runTest {
        val dummyStory = DataDummy.generateDummyListStory()
        val data: PagingData<listStory> = StoryPagingSource.snapshot(dummyStory.listStory)
        val expectedQuote = MutableLiveData<PagingData<listStory>>()
        expectedQuote.value = data
        Mockito.`when`(appRepository.getStories()).thenReturn(expectedQuote)

        val listStoryViewModel = ListStoryViewModel(appRepository)
        val actualQuote: PagingData<listStory> = listStoryViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.listStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory.listStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<listStory> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<listStory>>()
        expectedQuote.value = data
        Mockito.`when`(appRepository.getStories()).thenReturn(expectedQuote)
        val listStoryViewModel = ListStoryViewModel(appRepository)
        val actualQuote: PagingData<listStory> = listStoryViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }

}

class StoryPagingSource : PagingSource<Int, LiveData<List<listStory>>>() {
    companion object {
        fun snapshot(items: List<listStory>): PagingData<listStory> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<listStory>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<listStory>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}
val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}