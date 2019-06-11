package com.tabjin.dahh.model

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.tabjin.dahh.bean.Contact
import java.util.ArrayList
import java.util.HashMap

class PrefixContactModel:IContact{
    override fun getContact(context: Context, prefix: String): ArrayList<Contact> {
        return getAllContact(context,prefix)
    }

    /**
     * 读取联系人
     */
    fun getAllContact(context: Context,prefix:String):ArrayList<Contact> {
        //获取联系人信息的Uri
        val uri = ContactsContract.Contacts.CONTENT_URI
        //获取ContentResolver
        val contentResolver = context.getContentResolver()
        //查询数据，返回Cursor
        val cursor = contentResolver.query(uri, null, null, null, null)
        val list = ArrayList<Contact>()
        while (cursor != null && cursor!!.moveToNext()) {
            val contact = Contact()
            val sb = StringBuilder()
            //获取联系人的ID
            val contactId = cursor!!.getString(cursor!!.getColumnIndex(ContactsContract.Contacts._ID))
            //获取联系人的姓名
            val name = cursor!!.getString(cursor!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            //构造联系人信息
            sb.append("contactId=").append(contactId).append(",Name=").append(name)
            contact.contactName = name
            if(!name.startsWith(prefix)){
                continue
            }
            val id = cursor!!.getString(cursor!!.getColumnIndex(ContactsContract.Contacts._ID))//联系人ID


            //查询电话类型的数据操作
            val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null)
            while (phones != null && phones!!.moveToNext()) {
                val phoneNumber = phones!!.getString(phones!!.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER))
                //添加Phone的信息
                sb.append(",Phone=").append(phoneNumber)
                contact.contactPhone = phoneNumber
            }
            phones!!.close()


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

            list.add(contact)
            Log.i("=========orgName=====", sb.toString())//查看所有的数据
            Log.e("=========map=====", contact.contactName+contact.contactPhone)//有很多数据的时候，只会添加一条  例如邮箱，
        }

        Log.i("=========list=====", list.toString())//
        cursor!!.close()
        return list
    }

}