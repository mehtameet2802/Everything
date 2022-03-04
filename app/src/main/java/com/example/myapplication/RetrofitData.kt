package com.example.myapplication

import retrofit2.Call
import retrofit2.http.*

interface RetrofitData {

    @GET("popular?api_key=a631feaba1636b38b4d07a2fb9d1ac4a&page=")
    fun getMovies(
        @Query("page") page: Int
    ): Call<information>

    @GET("now_playing?api_key=a631feaba1636b38b4d07a2fb9d1ac4a&page=")
    fun getUpcoming(
        @Query("page") page: Int
    ): Call<information2>

    @GET("users")
//    @Headers("Accept:application/json","Content-Type:application/json")
    fun user(): Call<List<data>>

    @POST("users")
    @Headers("Authorization: Bearer 07fd24f84b0c9446b57061a51872b2559bee0a264a6f071ada72b2379fc5f961")
    fun createUser(@Body params: data): Call<data>

    @PATCH("users/{user_id}")
    @Headers("Authorization: Bearer 07fd24f84b0c9446b57061a51872b2559bee0a264a6f071ada72b2379fc5f961")
    fun updateUser(
        @Path("user_id") user_id: String?,
        @Body params: data
    ): Call<data>
//    200 is ok

    @DELETE("users/{user_id}")
    @Headers("Authorization: Bearer 07fd24f84b0c9446b57061a51872b2559bee0a264a6f071ada72b2379fc5f961")
    fun deleteUser(@Path("user_id") user_id: String?):Call<response>
}