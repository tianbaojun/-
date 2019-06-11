package com.tabjin.shorttext

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recycler.adapter = Myadapter()


    }


    class Myadapter: RecyclerView.Adapter<Myadapter.Holder>(){
        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.tv.text = "$position"
//            holder.tv.layoutParams.height = 100
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myadapter.Holder{
            return Holder(TextView(parent.context))
        }
        override fun getItemCount(): Int {
            return 80
        }

                class Holder(v: TextView):RecyclerView.ViewHolder(v){
                    var tv = v
                }
    }
}
