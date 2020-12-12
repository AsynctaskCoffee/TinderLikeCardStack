package com.asynctaskcoffee.cardstack

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.Transformation
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.contains
import kotlin.math.roundToInt


@SuppressLint("ClickableViewAccessibility")
class CardContainer(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    View.OnTouchListener, CardContainerAdapter.DataListener, CardContainerAdapter.ActionListener {

    private var cardListener: CardListener? = null

    fun setOnCardActionListener(cardListener: CardListener) {
        this.cardListener = cardListener
    }

    private var cardContainerAdapter: CardContainerAdapter? = null

    var margin = 20.px
    var marginTop = 10.px
    var maxStackSize = 5

    private var mainContainer: FrameLayout? = null
    private var headerContainer: FrameLayout? = null
    private var footerContainer: FrameLayout? = null
    private var emptyContainer: FrameLayout? = null
    private var draggableSurfaceLayout: FrameLayout? = null

    private var rightBoundary = 0f
    private var leftBoundary = 0f
    private var screenWidth = 0
    private var swipeIndex = 0

    private var count = 0
    private var oldX = 0f
    private var oldY = 0f
    private var newX = 0f
    private var newY = 0f
    private var dX = 0f
    private var dY = 0f
    private var resetX = 0f
    private var resetY = 0f
    private val cardDegreesForTransform = 40.0f

    private var viewList: ArrayList<View> = arrayListOf()

    init {
        screenWidth = Resources.getSystem().displayMetrics.widthPixels
        leftBoundary = screenWidth * (1.0f / 6.0f)
        rightBoundary = screenWidth * (5.0f / 6.0f)
        setupSurface()
    }


    fun setEmptyView(v: View) {
        emptyContainer?.visibility = View.VISIBLE
        mainContainer?.visibility = View.GONE
        if (v.parent != null)
            (v.parent as ViewGroup).removeView(v)
        emptyContainer?.removeAllViews()
        emptyContainer?.addView(v)
    }

    fun addHeaderView(v: View) {
        if (v.parent != null)
            (v.parent as ViewGroup).removeView(v)
        headerContainer?.removeAllViews()
        headerContainer?.addView(v)
    }

    fun addFooterView(v: View) {
        if (v.parent != null)
            (v.parent as ViewGroup).removeView(v)
        footerContainer?.removeAllViews()
        footerContainer?.addView(v)
    }

    fun getHeaderView(): View? = headerContainer
    fun getFooterView(): View? = footerContainer

    private fun setupSurface() {
        val viewMain = LayoutInflater.from(context).inflate(R.layout.card_container, null)
        mainContainer = viewMain.findViewById(R.id.mainContainer)
        emptyContainer = viewMain.findViewById(R.id.emptyLayout)
        headerContainer = viewMain.findViewById(R.id.headerContainer)
        footerContainer = viewMain.findViewById(R.id.footerContainer)
        draggableSurfaceLayout = viewMain.findViewById(R.id.draggableSurfaceLayout)
        addView(viewMain)
    }

    private fun setCardForAnimation() {
        if (viewList.isNotEmpty()) {
            viewList[viewList.size - 1].setOnTouchListener(this)
        } else {
            emptyContainer?.visibility = View.VISIBLE
            mainContainer?.visibility = View.GONE
            cardListener?.onSwipeCompleted()
        }
    }

    private fun reOrderMarginsForNewItems() {
        if (viewList.isNotEmpty() && mainContainer != null) {
            for (i in 0 until mainContainer!!.childCount) {
                val card = mainContainer!!.getChildAt(i).findViewById<CardView>(R.id.card)
                viewWithNewMarginAnimationForNewItems(card, i)
                card.elevation = (mainContainer!!.childCount + 1 - i).toFloat()
            }
        }
    }

    private fun reOrderMargins() {
        if (viewList.isNotEmpty() && mainContainer != null) {
            for (i in 0 until mainContainer!!.childCount) {
                val card = mainContainer!!.getChildAt(i).findViewById<CardView>(R.id.card)
                viewWithNewMarginAnimation(card)
                card.elevation = (mainContainer!!.childCount + 1 - i).toFloat()
            }
            if (nextCondition()) {
                addOneMore()
            }
        }
    }

    private fun nextCondition() =
        (cardContainerAdapter != null && ((cardContainerAdapter?.getCount() ?: 0) > count))

    private fun viewWithNewMarginAnimation(v: View) {
        val newLeftMargin = (v.layoutParams as ConstraintLayout.LayoutParams).leftMargin - margin
        val newTopMargin =
            (v.layoutParams as ConstraintLayout.LayoutParams).topMargin + (if (nextCondition()) marginTop else 0)
        val newRightMargin = (v.layoutParams as ConstraintLayout.LayoutParams).rightMargin - margin
        val newBottomMargin =
            (v.layoutParams as ConstraintLayout.LayoutParams).bottomMargin - margin

        val oldLeftMargin = (v.layoutParams as ConstraintLayout.LayoutParams).leftMargin
        val oldTopMargin = (v.layoutParams as ConstraintLayout.LayoutParams).topMargin
        val oldRightMargin = (v.layoutParams as ConstraintLayout.LayoutParams).rightMargin
        val oldBottomMargin = (v.layoutParams as ConstraintLayout.LayoutParams).bottomMargin

        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val params = v.layoutParams as ConstraintLayout.LayoutParams
                params.leftMargin =
                    (oldLeftMargin + ((newLeftMargin - oldLeftMargin) * interpolatedTime)).roundToInt()
                params.topMargin =
                    (oldTopMargin + ((newTopMargin - oldTopMargin) * interpolatedTime)).roundToInt()
                params.rightMargin =
                    (oldRightMargin + ((newRightMargin - oldRightMargin) * interpolatedTime)).roundToInt()
                params.bottomMargin =
                    (oldBottomMargin + ((newBottomMargin - oldBottomMargin) * interpolatedTime)).roundToInt()
                v.layoutParams = params
            }
        }
        a.duration = 50
        v.startAnimation(a)
    }

    private fun viewWithNewMarginAnimationForNewItems(v: View, i: Int) {

        val newLeftMargin = margin * (maxStackSize - i)
        val newTopMargin = (i + 1) * marginTop
        val newRightMargin = margin * (maxStackSize - i)
        val newBottomMargin = margin * (maxStackSize - i)

        val oldLeftMargin = (v.layoutParams as ConstraintLayout.LayoutParams).leftMargin
        val oldTopMargin = (v.layoutParams as ConstraintLayout.LayoutParams).topMargin
        val oldRightMargin = (v.layoutParams as ConstraintLayout.LayoutParams).rightMargin
        val oldBottomMargin = (v.layoutParams as ConstraintLayout.LayoutParams).bottomMargin

        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val params = v.layoutParams as ConstraintLayout.LayoutParams
                params.leftMargin =
                    (oldLeftMargin + ((newLeftMargin - oldLeftMargin) * interpolatedTime)).roundToInt()
                params.topMargin =
                    (oldTopMargin + ((newTopMargin - oldTopMargin) * interpolatedTime)).roundToInt()
                params.rightMargin =
                    (oldRightMargin + ((newRightMargin - oldRightMargin) * interpolatedTime)).roundToInt()
                params.bottomMargin =
                    (oldBottomMargin + ((newBottomMargin - oldBottomMargin) * interpolatedTime)).roundToInt()
                v.layoutParams = params
            }
        }
        a.duration = 150
        v.startAnimation(a)
    }

    private fun isCardAtLeft(view: View?): Boolean {
        if (view != null) {
            return view.x + view.width / 2 < leftBoundary
        }
        return false
    }

    private fun isCardAtRight(view: View?): Boolean {
        if (view != null) {
            return view.x + view.width / 2 > rightBoundary
        }
        return false
    }


    private var isFirstTimeMove = false

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v != null)
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    isFirstTimeMove = mainContainer?.contains(v) == true
                    if (v.parent != null) {
                        (v.parent as ViewGroup).removeView(v)
                        v.layoutParams = LayoutParams(
                            viewList[viewList.size - 1].width,
                            viewList[viewList.size - 1].height
                        )
                        (v.layoutParams as MarginLayoutParams).topMargin += mainContainer!!.y.toInt()
                        draggableSurfaceLayout?.addView(v)
                    }
                    oldX = event.x
                    oldY = event.y
                    v.clearAnimation()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    isFirstTimeMove = false
                    when {
                        isCardAtLeft(v) -> {
                            dismissCard(v, -(screenWidth * 2),false)
                            cardContainerAdapter?.let {
                                if (it.getCount() > swipeIndex) {
                                    cardListener?.onLeftSwipe(swipeIndex, it.getItem(swipeIndex))
                                    swipeIndex++
                                }
                            }
                        }
                        isCardAtRight(v) -> {
                            dismissCard(v, (screenWidth * 2),false)
                            cardContainerAdapter?.let {
                                if (it.getCount() > swipeIndex) {
                                    cardListener?.onRightSwipe(swipeIndex, it.getItem(swipeIndex))
                                    swipeIndex++
                                }
                            }
                        }
                        else -> {
                            resetCard(v)
                            cardContainerAdapter?.let {
                                if (it.getCount() > swipeIndex) {
                                    cardListener?.onSwipeCancel(swipeIndex, it.getItem(swipeIndex))
                                }
                            }
                        }
                    }
                    return true
                }

                MotionEvent.ACTION_MOVE -> {
                    newX = event.x
                    newY = event.y.plus(if (isFirstTimeMove) mainContainer!!.y else 0f)
                    dX = newX - oldX
                    dY = newY - oldY
                    v.x = v.x.plus(dX)
                    v.y = v.y.plus(dY)
                    setCardRotation(v, v.x)
                    return true
                }
                else -> return super.onTouchEvent(event)
            }
        return super.onTouchEvent(event)
    }


    private fun dismissCard(card: View, xPos: Int, rotate: Boolean) {
        card.animate()
            .x(xPos.toFloat())
            .y(0F)
            .rotation(if (rotate) {if (xPos > 0) 45f else -45f} else 0f)
            .setInterpolator(AccelerateInterpolator())
            .setDuration(300)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    card.parent?.let { viewParent ->
                        val viewGroup = viewParent as FrameLayout
                        viewGroup.removeView(card)

                        if (viewList.isNotEmpty()) {
                            viewList.removeLast()
                            cardContainerAdapter?.let {
                                if (it.getCount() > swipeIndex)
                                    cardListener?.onItemShow(
                                        swipeIndex,
                                        it.getItem(swipeIndex)
                                    )
                            }
                            setCardForAnimation()
                            reOrderMargins()
                        }
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationRepeat(animation: Animator?) {

                }
            })
    }

    private fun resetCard(card: View) {
        card.animate()
            .x(resetX)
            .y(mainContainer!!.y)
            .rotation(0F)
            .setInterpolator(OvershootInterpolator()).duration = 300
    }

    private fun setCardRotation(card: View, posX: Float) {
        val rotation = (cardDegreesForTransform * (posX)) / screenWidth
        val halfCardHeight = (card.height / 2)
        if (oldY < halfCardHeight) {
            card.rotation = rotation
        } else {
            card.rotation = -rotation
        }
    }

    private fun reset() {
        swipeIndex = 0
        count = 0
        viewList.clear()
        mainContainer?.removeAllViews()
    }

    fun setAdapter(cardContainerAdapter: CardContainerAdapter) {
        reset()

        this.cardContainerAdapter = cardContainerAdapter
        this.cardContainerAdapter?.dataListener = this
        this.cardContainerAdapter?.actionListener = this

        if (cardContainerAdapter.getCount() > 0) {
            emptyContainer?.visibility = View.GONE
            mainContainer?.visibility = View.VISIBLE
        } else {
            return
        }

        val size =
            if (cardContainerAdapter.getCount() > maxStackSize) maxStackSize else cardContainerAdapter.getCount()

        for (i in size downTo 1) {
            val childCard = LayoutInflater.from(context).inflate(R.layout.card_pose, null)
            val holder = childCard.findViewById<FrameLayout>(R.id.frame)
            val card = childCard.findViewById<CardView>(R.id.card)
            (card.layoutParams as ConstraintLayout.LayoutParams).setMargins(
                margin * i,
                marginTop * (size + 1 - i),
                margin * i,
                margin * i
            )
            card.elevation = (size + 1 - i).toFloat()
            holder.addView(cardContainerAdapter.getView(i - 1))
            viewList.add(childCard)
        }
        for (view in viewList) {
            mainContainer?.addView(view)
        }
        mainContainer?.pulseOnlyUp()


        count = viewList.size

        setCardForAnimation()
        cardListener?.onItemShow(swipeIndex, cardContainerAdapter.getItem(swipeIndex))
    }

    private fun addOneMore() {
        if (count < cardContainerAdapter?.getCount() ?: 0) {
            val childCard = LayoutInflater.from(context).inflate(R.layout.card_pose, null)
            val holder = childCard.findViewById<FrameLayout>(R.id.frame)
            val card = childCard.findViewById<CardView>(R.id.card)
            (card.layoutParams as ConstraintLayout.LayoutParams).setMargins(
                margin * (viewList.size + 1),
                marginTop,
                margin * (viewList.size + 1),
                margin * (viewList.size + 1)
            )
            card.elevation = 1.toFloat()
            holder.addView(cardContainerAdapter?.getView(count))
            viewList.add(0, childCard)
            mainContainer?.addView(childCard, 0)
            count++
        }
    }

    override fun notifyAppendData() {
        val needCount = maxStackSize - mainContainer!!.childCount
        if (needCount > 0) {
            emptyContainer?.visibility = View.GONE
            mainContainer?.visibility = View.VISIBLE
        }
        if ((count + needCount) < cardContainerAdapter!!.getCount()) {
            val size = (count + needCount)
            for (i in count until size) {
                val childCard = LayoutInflater.from(context).inflate(R.layout.card_pose, null)
                val holder = childCard.findViewById<FrameLayout>(R.id.frame)
                val card = childCard.findViewById<CardView>(R.id.card)
                (card.layoutParams as ConstraintLayout.LayoutParams).setMargins(
                    margin * (viewList.size + 1),
                    marginTop * (size + 1 - i),
                    margin * (viewList.size + 1),
                    margin * (viewList.size + 1)
                )
                card.elevation = 1.toFloat()
                holder.addView(cardContainerAdapter?.getView(i))
                viewList.add(0, childCard)
                mainContainer?.addView(childCard, 0)
                count++
            }
        }
        reOrderMarginsForNewItems()
        setCardForAnimation()
    }

    override fun swipeRight() {
        if (viewList.isNotEmpty()) {
            if (viewList[viewList.size - 1].animation?.hasEnded() == false)
                return
            dismissCard(viewList[viewList.size - 1], (screenWidth * 2),true)
            cardContainerAdapter?.let {
                if (it.getCount() > swipeIndex) {
                    cardListener?.onRightSwipe(swipeIndex, it.getItem(swipeIndex))
                    swipeIndex++
                }
            }
        }
    }

    override fun swipeLeft() {
        if (viewList.isNotEmpty()) {
            if (viewList[viewList.size - 1].animation?.hasEnded() == false)
                return
            dismissCard(viewList[viewList.size - 1], -(screenWidth * 2),true)
            cardContainerAdapter?.let {
                if (it.getCount() > swipeIndex) {
                    cardListener?.onLeftSwipe(swipeIndex, it.getItem(swipeIndex))
                    swipeIndex++
                }
            }
        }
    }

}