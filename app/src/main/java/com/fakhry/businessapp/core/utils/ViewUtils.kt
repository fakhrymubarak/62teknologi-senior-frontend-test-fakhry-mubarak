package com.fakhry.businessapp.core.utils

import android.view.View
import android.widget.ImageView
import coil.load
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.facebook.shimmer.ShimmerFrameLayout
import com.fakhry.businessapp.R

private fun View.visible() {
    this.visibility = View.VISIBLE
}

private fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible(state: Boolean, isGone: Boolean = false) {
    if (state) this.visible() else if (isGone) this.gone() else this.invisible()
}


fun ImageView.loadWithShimmer(data: Any) {
    this.load(data) {
        error(R.drawable.ic_placeholder_error)
        placeholder(getPlaceholderShimmer())
    }
}

/**
 * This is the placeholder for the imageView in coil
 */
private fun getPlaceholderShimmer() = ShimmerDrawable().apply {
    val shimmer =
        Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1000) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.95f) //the alpha of the underlying children
            .setHighlightAlpha(0.85f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()
    setShimmer(shimmer)
}

fun ShimmerFrameLayout.isShimmerStarted(state: Boolean, isGone: Boolean = true) {
    if (state) {
        this.startShimmer()
        this.visible()
    } else {
        this.stopShimmer()
        if (isGone) this.gone() else this.invisible()
    }
}