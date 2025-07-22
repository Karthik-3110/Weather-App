package com.example.weatherapp.api

sealed class networkresponce<out T> {
    data class Success<out T>(val data : T) : networkresponce<T>()
    data class Error(val message : String) : networkresponce<Nothing>()
    object Loading : networkresponce<Nothing>()
}