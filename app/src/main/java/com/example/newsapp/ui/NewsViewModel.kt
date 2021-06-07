package com.example.newsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.GetNewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.ResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class NewsViewModel(private val newsRepo: NewsRepository): ViewModel(){

    private val _GetNewsByTopicResponse = MutableLiveData<ResponseHandler<GetNewsResponse>>()
    val getNewsByTopicResponse: LiveData<ResponseHandler<GetNewsResponse>> = _GetNewsByTopicResponse

    fun getNewsByQuery(topic:String) {
        Timber.d("topic $topic")
        _GetNewsByTopicResponse.postValue(ResponseHandler.Loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            _GetNewsByTopicResponse.postValue(newsRepo.getNewsByQuery(topic))
        }
    }

}