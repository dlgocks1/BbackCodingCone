package com.jungdarry.bback_coding.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jungdarry.bback_coding.memoDB.Memo
import com.jungdarry.bback_coding.memoDB.MemoRepository

// 만약 ViewModel이 액티비티의 context를 쓰게 되면, 액티비티가 destroy 된 경우에는 메모리 릭이 발생할 수 있다.
// 따라서 Application Context를 사용하기 위해 Applicaion을 인자로 받는다.
class MemoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MemoRepository(application)
    private val memos = repository.getAll()

    fun getAll(): LiveData<List<Memo>> {
        return this.memos
    }

    fun getFilterd(findstr : String): LiveData<List<Memo>> {
        val filteredmemos = repository.getFilterMemo(findstr)
        return filteredmemos
    }

    fun insert(memo: Memo) {
        repository.insert(memo)
    }

    fun delete(memo: Memo) {
        repository.delete(memo)
    }
}