package com.asynctaskcoffee.tinderlikecardstack.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.asynctaskcoffee.tinderlikecardstack.R
import com.asynctaskcoffee.tinderlikecardstack.lib.CardContainer
import com.asynctaskcoffee.tinderlikecardstack.lib.CardListener
import com.asynctaskcoffee.tinderlikecardstack.lib.pulse
import com.asynctaskcoffee.tinderlikecardstack.lib.px

class MainActivity : AppCompatActivity(), CardListener {

    lateinit var cardContainer: CardContainer
    lateinit var adapter: MainAdapter
    private var modelList = arrayListOf<MainTestModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cardContainer = findViewById(R.id.cardContainer)

        cardContainer.setOnCardActionListener(this)

        /*Customization*/
        cardContainer.maxStackSize = 3
        cardContainer.marginTop = 13.px
        cardContainer.margin = 20.px

        /*Adding Extra Layouts*/
        cardContainer.setEmptyView(generateEmptyView())
        cardContainer.addFooterView(generateFooterView())
        cardContainer.addHeaderView(generateHeaderView())

        /*Setting Adapter*/
        modelList.clear()
        modelList.addAll(MainHelper.getFemaleModels())
        adapter = MainAdapter(modelList, this)
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

    private fun generateEmptyView(): View {
        return LayoutInflater.from(this).inflate(R.layout.empty_layout, null)
    }

    private fun generateHeaderView(): View {
        val v = LayoutInflater.from(this).inflate(R.layout.example_header_view, null)
        val maleSection = v.findViewById<LinearLayout>(R.id.maleSection)
        val femaleSection = v.findViewById<LinearLayout>(R.id.femaleSection)
        val allSection = v.findViewById<LinearLayout>(R.id.allSection)

        val textMale = v.findViewById<TextView>(R.id.textMale)
        val textFemale = v.findViewById<TextView>(R.id.textFemale)
        val textAll = v.findViewById<TextView>(R.id.textAll)

        maleSection.setOnClickListener {
            maleSection.setBackgroundColor(Color.WHITE)
            femaleSection.setBackgroundColor(Color.TRANSPARENT)
            allSection.setBackgroundColor(Color.TRANSPARENT)

            textMale.setTextColor(Color.parseColor("#3827a3"))
            textFemale.setTextColor(Color.parseColor("#ffffff"))
            textAll.setTextColor(Color.parseColor("#ffffff"))

            modelList.clear()
            modelList.addAll(MainHelper.getMaleModels())
            adapter = MainAdapter(modelList, this)
            cardContainer.setAdapter(adapter)
        }
        femaleSection.setOnClickListener {
            femaleSection.setBackgroundColor(Color.WHITE)
            maleSection.setBackgroundColor(Color.TRANSPARENT)
            allSection.setBackgroundColor(Color.TRANSPARENT)

            textMale.setTextColor(Color.parseColor("#ffffff"))
            textFemale.setTextColor(Color.parseColor("#3827a3"))
            textAll.setTextColor(Color.parseColor("#ffffff"))

            modelList.clear()
            modelList.addAll(MainHelper.getFemaleModels())
            adapter = MainAdapter(modelList, this)
            cardContainer.setAdapter(adapter)
        }
        allSection.setOnClickListener {
            allSection.setBackgroundColor(Color.WHITE)
            maleSection.setBackgroundColor(Color.TRANSPARENT)
            femaleSection.setBackgroundColor(Color.TRANSPARENT)

            textMale.setTextColor(Color.parseColor("#ffffff"))
            textFemale.setTextColor(Color.parseColor("#ffffff"))
            textAll.setTextColor(Color.parseColor("#3827a3"))

            modelList.clear()
            modelList.addAll(MainHelper.getAllModels())
            cardContainer.setAdapter(adapter)
        }

        return v
    }

    private fun generateFooterView(): View {
        val v = LayoutInflater.from(this).inflate(R.layout.example_footer_view, null)
        val cancelView = v.findViewById<ImageView>(R.id.cancel)
        val shakeView = v.findViewById<ImageView>(R.id.shake)
        val likeView = v.findViewById<ImageView>(R.id.like)
        val shuffleView = v.findViewById<LinearLayout>(R.id.shuffleView)

        shuffleView.setOnClickListener {
            it.pulse()
            modelList.shuffle()
            adapter = MainAdapter(modelList, this)
            cardContainer.setAdapter(adapter)
        }

        shakeView.setOnClickListener {
            it.pulse()
        }

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
