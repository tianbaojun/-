package com.tabjin.twelveanimals.adapter

import android.annotation.SuppressLint
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tabjin.twelveanimals.R
import com.tabjin.twelveanimals.Tool
import com.tabjin.twelveanimals.bean.Type

open class TypeAdapter : RecyclerView.Adapter<TypeAdapter.Holder>() {
    var data:ArrayList<Type> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var v = View.inflate(parent.context, R.layout.item1,null)
        return Holder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        var bean = data[position]
        holder.name.text = bean.name
        holder.recycler.layoutManager = GridLayoutManager(holder.name.context,
                7,GridLayoutManager.VERTICAL,false)
        val adapter= ItemTypeAdapter()
        adapter.data.addAll(bean.data)
        holder.recycler.adapter = adapter

    }

    class Holder(view: View): RecyclerView.ViewHolder(view) {
        var name:TextView = view.findViewById(R.id.tv_name)
        var recycler:RecyclerView = view.findViewById(R.id.item_recycler)
        init {
            recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.top = Tool.dip2px(parent.context, 5f)
                    outRect.bottom = Tool.dip2px(parent.context, 5f)
                    outRect.left = Tool.dip2px(parent.context, 5f)
                    outRect.right = Tool.dip2px(parent.context, 5f)
                }
            })
        }
    }
}