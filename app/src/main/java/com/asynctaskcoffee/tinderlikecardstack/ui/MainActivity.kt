package com.asynctaskcoffee.tinderlikecardstack.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.asynctaskcoffee.tinderlikecardstack.R
import com.asynctaskcoffee.tinderlikecardstack.lib.CardContainer
import com.asynctaskcoffee.tinderlikecardstack.lib.CardListener
import com.asynctaskcoffee.tinderlikecardstack.lib.pulse
import com.asynctaskcoffee.tinderlikecardstack.lib.px

class MainActivity : AppCompatActivity(), CardListener {

    lateinit var cardContainer: CardContainer
    lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cardContainer = findViewById(R.id.cardContainer)

        adapter = MainAdapter(generateTestData(), this)

        cardContainer.setOnCardActionListener(this)

        /*Customization*/
        cardContainer.maxStackSize = 3
        cardContainer.marginTop = 13.px
        cardContainer.margin = 20.px

        /*Adding Extra Layouts*/
        cardContainer.setEmptyView(generateEmptyView())
        cardContainer.addFooterView(generateFooterView())
        cardContainer.addHeaderView(generateHeaderView())

        cardContainer.setAdapter(adapter)
    }

    override fun onLeftSwipe(position: Int, model: Any) {
        Log.e(
            "SwipeLog",
            "onLeftSwipe pos: $position model: " + (model as MainTestModel).toString()
        )
    }

    override fun onRightSwipe(position: Int, model: Any) {
        Log.e(
            "SwipeLog",
            "onRightSwipe pos: $position model: " + (model as MainTestModel).toString()
        )
    }

    override fun onItemShow(position: Int, model: Any) {
        Log.e("SwipeLog", "onItemShow pos: $position model: " + (model as MainTestModel).toString())
    }

    override fun onSwipeCancel(position: Int, model: Any) {
        Log.e(
            "SwipeLog",
            "onSwipeCancel pos: $position model: " + (model as MainTestModel).toString()
        )
    }

    override fun onSwipeCompleted() {
        Log.e(
            "SwipeLog",
            "Out of swipe data"
        )
    }

    private fun generateTestData(): ArrayList<MainTestModel> {
        val list: ArrayList<MainTestModel> = arrayListOf()
        for (i in 0..25) list.add(
            MainTestModel(
                "userName$i",
                "https://picsum.photos/id/$i/200/300",
                if (i % 2 == 0) R.drawable.icons_2_dp_female_2_dp else R.drawable.icons_2_dp_male_2_dp,
                "25 - Last seen 5m ago"
            )
        )
        return list
    }

    private fun generateEmptyView(): View {
        return LayoutInflater.from(this).inflate(R.layout.empty_layout, null)
    }

    private fun generateHeaderView(): View {
        return LayoutInflater.from(this).inflate(R.layout.example_header_view, null)
    }

    private fun generateFooterView(): View {
        val v = LayoutInflater.from(this).inflate(R.layout.example_footer_view, null)
        val cancelView = v.findViewById<ImageView>(R.id.cancel)
        val shakeView = v.findViewById<ImageView>(R.id.shake)
        val likeView = v.findViewById<ImageView>(R.id.like)

        cancelView.setOnClickListener {
            it.pulse()
            adapter.swipeLeft()
        }
        likeView.setOnClickListener {
            it.pulse()
            adapter.swipeRight()
        }
        return v
    }
}
