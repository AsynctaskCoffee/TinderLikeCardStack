package com.asynctaskcoffee.tinderlikecardstack.lib

import android.view.View

abstract class CardContainerAdapter {

    abstract fun getItem(position: Int): Any
    abstract fun getView(position: Int): View
    abstract fun getCount(): Int

    var dataListener: DataListener? = null
    var actionListener: ActionListener? = null

    fun swipeRight() = actionListener?.swipeRight()
    fun swipeLeft() = actionListener?.swipeLeft()


    fun notifyDataSetChanged() {
        dataListener?.notifyDataSetChanged()
    }

    interface ActionListener {
        fun swipeRight()
        fun swipeLeft()
    }

    interface DataListener {
        fun notifyDataSetChanged()
    }
}
