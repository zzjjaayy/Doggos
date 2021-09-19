package com.jay.doggos.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jay.doggos.fragments.breedList.BreedListFragment
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class StatusEnum {
    LOADING, ERROR, DONE
}

class NetworkViewModel : ViewModel() {

    private val _allBreeds = MutableLiveData<List<String>>()
    val allBreeds: LiveData<List<String>> = _allBreeds

    private val _currentBreed = MutableLiveData<List<String>>()
    val currentBreed: LiveData<List<String>> = _currentBreed

    private val _randoms = MutableLiveData<List<String>>()
    val randoms: LiveData<List<String>> = _randoms

    private val _status = MutableLiveData<StatusEnum>()
    val status : LiveData<StatusEnum> = _status

    init {
        getAllBreeds()
    }

    fun clearCurrentBreed() {
        _currentBreed.value = null
    }

    private fun getAllBreeds() {
        viewModelScope.launch {
            _status.value = StatusEnum.LOADING
            try {
                val myResponse= DogApi.retrofitService.getBreeds()
                val jsonObj = JSONObject(myResponse)

                val keys : Iterator<*> = jsonObj.getJSONObject("message").keys()
                val listOfBreeds : MutableList<String> = mutableListOf()
                for(key in keys) {
                    listOfBreeds.add(key as String)
                }
                _allBreeds.value = listOfBreeds.toList()
                _status.value = StatusEnum.DONE
            } catch (e : Exception) {
                _status.value = StatusEnum.ERROR
                Log.d(BreedListFragment.LOG_TAG, e.stackTraceToString())
            }
        }
    }

    fun getCurrentBreed(breed : String) {
        viewModelScope.launch {
            _status.value = StatusEnum.LOADING
            try {
                val myResponse= DogApi.retrofitService.getCurrentBreed(breed)
                val jsonArray = JSONObject(myResponse).getJSONArray("message")

                val listOfImages : MutableList<String> = mutableListOf()
                for(i in 0 until jsonArray.length()) {
                    listOfImages.add(jsonArray[i] as String)
                }
                if(listOfImages.size < 10) {
                    _currentBreed.value = listOfImages.shuffled().toList()
                } else _currentBreed.value = listOfImages.shuffled().subList(0, 9).toList()

                Log.d(BreedListFragment.LOG_TAG, "Current breed -> ${currentBreed.value.toString()}")
                _status.value = StatusEnum.DONE
            } catch (e : Exception) {
                _status.value = StatusEnum.ERROR
                Log.d(BreedListFragment.LOG_TAG, e.stackTraceToString())
            }
        }
    }

    fun getRandoms() {
        viewModelScope.launch {
            _status.value = StatusEnum.LOADING
            try {
                val myResponse= DogApi.retrofitService.getRandom()
                val jsonArray = JSONObject(myResponse).getJSONArray("message")

                val listOfImages : MutableList<String> = mutableListOf()
                for(i in 0 until jsonArray.length()) {
                    listOfImages.add(jsonArray[i] as String)
                }
                _randoms.value = listOfImages.shuffled().toList()

                Log.d(BreedListFragment.LOG_TAG, "Randoms are -> ${randoms.value.toString()}")
                _status.value = StatusEnum.DONE
            } catch (e : Exception) {
                _status.value = StatusEnum.ERROR
                Log.d(BreedListFragment.LOG_TAG, e.stackTraceToString())
            }
        }
    }
}