package com.tabjin.twelveanimals.model

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Constant {

    companion object{
        var animalMap = HashMap<String,ArrayList<String>>()
        var allList = ArrayList<String>()
        var allAnimals = ArrayList<String>()
        init {
            animalMap["鼠"] = ArrayList(Arrays.asList("12","24","36","48"))
            animalMap["牛"] = ArrayList(Arrays.asList("11","23","35","47"))
            animalMap["虎"] = ArrayList(Arrays.asList("10","22","34","46"))
            animalMap["兔"] = ArrayList(Arrays.asList("09","21","33","45"))
            animalMap["龙"] = ArrayList(Arrays.asList("08","20","32","44"))
            animalMap["蛇"] = ArrayList(Arrays.asList("07","19","31","43"))
            animalMap["马"] = ArrayList(Arrays.asList("06","18","30","42"))
            animalMap["羊"] = ArrayList(Arrays.asList("05","17","29","41"))
            animalMap["猴"] = ArrayList(Arrays.asList("04","16","28","40"))
            animalMap["鸡"] = ArrayList(Arrays.asList("03","15","27","39"))
            animalMap["狗"] = ArrayList(Arrays.asList("02","14","26","38"))
            animalMap["猪"] = ArrayList(Arrays.asList("01","13","25","37","49"))

            allList.addAll(Arrays.asList("01","02","03","04","05","06","07",
                    "08","09","10","11","12","13","14",
                    "15","16","17","18","19","20","21",
                    "22","23","24","25","26","27","28",
                    "29","30","31","32","33","34","35",
                    "36","37","38","39","40","41","42",
                    "43","44","45","46","47","48","49"))

            allAnimals.addAll(Arrays.asList("鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪"))
        }
    }

}