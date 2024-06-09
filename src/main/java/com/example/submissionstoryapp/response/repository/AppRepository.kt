package com.example.submissionstoryapp.response.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submissionstoryapp.network.ApiService
import com.example.submissionstoryapp.response.daftar.DaftarResponse
import com.example.submissionstoryapp.response.login.LoginResponse
import com.example.submissionstoryapp.response.story.PostStoryResponse
import com.example.submissionstoryapp.response.story.StoryResponse
import com.example.submissionstoryapp.response.story.listStory
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepository(private val apiService: ApiService) {
    fun getStories(): LiveData<PagingData<listStory>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                AppPaging(apiService)
            }
        ).liveData
    }

    fun getStoriesWithLocation(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation(1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("ListStoryViewModel", "getStoriesWithLocation: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postStory(file: MultipartBody.Part, description: RequestBody): LiveData<Result<PostStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postStory(file, description)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("CreateStoryViewModel", "postStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postSignUp(name: String, email: String, password: String): LiveData<Result<DaftarResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postSignUp(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "postSignUp: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "postLogin: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
}