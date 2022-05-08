package com.jungdarry.bback_coding.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jungdarry.bback_coding.R
import com.jungdarry.bback_coding.memoDB.Memo
import java.util.*

class MemoAdapter(val memoItemClick: (Memo) -> Unit, val memoItemDeleteClick: (Memo) -> Unit)
    : RecyclerView.Adapter<MemoAdapter.ViewHolder>() {
    private var memos: List<Memo> = listOf()
    private var totalmemos: List<Memo> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_memo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return memos.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(memos[position])
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val titleTv = itemView.findViewById<TextView>(R.id.item_memo_title_tv)
        private val contentTv = itemView.findViewById<TextView>(R.id.item_memo_content_tv)
        private val deleteIv = itemView.findViewById<ImageView>(R.id.item_memo_delete_iv)
        private val lockIv = itemView.findViewById<ImageView>(R.id.item_memo_lock_iv)
        private val dateTv = itemView.findViewById<TextView>(R.id.item_memo_date_tv)
        fun bind(memo: Memo) {
            titleTv.text = memo.title
            if(memo.ispw){
                lockIv.visibility = View.VISIBLE
                contentTv.text = "비밀글 입니다."
            }else{
                lockIv.visibility = View.GONE
                contentTv.text = memo.content
            }
            itemView.setOnClickListener {
                memoItemClick(memo)
            }
            deleteIv.setOnClickListener {
                memoItemDeleteClick(memo)
            }
            dateTv.text = memo.date
        }
    }

    fun setMemos(memos: List<Memo>,ordering: Boolean) {
        this.totalmemos = memos
        if(!ordering){
            this.memos = totalmemos.sortedWith(compareBy({ it.date }))
        }else{
            this.memos = totalmemos.sortedWith(compareByDescending ({ it.date }))
        }
        notifyDataSetChanged()
    }

    fun filter(findStr: String,ordering:Boolean) {
        var charText = findStr
        charText = charText.toLowerCase(Locale.getDefault())
        var memostemp: MutableList<Memo> = mutableListOf()
        if (charText.length == 0) {
            memostemp.addAll(totalmemos)
        } else {
            for (recent in totalmemos) {
                if (recent.title.toLowerCase().contains(charText)) {
                    memostemp.add(recent)
                } else if (recent.content.toLowerCase().contains(charText)) {
                    memostemp.add(recent)
                }
            }
        }

        if(!ordering){
            memos = memostemp.sortedWith(compareBy({ it.date }))
        }else{
            memos = memostemp.sortedWith(compareByDescending ({ it.date }))
        }
        notifyDataSetChanged()
    }

    fun ordering(ordering:Boolean){
        if(ordering){
            this.memos = totalmemos.sortedWith(compareBy({ it.date }))
        }else{
            this.memos = totalmemos.sortedWith(compareByDescending ({ it.date }))
        }
        notifyDataSetChanged()
    }
}