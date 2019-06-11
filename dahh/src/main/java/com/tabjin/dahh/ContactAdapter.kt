package com.tabjin.dahh

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.tabjin.dahh.bean.Contact
import kotlinx.android.synthetic.main.item_contact.*

class ContactAdapter: RecyclerView.Adapter<ContactAdapter.Holder> {

    lateinit var context:Context
    lateinit var data:List<Contact>

    constructor(context: Context, data:List<Contact>){
        this.context = context
        this.data = data
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        val view = View.inflate(context,R.layout.item_contact,null)
        return Holder(view)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        var contact = data.get(p1)
        p0.tvName.text = contact.contactName
        p0.tvPhone.text = contact.contactPhone
        p0.checkBox.isChecked = contact.checkstate
        p0.tvState.text = contact.sendsuccess
        p0.checkBox.setOnCheckedChangeListener{ _, isChecked -> contact.checkstate = isChecked }
    }

    inner class Holder(v: View): RecyclerView.ViewHolder(v) {
        var tvName = v.findViewById<TextView>(R.id.tv_name)
        var tvPhone = v.findViewById<TextView>(R.id.tv_phone)
        var checkBox = v.findViewById<CheckBox>(R.id.checkbox)
        var tvState = v.findViewById<TextView>(R.id.tv_send_state)
    }
}