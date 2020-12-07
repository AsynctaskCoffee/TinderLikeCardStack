package com.asynctaskcoffee.tinderlikecardstack.lib

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.view.View
import android.view.animation.AccelerateInterpolator

val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun View.pulse() {
    val animatorSet = AnimatorSet()
    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.9f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.9f, 1f)
    animatorSet.playTogether(object1, object2)
    animatorSet.duration = 150
    animatorSet.interpolator = AccelerateInterpolator()
    animatorSet.start()
}
