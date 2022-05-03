package com.jungdarry.bback_coding.ui.add

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jungdarry.bback_coding.R
import com.jungdarry.bback_coding.databinding.ActivityAddmemoBinding
import com.jungdarry.bback_coding.memoDB.Memo
import com.jungdarry.bback_coding.ui.BaseActivity
import com.jungdarry.bback_coding.viewmodel.AddViewModel
import com.jungdarry.bback_coding.viewmodel.MemoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddActivity :BaseActivity<ActivityAddmemoBinding>(R.layout.activity_addmemo) {

    private lateinit var memoViewModel: MemoViewModel
    private lateinit var addViewModel:AddViewModel
    private var id: Int? = null
    private var ispw:Boolean = false
    private var pw:Int = -1


    override fun initView() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_addmemo)
        memoViewModel = ViewModelProvider(this)[MemoViewModel::class.java]
        addViewModel = ViewModelProvider(this)[AddViewModel::class.java]
        binding.lifecycleOwner = this
        binding.addViewModel = addViewModel

        if (intent != null && intent.hasExtra(EXTRA_MEMO_TITLE) && intent.hasExtra(EXTRA_MEMO_CONTENT)
            && intent.hasExtra(EXTRA_MEMO_ID) && intent.hasExtra(EXTRA_MEMO_ISPW) && intent.hasExtra(
                EXTRA_MEMO_PW)
        ) {
            addViewModel.updateTitle(intent.getStringExtra(EXTRA_MEMO_TITLE).toString())
            binding.addContentEdittv.setText(intent.getStringExtra(EXTRA_MEMO_CONTENT))
            addViewModel.updateLen(intent.getStringExtra(EXTRA_MEMO_CONTENT)!!.length)
            id = intent.getIntExtra(EXTRA_MEMO_ID, -1)

            if(intent.getBooleanExtra(EXTRA_MEMO_ISPW,false)){
                binding.addSecretCb.isChecked = true
                binding.addPwEdittv.visibility = View.VISIBLE
                binding.addPwEdittv.setText(intent.getIntExtra(EXTRA_MEMO_PW,-1).toString())
                ispw = intent.getBooleanExtra(EXTRA_MEMO_ISPW,false)
                pw = intent.getIntExtra(EXTRA_MEMO_PW,-1)
            }
        }

        addViewModel.currentLength.observe(this, Observer {
            if(it<1){
                binding.addContentLenTv.visibility = View.GONE
            }else{
                binding.addContentLenTv.visibility = View.VISIBLE
                binding.addContentLenTv.text = "$it 글자"
            }
        })

        addViewModel.currentTitle.observe(this, Observer {
            //할게 없음
        })
        addListener()
    }


    private fun addListener() {

        binding.addSecretCb.setOnCheckedChangeListener{view,isChecked->
            if(isChecked){
                ispw = true
                binding.addPwEdittv.visibility=View.VISIBLE
            }else{
                ispw = false
                binding.addPwEdittv.visibility=View.GONE
                binding.addPwEdittv.setText("")
            }
        }

        binding.homeEdittextTitle.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addViewModel.updateTitle(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.addContentEdittv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                addViewModel.updateLen(s.length)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        binding.addButton.setOnClickListener {
            val title = addViewModel.currentTitle.value.toString()
            val content = binding.addContentEdittv.text.toString()
            val now = System.currentTimeMillis()
            val date = Date(now)
            val sdf : SimpleDateFormat = SimpleDateFormat("yyyy HH:mm:s")

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "내용과 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }else if(binding.addSecretCb.isChecked && binding.addPwEdittv.length() != 4){
                Toast.makeText(this, "패스워드를 4자리 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                if(ispw){ pw = binding.addPwEdittv.text.toString().toInt() }
                val contact = Memo(id, title, content,ispw,pw,sdf.format(date).toString())
                memoViewModel.insert(contact)
                finish()
            }
        }

    }


    companion object {
        const val EXTRA_MEMO_TITLE = "EXTRA_MEMO_TITLE"
        const val EXTRA_MEMO_CONTENT = "EXTRA_MEMO_CONTENT"
        const val EXTRA_MEMO_ID = "EXTRA_MEMO_ID"
        const val EXTRA_MEMO_ISPW = "EXTRA_MEMO_ISPW"
        const val EXTRA_MEMO_PW = "EXTRA_MEMO_PW"
        private const val TAG = "로그"
    }

}