package com.cs4520.assignment5

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object Api {
    const val BASE_URL: String = "https://kgtttq6tg9.execute-api.us-east-2.amazonaws.com/"
    const val ENDPOINT: String = "prod/random/"
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(Api.BASE_URL)
    .build()

interface ApiProductFetcher {
    @GET(Api.ENDPOINT)
    suspend fun getProducts(): Response<ProductList>
}

object ApiProduct {
    val service: ApiProductFetcher by lazy {
        retrofit.create(ApiProductFetcher::class.java)
    }
}