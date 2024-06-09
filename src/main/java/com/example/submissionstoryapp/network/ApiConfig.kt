package com.example.submissionstoryapp.network

import android.content.Context
import com.example.submissionstoryapp.response.repository.Preference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private fun getInterceptor(token: String?): OkHttpClient {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            return if (token.isNullOrEmpty()) {
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()
            } else {
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(token))
                    .addInterceptor(loggingInterceptor)
                    .build()
            }
        }

        fun getApiService(context: Context): ApiService {

            val sharedPref = Preference.initPref(context, "onSignIn")
            val token = sharedPref.getString("token", null).toString()
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestHeaders)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}