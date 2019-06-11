package com.tabjin.twelveanimals.adapter

import com.tabjin.twelveanimals.bean.ItemType

class Type3Adapter : TypeAdapter() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        super.onBindViewHolder(holder, position)
        var adapter: ItemTypeAdapter = holder.recycler.adapter as ItemTypeAdapter

            adapter.click = object : ItemTypeAdapter.Click {
                override fun onclick(item: ItemType) {
                    if(position !=0) {
                        return
                    }
                    for(a in data[0].data){
                        a.selected = false
                    }
                    item.selected = true
                    notifyDataSetChanged()
                }
            }
    }
}