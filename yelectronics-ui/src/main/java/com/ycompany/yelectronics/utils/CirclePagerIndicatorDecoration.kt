package com.ycompany.yelectronics.utils

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import java.util.Locale

class CirclePagerIndicatorDecoration(@ColorInt colorInactive: Int) :
    ItemDecoration() {
    private var colorActive = -0x22000000
    private val colorInactive = 0x33000000

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private val mIndicatorHeight: Int =
        (DP * 16).toInt()

    /**
     * Indicator stroke width.
     */
    private val mIndicatorStrokeWidth: Float =
        DP * 4

    /**
     * Indicator width.
     */
    private val mIndicatorItemLength: Float =
        DP * 4

    /**
     * Padding between indicators.
     */
    private val mIndicatorItemPadding: Float =
        DP * 8

    /**
     * Some more natural animation interpolation
     */
    private val mInterpolator: Interpolator = AccelerateDecelerateInterpolator()
    private val mPaint = Paint()

    init {
        mPaint.strokeWidth = mIndicatorStrokeWidth
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true
        colorActive = colorInactive
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val itemCount = parent.adapter!!.itemCount

        // center horizontally, calculate width and subtract half from center
        val totalLength = mIndicatorItemLength * itemCount
        val paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        var indicatorStartX = (parent.width - indicatorTotalWidth) / 2f

        // center vertically in the allotted space
        val indicatorPosY = parent.height - mIndicatorHeight / 2f
        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)

        // find active page (which should be highlighted)
        val layoutManager = parent.layoutManager as LinearLayoutManager?
        val activePosition: Int
        activePosition = if (isRtlLanguage) {
            layoutManager!!.findLastVisibleItemPosition()
        } else {
            layoutManager!!.findFirstVisibleItemPosition()
        }
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }

        // find offset of active page (if the user is scrolling)
        val activeChild = layoutManager.findViewByPosition(activePosition)
        val left = activeChild!!.left
        val width = activeChild.width
        val right = activeChild.right

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        val progress = mInterpolator.getInterpolation(left * -1 / width.toFloat())
        if (isRtlLanguage) {
            indicatorStartX =
                (parent.width + indicatorTotalWidth) / 2f - (mIndicatorItemLength + DP * 4) / 2
        }

        //        float indicatorStartXhl = (parent.getWidth() + indicatorTotalWidth) / 2F - (mIndicatorItemLength + DP * 4) / 2;
        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress)
    }

    private fun drawInactiveIndicators(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        itemCount: Int
    ) {
        mPaint.color = colorInactive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
        var start = indicatorStartX
        for (i in 0 until itemCount) {
            c.drawCircle(start, indicatorPosY, mIndicatorItemLength / 2f, mPaint)
            start += itemWidth
        }
    }

    private fun drawHighlights(
        c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
        highlightPosition: Int, progress: Float
    ) {
        mPaint.color = colorActive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
        if (progress == 0f) {
            // no swipe, draw a normal indicator
            val highlightStart: Float
            highlightStart = if (isRtlLanguage) {
                indicatorStartX - itemWidth * highlightPosition
            } else {
                indicatorStartX + itemWidth * highlightPosition
            }
            c.drawCircle(highlightStart, indicatorPosY, mIndicatorItemLength / 2f, mPaint)
        } else {
            val highlightStart: Float
            highlightStart = if (isRtlLanguage) {
                indicatorStartX - itemWidth * highlightPosition
            } else {
                indicatorStartX + itemWidth * highlightPosition
            }
            val partialLength =
                mIndicatorItemLength * progress + mIndicatorItemPadding * progress
            c.drawCircle(
                highlightStart + partialLength,
                indicatorPosY,
                mIndicatorItemLength / 2f,
                mPaint
            )
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = mIndicatorHeight
    }//You can change here to your specific language

    //The method that checks if it's RTL language:
    private val isRtlLanguage: Boolean
        private get() {
            val deviceLanguage = Locale.getDefault().language
            return deviceLanguage.contains("iw") || deviceLanguage.contains("ar") //You can change here to your specific language
        }

    companion object {
        private val DP = Resources.getSystem().displayMetrics.density
    }
}