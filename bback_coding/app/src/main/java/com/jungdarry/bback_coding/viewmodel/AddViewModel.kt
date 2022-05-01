package com.jungdarry.bback_coding.viewmodel

import android.app.Application
import android.inputmethodservice.KeyboardView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.ObservableField
import androidx.lifecycle.*


//class AddViewModel(application: Application) : AndroidViewModel(application) {
class AddViewModel : ViewModel() {

    private val _currentTitle = MutableLiveData<String>()
    private val _currentContent = MutableLiveData<String>()
    private val _currentLength = MutableLiveData<Int>()

    val currentTitle : LiveData<String>
        get() = _currentTitle

    val currentContent : LiveData<String>
        get() = _currentContent

    val currentLength : LiveData<Int>
        get() = _currentLength

    fun updateLen(len:Int){
        _currentLength.value = len
    }

    fun updateTitle(str:String){
        _currentTitle.value = str
    }

    init{
        _currentTitle.value = ""
        _currentContent.value = ""
        _currentLength.value = 0
    }

    companion object {
        private const val TAG = "태그"
    }


}