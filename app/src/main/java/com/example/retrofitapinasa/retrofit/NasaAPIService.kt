package com.example.retrofitapinasa.retrofit

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NasaAPIService {
    @GET("apod?api_key=2HLf0TUZRdB8Qf4JxQfZ2cA8oLQfvVR3zi2wh42b")
    fun obtenerImagenAPI(@Query ("date") date: String): Call<ImagenNasa>
}