package com.jungdarry.bback_coding.memoDB

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MemoRepository(application: Application) : ViewModel() {

    private val memoDatabase = MeMoDatabase.getInstance(application)!!
    private val memoDao: MemoDao = memoDatabase.memoDao()
    private val memos: LiveData<List<Memo>> = memoDao.getAll()
    private var filtermemos : LiveData<List<Memo>> = memoDao.getFilterd("")

    fun getAll(): LiveData<List<Memo>> {
        return memos
    }

    fun getFilterMemo(findstr:String): LiveData<List<Memo>> {
        // ViewModel에서 DB에 접근을 요청할 때 수행할 함수를 만들어둔다.주의할 점은 Room DB를 메인 스레드에서 접근하려 하면 크래쉬가 발생한다
        try {
            val thread = Thread(Runnable {
                filtermemos = memoDao.getFilterd(findstr)
            })
            thread.start()
        } catch (e: Exception) { }
        return filtermemos
    }

    fun insert(memo: Memo) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                memoDao.insert(memo)
            } catch (e:java.lang.Exception){
                // Repository 에서의 예외처리는 예외처리를 여기서 하기
            }
        }

//        try {
//            ViewModel에서 DB에 접근을 요청할 때 수행할 함수를 만들어둔다.주의할 점은 Room DB를 메인 스레드에서 접근하려 하면 크래쉬가 발생한다
//            val thread = Thread(Runnable {
//            thread.start()
//        } catch (e: Exception) { }
    }

    fun delete(memo: Memo) {
        try {
            val thread = Thread(Runnable {
                memoDao.delete(memo)
            })
            thread.start()
        } catch (e: Exception) { }
    }

}