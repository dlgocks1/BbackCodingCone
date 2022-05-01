package com.jungdarry.bback_coding.ui.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.widget.textChanges
import com.jungdarry.bback_coding.R
import com.jungdarry.bback_coding.databinding.ActivityMainBinding
import com.jungdarry.bback_coding.memoDB.Memo
import com.jungdarry.bback_coding.ui.BaseActivity
import com.jungdarry.bback_coding.ui.add.AddActivity
import com.jungdarry.bback_coding.viewmodel.MainViewModel
import com.jungdarry.bback_coding.viewmodel.MemoViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity :  BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    companion object {
        private const val TAG = "로그"
    }
    private lateinit var memoViewModel: MemoViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter : MemoAdapter
    private var myCompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        //해당 액티비티가 날라갈 때 Observable 다 날리기
        this.myCompositeDisposable.clear()
        super.onDestroy()
    }
    override fun initView() {
        super.initView()
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        memoViewModel = ViewModelProvider(this)[MemoViewModel::class.java]
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.lifecycleOwner = this
        binding.memoViewModel = memoViewModel
        binding.mainViewModel = mainViewModel

        binding.mainRadiogroup.check(R.id.main_radio_bt1)
        binding.mainRadiogroup.setOnCheckedChangeListener{group, checkId->
            when(checkId){
                R.id.main_radio_bt1 -> mainViewModel.updateOrdering(true)
                R.id.main_radio_bt2 -> mainViewModel.updateOrdering(false)
            }
        }

        adapter = MemoAdapter({ memo ->
            if (memo.ispw) {
                Log.d(TAG, "MainActivity MemoAdapter memoPW :" + memo.pw.toString())
                showPwDialog(memo)
            } else {
                startAddactivity(memo)
            }
        }, { memo ->
            deleteDialog(memo)
        })

        val lm = LinearLayoutManager(this)
        binding.mainRv.adapter = adapter
        binding.mainRv.layoutManager = lm
        binding.mainRv.setHasFixedSize(true)

        mainViewModel.searchText.observe(this, Observer {
            adapter.filter(it,mainViewModel.ordering.value.toString().toBoolean())
        })

        mainViewModel.ordering.observe(this, Observer {
            adapter.filter(mainViewModel.searchText.value.toString(),it)
        })

        memoViewModel.getAll().observe(this, Observer<List<Memo>>{
                memos->//updateUI
            mainViewModel.updateSearchText("")
            adapter.setMemos(memos,mainViewModel.ordering.value.toString().toBoolean())
        })

        binding.mainButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        // SearchView의 EditText 옵저버블 만들기. rxbinding으로
        val editTextChangeObservable = binding.searchTermEditText.textChanges()

        // 구독하는 subcribe 만들기, 옵저버블에 연산자 추가
        val searchEditTextSubScription: Disposable =
            editTextChangeObservable
                // 글자가 입력되고 0.3초 후에 onNext 이벤트 데이터 흘려보내기
                .debounce(300, TimeUnit.MILLISECONDS)
                // io 쓰레드에서 돌리겠다.
                // Scheduler instance intended for IO-bound work.
                //네트워크 요청, 파일 읽기, 쓰기 등 을 IO쓰레드
                .subscribeOn(Schedulers.single())
                //구독 행위를 통해 이벤트 응답 받기
                .subscribeBy(
                    onNext = {
                        Log.d(TAG, "onNext : $it")
                        //TODO:: 흘러온 이벤트 데이터로 리사이클러뷰 필터링
                        runOnUiThread {
//                            adapter.filter(it.toString())
                            // 네트워킹 작업을 안함
                            mainViewModel.updateSearchText(it.toString())
                        }
                    },
                    onComplete = {
                        Log.d(TAG, "onComplete")
                    },
                    onError = {
                        Log.d(TAG, "onError : $it")
                    }
                )

        //구독 관리
        myCompositeDisposable.add(searchEditTextSubScription)
    }

    private fun startAddactivity(memo: Memo) {
        val intent = Intent(this, AddActivity::class.java)
        intent.putExtra(AddActivity.EXTRA_MEMO_TITLE, memo.title)
        intent.putExtra(AddActivity.EXTRA_MEMO_CONTENT, memo.content)
        intent.putExtra(AddActivity.EXTRA_MEMO_ID, memo.id)
        intent.putExtra(AddActivity.EXTRA_MEMO_ISPW, memo.ispw)
        intent.putExtra(AddActivity.EXTRA_MEMO_PW, memo.pw)
        startActivity(intent)
    }


    private fun showPwDialog(memo: Memo) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.pw_dialog, null)
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("비밀번호를 입력해주세요")
            .setPositiveButton("확인") { dialog, which ->
                val textView: EditText = view.findViewById(R.id.pw_dialog_edittv)
                if (textView.text.toString().toInt() == memo.pw) {
                    startAddactivity(memo)
                }
            }
            .setNeutralButton("취소", null)
            .create()

        //  여백 눌러도 창 안없어지게
        alertDialog.setCancelable(false)
        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun deleteDialog(memo: Memo) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("메모를 지우시겠습니까?")
            .setNegativeButton("아니요") { _, _ -> }
            .setPositiveButton("예") { _, _ ->
                memoViewModel.delete(memo)
            }
        builder.show()
    }

}

//## 과제 ##
//못한 것
//- 사용자는 메모를 검색할 수 있어야 한다.

//주제: 메모서비스 만들기 (앱,웹 상관없음)
//화면구성:
//- 메모 목록화면
//- 메모 상세화면
//- 메모 편집, 작성 화면

//⭐️기능:
//- 사용자는 작성한 메모 목록을 볼 수 있어야함
//- 메모 목록에 노출되는 메모는 작성된 메모문장 한줄만 노출된다.
//- 메모 작성 페이지에서 메모 작성이 가능하다.
//- 메모를 작성할때 작성된 메모의 글자수가 노출된다.
//- 사용자는 메모를 편집할 수 있어야 한다.
//- 사용자는 메모를 삭제할 수 있어야 한다.
//- 작성된 메모는 비밀메모로 변경이 가능하다.

//
//비밀메모:
//- 메모목록에 메모 문장이 노출되지 않는다.
//- 메모 목록 화면에는 “비밀메모 입니다” 혹은 잠금 표시로 노출된다.
//- 메모 목록에서 상세보기클릭시 비밀메모인 경우 암호를 입력해야 메모 상세 화면으로 이동이 가능하다.
//- 일반메모는 메모 상세화면에서 비밀메모로 변환이 가능하다.
//- 일반메모에서 비밀메모로 설정시 비밀번호 입력창이 뜨고 비밀번호를 입력하면 비밀메모로 바뀐다.

//- 위 요건들 포함 추가 기능 및 화면 자유롭게 추가가능