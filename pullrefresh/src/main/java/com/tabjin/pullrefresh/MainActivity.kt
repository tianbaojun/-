package com.tabjin.pullrefresh

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listview.adapter = Myadapter()
    }

    class Myadapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var c = convertView
            if (convertView == null) {
                c = View.inflate(parent?.context, R.layout.text, null)
            }
            c?.findViewById<TextView>(R.id.textView)?.text = "$position"
            return c!!
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 50
        }

    }
}
