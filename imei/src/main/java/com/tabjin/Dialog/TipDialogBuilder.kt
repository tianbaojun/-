package com.tabjin.Dialog

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import android.view.View
import com.tabjin.imei.R

open class TipDialogBuilder : AlertDialog.Builder{
    var mCtn:Context
    var mDialog:AlertDialog
    var view:View
    constructor(context: Context,@LayoutRes layoutId:Int):this(context,layoutId,R.style.appalertdialog){
        mCtn = context
    }
    constructor(context:Context,@LayoutRes layoutId:Int,@StyleRes resId:Int):super(context,resId){
        mCtn = context
        view = View.inflate(mCtn, layoutId,null)
        mDialog = this.create()
    }

    override fun show():AlertDialog {
        mDialog = super.show()
        mDialog.setContentView(view)
        return mDialog
    }

    open fun setOnSureClickListener(@IdRes id:Int,listener:OnClickListener):TipDialogBuilder{
        view.findViewById<View>(id)?.setOnClickListener {
            listener.onClick(it,mDialog)
        }
        return this
    }

    interface OnClickListener{
        fun onClick(view:View,dialog:AlertDialog)
    }


}