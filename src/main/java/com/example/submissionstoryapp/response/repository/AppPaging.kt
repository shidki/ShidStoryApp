package com.example.submissionstoryapp.response.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submissionstoryapp.network.ApiService
import com.example.submissionstoryapp.response.story.listStory


class AppPaging(private val apiService: ApiService): PagingSource<Int, listStory>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, listStory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, listStory> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(page, params.loadSize)

            val stories = responseData.listStory?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = stories,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (stories.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}