package com.app.shoplist.data.network.response

import com.app.shoplist.data.model.ProductDetails
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("/")
    suspend fun getProducts(): Response<List<ProductDetails>>

}