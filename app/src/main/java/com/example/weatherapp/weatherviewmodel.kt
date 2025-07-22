package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.constant
import com.example.weatherapp.api.networkresponce
import com.example.weatherapp.api.retrofitinstance
import com.example.weatherapp.api.weathermodel
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class weatherViewModel :ViewModel() {

    private val weatherApi = retrofitinstance.weatherApi
    private val _weatherresult = MutableLiveData<networkresponce<weathermodel>>()
    val weatherresult : LiveData<networkresponce<weathermodel>> =_weatherresult
    fun getData(city : String){
        _weatherresult.value = networkresponce.Loading
        viewModelScope.launch {
            try {
                val response=  weatherApi.getWeather(constant.apikey,city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherresult.value = networkresponce.Success(it)
                    }
                }else{
                    _weatherresult.value = networkresponce.Error("Failed to load data")
                }
            }catch (e : Exception){
                _weatherresult.value = networkresponce.Error("Failed to load data")
            }

        }

    }
}