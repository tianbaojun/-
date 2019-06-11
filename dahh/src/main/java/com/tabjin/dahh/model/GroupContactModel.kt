package com.tabjin.dahh.model

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import com.tabjin.dahh.bean.Contact



open class GroupContactModel :IContact {

    override fun getContact(context: Context,data:String): ArrayList<Contact> {

        val list : ArrayList<Contact> = ArrayList<Contact>()
        val groups:List<Contact> = getAllGroupInfo(context)
        val iterator = groups.iterator()
        while(iterator.hasNext()){
            val contact = iterator.next()
            if(TextUtils.equals(contact.contactName,data)){
                list.addAll(getAllContactsByGroupId(context,contact.contactGroupId))
                break
            }
        }
        return list
    }

    fun getAllGroupInfo(context: Context): List<Contact> {

        val groupList = ArrayList<Contact>()

        var cursor: Cursor? = null

        try {
            cursor = context.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, null, null, null)

            while (cursor!!.moveToNext()) {

                var ge: Contact? = Contact()

                val groupId = cursor!!.getInt(cursor!!.getColumnIndex(ContactsContract.Groups._ID)) // 组id
                val groupName = cursor!!.getString(cursor!!
                        .getColumnIndex(ContactsContract.Groups.TITLE)) // 组名

                ge!!.contactGroupId = groupId
                ge!!.contactGroupName = groupName

                Log.i("MainActivity", "group id:" + groupId + ">>groupName:"
                        + groupName)

                groupList.add(ge)
            }

            return groupList

        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
    }


    fun getAllContactsByGroupId(context:Context,groupId: Int): List<Contact> {

        val RAW_PROJECTION = arrayOf(ContactsContract.Data.RAW_CONTACT_ID)

        val RAW_CONTACTS_WHERE = (ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
                + "=?"
                + " and "
                + ContactsContract.Data.MIMETYPE
                + "="
                + "'"
                + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE
                + "'")

        // 通过分组的id 查询得到RAW_CONTACT_ID
        val cursor = context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI, RAW_PROJECTION,
                RAW_CONTACTS_WHERE, arrayOf(groupId.toString() + ""), "data1 asc")

        val contactList = ArrayList<Contact>()

        while (cursor.moveToNext()) {
            // RAW_CONTACT_ID
            val col = cursor.getColumnIndex("raw_contact_id")
            val raw_contact_id = cursor.getInt(col)

            // Log.i("getAllContactsByGroupId", "raw_contact_id:" +
            // raw_contact_id);

            var ce: Contact? = Contact()

            ce!!.contactId = raw_contact_id

            val dataUri = Uri.parse("content://com.android.contacts/data")
            val dataCursor = context.getContentResolver().query(dataUri, null, "raw_contact_id=?",
                    arrayOf<String>(""+raw_contact_id), null)

            while (dataCursor.moveToNext()) {
                val data1 = dataCursor.getString(dataCursor
                        .getColumnIndex("data1"))
                val mime = dataCursor.getString(dataCursor
                        .getColumnIndex("mimetype"))

                if ("vnd.android.cursor.item/phone_v2" == mime) {
                    ce!!.contactPhone = (data1)
                } else if ("vnd.android.cursor.item/name" == mime) {
                    ce!!.contactName = (data1)
                }
            }

            dataCursor.close()
            contactList.add(ce)
        }

        cursor.close()

        return contactList
    }

}