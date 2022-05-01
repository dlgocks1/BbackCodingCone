package com.jungdarry.bback_coding.ui

import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T: ViewDataBinding>
    (@LayoutRes private val layoutId: Int): AppCompatActivity() {

    protected lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSetContentView()

//        Log.d(localClassName,"OnCreate")
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        initView()
        initViewModel()
        initListener()
    }

    protected open fun beforeSetContentView() {}
    protected open fun initView() {}
    protected open fun initViewModel() {}
    protected open fun initListener() {}

}