package com.app.shoplist

import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("/")
    suspend fun getProducts(): Response<List<ProductDetails>>

}