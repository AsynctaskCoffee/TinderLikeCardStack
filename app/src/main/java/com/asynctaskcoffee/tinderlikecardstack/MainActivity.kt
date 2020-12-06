package com.asynctaskcoffee.tinderlikecardstack

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    lateinit var cardContainer: CardContainer
    lateinit var adapter: Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cardContainer = findViewById(R.id.cardContainer)

        val list: ArrayList<String> = arrayListOf()
        for (i in 0..5) list.add("https://picsum.photos/id/$i/200/300")

        adapter = Adapter(list, this)

        cardContainer.setEmptyView(generateEmptyView())
        cardContainer.setAdapter(adapter)
    }

    class Adapter(private val list: ArrayList<String>, context: Context) :
        CardContainerAdapter() {

        var layoutInflater: LayoutInflater = LayoutInflater.from(context)

        override fun getItem(position: Int): String = list[position]

        @SuppressLint("InflateParams")
        override fun getView(position: Int): View {
            val v = layoutInflater.inflate(R.layout.card_view, null)
            val imageView = v.findViewById<ImageView>(R.id.imageView)
            Picasso.get().load(getItem(position)).into(imageView)
            return v
        }

        override fun getCount(): Int = list.size
    }

    fun generateEmptyView(): View {
        return LayoutInflater.from(this).inflate(R.layout.empty_layout, null)
    }
}