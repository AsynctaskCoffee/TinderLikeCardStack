package com.asynctaskcoffee.tinderlikecardstack

import android.view.View

abstract class CardContainerAdapter {
    abstract fun getItem(position: Int): Any
    abstract fun getView(position: Int): View
    abstract fun getCount(): Int
    var dataListener: DataListener? = null

    fun notifyDataSetChanged() {
        dataListener?.notifyDataSetChanged()
    }

    interface DataListener {
        fun notifyDataSetChanged()
    }
}
