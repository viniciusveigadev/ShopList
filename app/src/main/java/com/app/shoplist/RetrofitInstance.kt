package com.app.shoplist

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: ProductService by lazy {
        Retrofit.Builder()
            .baseUrl("https://assessment-edvora.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductService::class.java)
    }
}