package com.tabjin.mvvm

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class WordListAdapter: RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WordViewHolder {
        val view = View.inflate(p0.context,R.layout.recyclerview_item,p0)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(mWords == null){
            return 0
        }else{
            return mWords!!.size
        }
    }

    override fun onBindViewHolder(p0: WordViewHolder, p1: Int) {
        var word:Word = mWords!![p1]
        if(word == null){
            p0.wordItemView!!.text = "no word"
        }else{
            p0.wordItemView!!.text = word.mWord
        }
    }

    var mWords:List<Word>? = null

    fun setWords(list:List<Word>){
        mWords = list
        notifyDataSetChanged()
    }

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wordItemView: TextView? = null
        fun constructor(view:View){
            wordItemView = view.findViewById(R.id.textView)
        }
    }


}