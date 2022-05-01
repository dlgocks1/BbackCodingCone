package com.jungdarry.bback_coding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _searchText = MutableLiveData<String>()
    private var _ordering = MutableLiveData<Boolean>()

    val searchText : LiveData<String>
        get() = _searchText
    val ordering : LiveData<Boolean>
        get() = _ordering

    fun updateOrdering(ordering:Boolean){
        _ordering.value = ordering
    }

    fun updateSearchText(findstr:String){
        _searchText.value = findstr
    }

    init{
        _searchText.value = ""
        _ordering.value = true
    }

}