package com.tabjin.shorttext;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button sendBtn;

    private  PendingIntent deliverPI;
    private  PendingIntent sentPI;
    private  String defaultSmsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED) {
                testGetAllContact();
            }else{
                String[] permission = {
                        Manifest.permission.READ_CONTACTS
                };
                requestPermissions(permission,11);
            }
        }else{
            testGetAllContact();
        }
        defaultSmsApp =  Telephony.Sms.getDefaultSmsPackage(this);
        sendBtn = findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(Build.VERSION.SDK_INT>=23){
                    if(checkSelfPermission(Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED) {
                        sendSMS("dsafdfdfdsfdsafdsafdsafdsafdsafdsa","15918522240");
                    }else{
                        String[] permission = {
                                Manifest.permission.SEND_SMS
                        };
                        requestPermissions(permission,22);
                    }
                }else{
                    sendSMS("dsafdfdfdsfdsafdsafdsafdsafdsafdsa","15918522240");
                }*/
                setDefault(BuildConfig.APPLICATION_ID);
                TestInsertSMS();
            }
        });

        //处理返回的发送状态
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        sentPI= PendingIntent.getBroadcast(this, 0, sentIntent,
                0);
        // register the Broadcast Receivers
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,
                                "短信发送成功", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));

        //处理返回的接收状态
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        deliverPI = PendingIntent.getBroadcast(this, 0,
                deliverIntent, 0);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                Toast.makeText(MainActivity.this,
                        "收信人已经成功接收", Toast.LENGTH_SHORT)
                        .show();
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));
    }

    private void setDefault(String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent smsIntent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            smsIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,name);
            startActivity(smsIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&permissions[0].equals(Manifest.permission.READ_CONTACTS)){
            testGetAllContact();
        }
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&permissions[0].equals(Manifest.permission.SEND_SMS)){
            sendSMS("dsafdfdfdsfdsafdsafdsafdsafdsafdsa","15918522240");
        }
    }

    public void sendSMS(String content,String phone){
        SmsManager smsManager = SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(content);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phone, null, text, sentPI,
                    deliverPI);
        }


    }

    /**
     * 读取联系人
     */
    public void testGetAllContact()
    {
        //获取联系人信息的Uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //获取ContentResolver
        ContentResolver contentResolver = this.getContentResolver();
        //查询数据，返回Cursor
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        while(cursor!=null&&cursor.moveToNext())
        {
            Map<String,Object> map = new HashMap<String,Object>();
            StringBuilder sb = new StringBuilder();
            //获取联系人的ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //构造联系人信息
            sb.append("contactId=").append(contactId).append(",Name=").append(name);
            map.put("name", name);
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));//联系人ID


            //查询电话类型的数据操作
            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
                    null, null);
            while(phones!=null&&phones.moveToNext())
            {
                String phoneNumber = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                //添加Phone的信息
                sb.append(",Phone=").append(phoneNumber);
                map.put("mobile", phoneNumber);
            }
            phones.close();


            //查询Email类型的数据操作
         /*   Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                    null, null);
            while (emails.moveToNext())
            {
                String emailAddress = emails.getString(emails.getColumnIndex(
                        ContactsContract.CommonDataKinds.Email.DATA));
                //添加Email的信息
                sb.append(",Email=").append(emailAddress);
                Log.e("emailAddress", emailAddress);
                map.put("email", emailAddress);


            }
            emails.close();*/
            //Log.i("=========ddddddddddd=====", sb.toString());

            //查询==地址==类型的数据操作.StructuredPostal.TYPE_WORK
            /*Cursor address = contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                    null, null);
            while (address.moveToNext())
            {
                String workAddress = address.getString(address.getColumnIndex(
                        ContactsContract.CommonDataKinds.StructuredPostal.DATA));


                //添加Email的信息
                sb.append(",address").append(workAddress);
                map.put("address", workAddress);
            }
            address.close();*/
            //Log.i("=========ddddddddddd=====", sb.toString());

            //查询==公司名字==类型的数据操作.Organization.COMPANY  ContactsContract.Data.CONTENT_URI
           /* String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] orgWhereParams = new String[]{id,
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            Cursor orgCur = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                    null, orgWhere, orgWhereParams, null);
            if (orgCur.moveToFirst()) {
                //组织名 (公司名字)
                String company = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                //职位
                String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                sb.append(",company").append(company);
                sb.append(",title").append(title);
                map.put("company", company);
                map.put("title", title);
            }
            orgCur.close();*/
            list.add(map);
            Log.i("=========orgName=====", sb.toString());//查看所有的数据
            Log.e("=========map=====", map.toString());//有很多数据的时候，只会添加一条  例如邮箱，
        }

        Log.i("=========list=====", list.toString());//
        cursor.close();
    }

    private void TestInsertSMS() {
        final String ADDRESS = "address";
        final String DATE = "date";
        final String READ = "read";
        final String STATUS = "status";
        final String TYPE = "type";
        final String BODY = "body";
        int MESSAGE_TYPE_INBOX = 1;
        int MESSAGE_TYPE_SENT = 2;
        ContentValues values = new ContentValues();
        /* 手机号 */
        values.put(ADDRESS, "17681823314");
        /* 时间 */
        values.put(DATE, System.currentTimeMillis());
        values.put(READ, 1);
        values.put(STATUS, -1);
        /* 类型1为收件箱，2为发件箱 */
        values.put(TYPE, 2);
        /* 短信体内容 */
        values.put(BODY, "测试插入一条短信");
        /* 插入数据库操作 */
        Uri inserted = getContentResolver().insert(Uri.parse("content://sms"),
                values);
        setDefault(defaultSmsApp);
    }
}
