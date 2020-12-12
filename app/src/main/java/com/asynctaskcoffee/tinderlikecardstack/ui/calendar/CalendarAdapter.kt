package com.asynctaskcoffee.tinderlikecardstack.ui.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.asynctaskcoffee.cardstack.CardContainerAdapter
import com.asynctaskcoffee.tinderlikecardstack.R

class CalendarAdapter(var context: Context, var days: List<CalendarModel>) :
    CardContainerAdapter() {
    override fun getItem(position: Int): CalendarModel = days[position]

    override fun getView(position: Int): View {
        val v = LayoutInflater.from(context).inflate(R.layout.item_calendar, null)
        val day = v.findViewById<TextView>(R.id.day)
        val month = v.findViewById<TextView>(R.id.month)
        val dayName = v.findViewById<TextView>(R.id.dayName)

        day.text = getItem(position).day.toString()
        month.text = getItem(position).month
        dayName.text = getItem(position).dayName

        return v
    }

    override fun getCount() = days.size
}