package com.marafinet.bookssearcher

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksAPI {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BookResponse
}
