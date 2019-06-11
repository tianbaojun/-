package com.tabjin.dahh

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.tabjin.dahh.bean.Contact
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.Telephony
import android.support.v7.app.AlertDialog
import com.tabjin.dahh.model.*


class MainActivity : AppCompatActivity(),View.OnClickListener {

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_send->{
                var num = 0
                for(i in contacts.indices){
                    if(contacts[i].checkstate){
                        num++
                    }
                }
                AlertDialog.Builder(this@MainActivity).setTitle("请确认收件人数")
                            .setMessage("共$num 位收件人，确认发送？")
                            .setCancelable(true)
                            .setPositiveButton("发送") { dialog, which ->
                                dialog.dismiss()
                                tv_stop.visibility = View.VISIBLE
                                tv_send.visibility = View.GONE
                                progressbar.visibility = View.VISIBLE
                                content = et_input.text.toString()
                                startSend()
                            }.setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                            .show()
            }
            R.id.tv_stop->{
                tv_stop.visibility = View.GONE
                tv_send.visibility = View.VISIBLE
                progressbar.visibility = View.INVISIBLE
            }
        }
    }

    val SENT_SMS_ACTION = "SENT_SMS_ACTION"
    val DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION"
    val TAG = "sendsms"

    var stop = false

    var contacts:ArrayList<Contact> = ArrayList<Contact>()
    var mAdapter:ContactAdapter? = null
    var contactModel:IContact? = null
    var contentCombain: IContent? = null

    var content = ""

    var sendingIndex = -1

    var receiver = object : BroadcastReceiver() {
        override fun onReceive(_context: Context, _intent: Intent) {
            val index = sendingIndex
            if (index <= -1 || index >= contacts.size) {
                return
            }
            Log.e(TAG,"action=${_intent.action}   index = $index")
            when (_intent.action) {
                SENT_SMS_ACTION -> {
                    contacts[index].sentNum++
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            contacts[index].sentSuccess++
                        }
                        else->{
                            contacts[index].sentFaild++
                        }
                    }

                    if(contacts[index].sentNum == contacts[index].perNum){
                        val nextIndex = nextPosition(contacts,index)
                        //所有拆分短信发送动作完成，进行下一个短信
                        if(nextIndex>-1&&nextIndex<contacts.size){
                            //判断有没有暂停
                            if(progressbar.visibility == View.VISIBLE) {
                                Log.e(TAG,"addTask sendSMS $nextIndex")
                                SMSsendModel.instance.submitTask(this@MainActivity,
                                        contentCombain?.combianContent(content, contacts[nextIndex])!!, nextIndex, contacts)
                                tv_send_name.text = contacts[nextIndex].contactName
                                sendingIndex = nextIndex
                            }
                        }else if(nextIndex == -1){    //所有任务发送完成，
                            progressbar.visibility = View.INVISIBLE
                            tv_send_name.text = "任务完成"
                            tv_stop.visibility= View.GONE
                            tv_send.visibility = View.VISIBLE
                        }
                        //所有拆分后的短信都发送成功，插入短信数据库
                        if(contacts[index].sentSuccess == contacts[index].perNum){
                            Log.e(TAG,"${contacts[index].contactName} 发送完成并成功")
                            ThreadHelper.instance.addInsertTask(this@MainActivity,
                                    contacts[index].contactPhone,
                                    contentCombain?.combianContent(content, contacts[index])!!,
                                    2)
                            contacts[index].sendsuccess = "发送成功"
                            mAdapter?.notifyDataSetChanged()
                        }else if(contacts[index].sentFaild == contacts[index].perNum){
                            contacts[index].sendsuccess = "发送失败"
                            mAdapter?.notifyDataSetChanged()
                        }else{
                            contacts[index].sendsuccess = "部分发送失败"
                            mAdapter?.notifyDataSetChanged()
                        }
                    }
                }
                DELIVERED_SMS_ACTION->{
                    if(index>-1&&index<contacts.size){
                        contacts[index].delevedsuccess = true
                    }
                }
            }

        }
    }

    /**
     * 找到下一个需要发送的联系人
     *
     */
    fun nextPosition(contacts:List<Contact>,index:Int):Int{
        if(index<-1||index>contacts.size-2){
            return -1
        }
        for(i in index+1 until contacts.size){
            if(contacts[i].checkstate){
                return i
            }
        }
        return -1
    }

    var sentNum = 0
    var defaultSmsApp = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = ContactAdapter(this,contacts)
        recyclerview.adapter = mAdapter
        recyclerview.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        var filter =  IntentFilter(SENT_SMS_ACTION)
//        filter.addAction(DELIVERED_SMS_ACTION)
        var sp = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        defaultSmsApp = sp.getString("defaultSmsApp","")!!
        if(TextUtils.isEmpty(defaultSmsApp)){
            defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this)
            //不是本应用，就是系统应用，保存下来
            if(!TextUtils.equals(defaultSmsApp,BuildConfig.APPLICATION_ID)) {
                sp.edit().putString("defaultSmsApp", defaultSmsApp).apply()
            }
        }
        if(!TextUtils.equals(defaultSmsApp,BuildConfig.APPLICATION_ID)) {
            SMSHelper.setDefault(this@MainActivity, BuildConfig.APPLICATION_ID)
        }
        // register the Broadcast Receivers
        this@MainActivity.registerReceiver(receiver, filter)

        contactModel = ContactProviderFactory.instance.getProvider(2)
        contentCombain = ContentProviderFactory.instance.getProvider(1)

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
            ||checkSelfPermission(Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED
            ||checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED) {
                val permission = arrayOf(Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE)
                requestPermissions(permission, 11)
            }
        }

        prefix_input.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(progressbar.visibility == View.INVISIBLE&&!TextUtils.isEmpty(s)){
                    getContact(s.toString())
                }else if(TextUtils.isEmpty(s)&&progressbar.visibility == View.INVISIBLE){
                    contacts.clear()
                    mAdapter?.notifyDataSetChanged()
                }
            }
        })


        tv_stop.visibility = View.GONE;
        tv_send.visibility = View.VISIBLE
        tv_stop.setOnClickListener(this)
        tv_send.setOnClickListener(this)

    }

    private fun startSend() {
        sendingIndex = nextPosition(contacts,-1)
        val msg = contentCombain?.combianContent(content,contacts[sendingIndex])!!
        SMSsendModel.instance.continueNum = 0
        SMSsendModel.instance.submitTask(this@MainActivity,msg, sendingIndex, contacts)
        tv_send_name.text = contacts[sendingIndex].contactName

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(i in grantResults){
            if(i!=0){
                finish()
                Toast.makeText(this,"请给与相应权限",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getContact(prefix:String){
        progressbar.visibility = View.VISIBLE
        ThreadHelper.instance.addTask(Runnable {
            var list: ArrayList<Contact> = contactModel?.getContact(this@MainActivity, prefix)!!
            contacts.clear()
            contacts.addAll(list)
            Log.e("size", "" + list.size)
            runOnUiThread {
                progressbar.visibility = View.INVISIBLE
                mAdapter?.notifyDataSetChanged()
            }
        })
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)

        defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this)
        if(TextUtils.equals(defaultSmsApp,BuildConfig.APPLICATION_ID)) {
            var sp = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
            var appSms = sp.getString("defaultSmsApp","")
            if(!TextUtils.isEmpty(appSms)) SMSHelper.setDefault(this, defaultSmsApp)
        }
        super.onDestroy()
    }
}