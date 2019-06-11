package com.tabjin.twelveanimals.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tabjin.twelveanimals.R
import com.tabjin.twelveanimals.adapter.TypeAdapter
import com.tabjin.twelveanimals.bean.Type
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_clear ->{
                for(bean in types){
                    for(item in bean.data){
                        if(item.selected){
                            item.selected = false
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }
            R.id.btn_start ->{
                result.clear()
                //出号集合
                var chuhaoList = ArrayList<String>()
                var chuhaoStr = "出号："
                for(a in types[0].data){
                    if(a.selected){
                        chuhaoList.add(a.name)
                        if(chuhaoStr.length == 3){
                            chuhaoStr = "${chuhaoStr}${a.name}"
                        }else{
                            chuhaoStr = "${chuhaoStr}、${a.name}"
                        }
                    }
                }
                //胆码集合
                var danmaStr = "胆码："
                var bList = ArrayList<String>()
                for(c in types[1].data){
                    if(c.selected){
                        bList.add(c.name)
                        if(danmaStr.length == 3){
                            danmaStr = "${danmaStr}${c.name}"
                        }else{
                            danmaStr = "${danmaStr}、${c.name}"
                        }
                    }
                }
                //杀码集合
                var shamaStr = "杀码："
                var killNum = ArrayList<String>()
                for(c in types[2].data){
                    if(c.selected){
                        killNum.add(c.name)
                        if(shamaStr.length == 3){
                            shamaStr = "${shamaStr}${c.name}"
                        }else{
                            shamaStr = "${shamaStr}、${c.name}"
                        }
                    }
                }
                var conditionstr = "$chuhaoStr \r\n$danmaStr\r\n$shamaStr"
                var resultNum = 0
                var resultStr = java.lang.StringBuilder()
                //生成结果
                for(a in chuhaoList){
                    val chu = HashMap<String,ArrayList<String>>()
                    result[a] = chu
                    //选中胆码对应的数字集合，分类保存去除杀码后的集合
                    var danNums = HashMap<String,ArrayList<String>>()
                    for(b in bList){
                        //copy一份生肖对应的数字并去除杀码
                        val animalNums = ArrayList<String>(animalMap[b]!!)
                        removeKill(animalNums,killNum)
                        danNums[b] = animalNums
                    }
                    if(chuhaoMap[a]!=null) {
                        var resultList = combain(danNums, chuhaoMap[a]!!)
                        resultNum += resultList.size
                        for(c in resultList){
                            if(resultStr.isEmpty()) {
                                resultStr.append(c)
                            }else{
                                resultStr.append("\r\n$c")
                            }
                        }
                    }
                }

                var intent = Intent(this@MainActivity, ResultActivity::class.java)
                intent.putExtra("resultNum",resultNum)
                intent.putExtra("condition",conditionstr)
                intent.putExtra("resultStr",resultStr.toString())
                startActivity(intent)
            }
            R.id.iv_back ->finish()
        }
    }

    fun combain(data:HashMap<String,ArrayList<String>>,num :Int):ArrayList<String>{

        val allData = ArrayList<String>()
        var iterator = data.keys.iterator()
        while(iterator.hasNext()){
            var name = iterator.next()
            if(data[name]!=null) {
                allData.addAll(data[name]!!)
            }
        }
        val list = ArrayList<String>()

        when(num){
            1->list.addAll(allData)
            2->{
                //先计算所有的
                if(allData.size>=2){
                    for(i in 0..allData.size-2){
                        for(j in i+1 until allData.size){
                            list.add("${allData[i]}  ${allData[j]}")
                        }
                    }
                }

                //除去只存在于一个胆码中的，只有一个胆码是不去除
                if(data.size > 1) {
                    val iterator = data.keys.iterator()
                    while (iterator.hasNext()) {
                        val name = iterator.next()
                        if (data[name] != null) {
                            for (i in 0..data[name]!!.size - 2) {
                                for (j in i + 1 until data[name]!!.size) {
                                    list.remove("${data[name]!![i]}  ${data[name]!![j]}")
                                }
                            }
                        }
                    }
                }
            }
            3->{
                if(allData.size>=3){
                    for(i in 0..allData.size-3){
                        for(j in i+1 until allData.size-1){
                            for(k in j+1 until allData.size) {
                                list.add("${allData[i]}  ${allData[j]}  ${allData[k]}")
                            }
                        }
                    }
                }
                if(data.size > 1) {
                    val iterator = data.keys.iterator()
                    while (iterator.hasNext()) {
                        val name = iterator.next()
                        if (data[name] != null) {
                            for (i in 0..data[name]!!.size - 3) {
                                for (j in i + 1 until data[name]!!.size - 1) {
                                    for (k in j + 1 until data[name]!!.size) {
                                        list.add("${data[name]!![i]}  ${data[name]!![j]}  ${data[name]!![k]}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            7->{

            }
        }
        return list
    }
    var qushulist = ArrayList<String>()
    fun qushu(list:ArrayList<String>,num:Int){
        for(i in 0 until list.size-num){
            if(num ==0){

                qushulist.add("")
            }
            qushulist.add("${list[i]}  ")
        }
    }


    /**
     * 去除存在的杀码
     */
    fun removeKill(data:ArrayList<String>, kills:ArrayList<String>){
        if(data.isEmpty()||kills.isEmpty()){
            return
        }
        var iterator = data.iterator()
        while(iterator.hasNext()){
            var str:String = iterator.next()
            for(kill in kills){
                if(TextUtils.equals(kill,str)){
                    iterator.remove()
                }
            }
        }
    }

    val adapter = TypeAdapter()
    val types = ArrayList<Type>()
    val ma = Type()
    val animals = Type()
    val killers = Type()
    var animalMap = HashMap<String,ArrayList<String>>()
    var chuhaoMap = HashMap<String,Int>()
    var result = HashMap<String,HashMap<String,ArrayList<String>>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recycler.addItemDecoration(object:RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                if(parent.getChildAdapterPosition(view) == 0){
                    outRect.top = dip2px(this@MainActivity,20f)
                }
            }
        })
        recycler.adapter = adapter
        recycler.addItemDecoration(object :RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = dip2px(this@MainActivity,20f)
            }
        })
        initData()
        btn_clear.setOnClickListener(this)
        btn_start.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        tv_title.text = "十二生肖"
    }

    fun initData(){
        try {
            var input = assets.open("type.json")
            val scanner = Scanner(input, "UTF-8").useDelimiter("\\A")
            var content = StringBuilder()
            while (scanner.hasNext()) {
                content.append(scanner.next())
            }
            scanner.close()
            if (content.isNotEmpty()) {
                types.addAll(Gson().fromJson<List<Type>>(content.toString(), object : TypeToken<List<Type>>() {}.type))
                adapter.data.clear()
                adapter.data.addAll(types)
                adapter.notifyDataSetChanged()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
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

        chuhaoMap["一码"] = 1
        chuhaoMap["二码"] = 2
        chuhaoMap["三码"] = 3
        chuhaoMap["七码"] = 7
    }

    fun dip2px(context: Context, dpValue:Float):Int{
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}
