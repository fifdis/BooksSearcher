package com.marafinet.bookssearcher

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BooksViewModel : ViewModel() {
    val books = MutableStateFlow<List<BookItem>>(emptyList())

    init {
        fetchBooks("Гарри Поттер")
    }

    private fun fetchBooks(query: String) {
        viewModelScope.launch {
            try {
                books.value = RetrofitClient.api.searchBooks(query).items ?: emptyList()
                Log.d("BooksViewModel", "API Response: $books.value")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("BooksViewModel", "Error fetching books", e)
            }
        }
    }

}