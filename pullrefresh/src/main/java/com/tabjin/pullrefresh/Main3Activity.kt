package com.tabjin.pullrefresh

import android.app.ActionBar
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.appsdream.nestrefresh.base.AbsRefreshLayout
import cn.appsdream.nestrefresh.base.OnPullListener
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.activity_main3.view.*

class Main3Activity : AppCompatActivity(),OnPullListener {
    override fun onRefresh(listLoader: AbsRefreshLayout?) {
        adapter.list.clear()
        for(i in 1 .. adapter.list.size+20) {
            adapter.list.add(i.toString())
        }
        adapter.notifyDataSetChanged()
        refreshLayout.onLoadFinished()
    }

    override fun onLoading(listLoader: AbsRefreshLayout?) {
        for(i in adapter.list.size .. adapter.list.size+20) {
            adapter.list.add(i.toString())
        }
        adapter.notifyDataSetChanged()

        refreshLayout.onLoadFinished()
    }

    var adapter = Main3Activity.MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        refreshLayout.setOnLoadingListener(this)

        recycler.layoutManager = LinearLayoutManager(this@Main3Activity,LinearLayoutManager.VERTICAL,false)
        recycler.adapter = adapter
        onLoading(null)
    }


    class MyAdapter: RecyclerView.Adapter<MyAdapter.MyHolder>(){

        var list:ArrayList<String> = ArrayList<String>()

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int):MyHolder {
            var c = LayoutInflater.from(p0.context).inflate(R.layout.text,p0,false)
//            var c = View.inflate(p0.context, R.layout.text, null)
            return MyHolder(c)
        }

        override fun getItemCount(): Int {
            if(list == null){
                return 0
            }else{
                return list.size
            }
        }

        override fun onBindViewHolder(p0: MyHolder, p1: Int) {
            p0.tv.text = p1.toString()
            p0.tv.setOnClickListener{
                var intent = Intent()
                intent.data = Uri.parse("youtil://com.youtil.ec/splash")
                p0.tv.context.startActivity(intent)
            }
        }

        class MyHolder(v: View):RecyclerView.ViewHolder(v){
            var tv = v.findViewById<TextView>(R.id.textView)
        }

    }
}
