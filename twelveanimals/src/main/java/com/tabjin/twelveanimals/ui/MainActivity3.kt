package com.tabjin.twelveanimals.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tabjin.twelveanimals.R
import com.tabjin.twelveanimals.adapter.Type3Adapter
import com.tabjin.twelveanimals.bean.Type
import com.tabjin.twelveanimals.model.CompainModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity3 : AppCompatActivity(), View.OnClickListener {
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
                var condition = java.lang.StringBuilder("胆码1：")

                var chuList1 = ArrayList<Int>()
                var dan1 = ArrayList<String>()
                for(c in types[1].data){
                    if(c.selected){
                        dan1.add(c.name)
                        condition.append(c.name).append("  ")

                    }
                }
                condition.append("\n出码1：")
                for(c in types[2].data){
                    if(c.selected){
                        chuList1.add(c.name.toInt())
                        condition.append(c.name).append("  ")
                    }
                }
                var chuList2 = ArrayList<Int>()
                var dan2 = ArrayList<String>()
                condition.append("\n胆码2：")
                for(c in types[3].data){
                    if(c.selected){
                        dan2.add(c.name)
                        condition.append(c.name).append("  ")
                    }
                }
                condition.append("\n出码2：")
                for(c in types[4].data){
                    if(c.selected){
                        chuList2.add(c.name.toInt())
                        condition.append(c.name).append("  ")
                    }
                }
                var chuList3 = ArrayList<Int>()
                var dan3 = ArrayList<String>()
                condition.append("\n胆码3：")
                for(c in types[5].data){
                    if(c.selected){
                        dan3.add(c.name)
                        condition.append(c.name).append("  ")
                    }
                }
                condition.append("\n出码3：")
                for(c in types[6].data){
                    if(c.selected){
                        chuList3.add(c.name.toInt())
                        condition.append(c.name).append("  ")
                    }
                }
                val sha = ArrayList<String>()
                condition.append("\n杀码：")
                for(c in types[7].data){
                    if(c.selected){
                        sha.add(c.name)
                        condition.append(c.name).append("  ")
                    }
                }

                //所有码集合
                val all = ArrayList<String>()
                for(c in types[1].data){
                    all.add(c.name)
                }
                if(chuhaoList.isEmpty()){
                    Toast.makeText(this@MainActivity3,"请选择任几",Toast.LENGTH_SHORT).show()
                    return
                }
                //判断是否符合条件  任几必须要大于出码数的最大数
                val renji = chuhaoMap[chuhaoList[0]]
                val chu = ArrayList<Int>(chuList1)
                chu.addAll(chuList2)
                chu.addAll(chuList3)
                for(a in chu) {
                    if (renji!! < a) {
                        Toast.makeText(this@MainActivity3, "出码数不能大于任几数", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                if(chuList1.isEmpty()){
                    chuList1.add(0)
                }
                if(chuList2.isEmpty()){
                    chuList2.add(0)
                }
                if(chuList3.isEmpty()){
                    chuList3.add(0)
                }
                var resultList = ArrayList<String>()
                //开始计算
                showPD()
                Thread{
                    for(a in chuList1){
                        for(b in chuList2){
                            for(c in chuList3){
                                resultList.addAll(CompainModel.compainDan3Sha1(all,dan1,a,dan2,b,dan3,c,sha,renji!!))
                            }
                        }
                    }
                    var conditionstr = condition.toString()
                    var resultNum = 0
                    var resultStr = java.lang.StringBuilder()

                    resultNum = resultList.size
                    for(a in resultList){
                        resultStr.append("$a\n")
                    }
                    runOnUiThread{
                        dismissPD()
                        val intent = Intent(this@MainActivity3, ResultActivity::class.java)
                        intent.putExtra("resultNum",resultNum)
                        intent.putExtra("condition",conditionstr)
                        intent.putExtra("resultStr",resultStr.toString())
                        startActivity(intent)
                    }
                }.start()
            }
        }
    }

    val adapter = Type3Adapter()
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
                    outRect.top = dip2px(this@MainActivity3,20f)
                }
            }
        })
        recycler.adapter = adapter
        recycler.addItemDecoration(object :RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = dip2px(this@MainActivity3,20f)
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
            var input = assets.open("type3.json")
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

    private var dialog:ProgressDialog?=null
    private fun showPD() {
        Log.e("dialog","show")
        if(dialog == null) {
            dialog = ProgressDialog(this)
        }
        dialog?.setProgressStyle(0)//转盘
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }
    private fun dismissPD() {
        if(dialog == null) {
            return
        }

        Log.e("dialog","dismiss")
        dialog?.dismiss()
    }

}
