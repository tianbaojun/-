package com.tabjin.twelveanimals.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tabjin.twelveanimals.R
import com.tabjin.twelveanimals.adapter.Type2Adapter
import com.tabjin.twelveanimals.bean.Type
import com.tabjin.twelveanimals.model.CompainModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity2 : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back ->finish()
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
                var chuhaoStr = "任几："
                for(a in types[0].data){
                    if(a.selected){
                        chuhaoList.add(a.name)
                        chuhaoStr = "${chuhaoStr}${a.name}"
                        break
                    }
                }
                //出号集合
                var danmaStr = "出号数："
                var bList = ArrayList<String>()
                //由于出好数与出码数相差大于2，会产生超过一万条数据
                var chuMin = 7
                for(c in types[2].data){
                    if(c.selected){
                        bList.add(c.name)
                        if(c.name.toInt()<chuMin){
                            chuMin = c.name.toInt()
                        }
                        if(danmaStr.length == 4){
                            danmaStr = "${danmaStr}${c.name}"
                        }else{
                            danmaStr = "${danmaStr}、${c.name}"
                        }
                    }
                }
                if(chuhaoMap[chuhaoList[0]]!=null&&chuhaoMap[chuhaoList[0]]!!-chuMin>2){
                    Toast.makeText(this@MainActivity2,"结果大于一万条，无法完成计算！请更改组合！",Toast.LENGTH_SHORT).show()
                    return
                }
                //杀码集合
                var shamaStr = "胆码："
                var killNum = ArrayList<String>()
                var unkillNum = ArrayList<String>()
                for(c in types[1].data){
                    if(c.selected){
                        killNum.add(c.name)
                        if(shamaStr.length == 3){
                            shamaStr = "${shamaStr}${c.name}"
                        }else{
                            shamaStr = "${shamaStr}、${c.name}"
                        }
                    }else{
                        unkillNum.add(c.name)
                    }
                }
                //出号数的最大值
                var maxMustNum = 0
                for(b in bList){
                    if(b.toInt()>maxMustNum){
                        maxMustNum = b.toInt()
                    }
                }
                if(chuhaoList.isEmpty()){
                    Toast.makeText(this@MainActivity2,"请选择任几",Toast.LENGTH_SHORT).show()
                    return
                }
                //判断是否符合条件  任几必须要大于必选数的最大数
                var renji = chuhaoMap[chuhaoList[0]]
                if(renji!!<maxMustNum){
                    Toast.makeText(this@MainActivity2,"出号数不能大于任几数",Toast.LENGTH_SHORT).show()
                    return
                }
                if(killNum.size<maxMustNum){
                    Toast.makeText(this@MainActivity2,"出号数不能大于胆码数",Toast.LENGTH_SHORT).show()
                    return
                }

                var conditionstr = "$chuhaoStr \r\n$danmaStr\r\n$shamaStr"
                var resultNum = 0
                var resultStr = java.lang.StringBuilder()
                var resultList = ArrayList<String>()
                //生成结果
                for(b in bList) {

                    resultList.addAll(CompainModel.compin(killNum, unkillNum, renji, b.toInt()))
                }

                resultNum = resultList.size
                for(a in resultList){
                    resultStr.append("$a\r\n")
                }
                var intent = Intent(this@MainActivity2, ResultActivity::class.java)
                intent.putExtra("resultNum",resultNum)
                intent.putExtra("condition",conditionstr)
                intent.putExtra("resultStr",resultStr.toString())
                startActivity(intent)
            }
        }
    }

    val adapter = Type2Adapter()
    val types = ArrayList<Type>()
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
                    outRect.top = dip2px(this@MainActivity2,20f)
                }
            }
        })
        recycler.adapter = adapter
        recycler.addItemDecoration(object :RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = dip2px(this@MainActivity2,20f)
            }
        })
        initData()
        btn_clear.setOnClickListener(this)
        btn_start.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        tv_title.text = "欢乐七彩"
    }

    fun initData(){
        try {
            var input = assets.open("type2.json")
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

        chuhaoMap["一码"] = 1
        chuhaoMap["二码"] = 2
        chuhaoMap["三码"] = 3
        chuhaoMap["四码"] = 4
        chuhaoMap["五码"] = 5
        chuhaoMap["六码"] = 6
        chuhaoMap["七码"] = 7
    }

    fun dip2px(context: Context, dpValue:Float):Int{
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}
