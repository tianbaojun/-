package com.tabjin.twelveanimals.adapter

import com.tabjin.twelveanimals.bean.ItemType

class Type2Adapter : TypeAdapter() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        super.onBindViewHolder(holder, position)
        var adapter: ItemTypeAdapter = holder.recycler.adapter as ItemTypeAdapter

            adapter.click = object : ItemTypeAdapter.Click {
                override fun onclick(item: ItemType) {
                    if(position !=0) {
                        return
                    }
                    var map = HashMap<String, Int>()
                    map["一码"] = 2
                    map["二码"] = 3
                    map["三码"] = 4
                    map["四码"] = 5
                    map["五码"] = 6
                    map["六码"] = 7
                    map["七码"] = 8
                    var maxName = 0
                    for (a in data[0].data) {
                        if(a == item){
                            maxName = if(a.selected) {
                                map[a.name]!!
                            }else{
                                0
                            }
                        }else{
                            a.selected = false
                        }
                    }
                    if (maxName < 2) {
                        for (i in maxName until data[2].data.size) {
                            data[2].data[i].visiable = true
                            data[2].data[i].selected = false
                        }
                    } else {
                        for (i in maxName until data[2].data.size) {
                            data[2].data[i].visiable = false
                            data[2].data[i].selected = false
                        }
                        for (i in 0 until maxName) {
                            data[2].data[i].visiable = true
                            data[2].data[i].selected = false
                        }
                    }
                    notifyDataSetChanged()
                }
            }
    }
}