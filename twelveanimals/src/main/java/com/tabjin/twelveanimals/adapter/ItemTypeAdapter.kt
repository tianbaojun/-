package com.tabjin.twelveanimals.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tabjin.twelveanimals.R
import com.tabjin.twelveanimals.bean.ItemType

class ItemTypeAdapter: RecyclerView.Adapter<ItemTypeAdapter.Holder>() {
    var data:ArrayList<ItemType> = ArrayList()
    var click: Click? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var v = View.inflate(parent.context, R.layout.item_type,null)
        return Holder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var bean = data[position]
        val context = holder.name.context
        if(bean.selected){
            holder.name.setBackgroundResource(R.drawable.bg_check)
            holder.name.setTextColor(Color.WHITE)
        }else{
            holder.name.setBackgroundResource(R.drawable.bg)
            holder.name.setTextColor(Color.BLACK)
        }
        holder.name.text = bean.name
        holder.name.setOnClickListener {
            bean.selected = !bean.selected
            if(bean.selected){
                holder.name.setBackgroundResource(R.drawable.bg_check)
                holder.name.setTextColor(Color.WHITE)
            }else{
                holder.name.setBackgroundResource(R.drawable.bg)
                holder.name.setTextColor(Color.BLACK)
            }
            click?.onclick(bean)
        }
        if(bean.visiable) {
            holder.name.visibility = View.VISIBLE
        }else{
            holder.name.visibility = View.GONE
        }
    }

    class Holder(view: View): RecyclerView.ViewHolder(view) {
        var name:TextView = view.findViewById(R.id.tv_name)
    }

    interface Click{
        fun onclick(item:ItemType)
    }
}