package com.example.submissionstoryapp.network

import com.example.submissionstoryapp.response.login.LoginResponse
import com.example.submissionstoryapp.response.daftar.DaftarResponse
import com.example.submissionstoryapp.response.story.PostStoryResponse
import com.example.submissionstoryapp.response.story.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postSignUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): DaftarResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ) : StoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location: Int? = 0,
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): PostStoryResponse
}