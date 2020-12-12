package com.asynctaskcoffee.cardstack

import android.view.View

abstract class CardContainerAdapter {

    abstract fun getItem(position: Int): Any
    abstract fun getView(position: Int): View
    abstract fun getCount(): Int

    var dataListener: DataListener? = null
    var actionListener: ActionListener? = null

    fun swipeRight() = actionListener?.swipeRight()
    fun swipeLeft() = actionListener?.swipeLeft()


    fun notifyAppendData() {
        dataListener?.notifyAppendData()
    }

    interface ActionListener {
        fun swipeRight()
        fun swipeLeft()
    }

    interface DataListener {
        fun notifyAppendData()
    }
}
