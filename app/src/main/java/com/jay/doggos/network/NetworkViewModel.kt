package com.jay.doggos.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NetworkViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response


    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            try {
                val breeds = DogApi.retrofitService.getBreeds()
                _response.value = breeds
            } catch (e : Exception) {
                _response.value = e.toString()
            }
        }
    }
}