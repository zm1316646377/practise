package com.example.practise.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class FlowLayout : ViewGroup {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 获取元素padding
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - paddingLeft - paddingRight, widthMode)
        val childHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - paddingTop - paddingBottom, heightMode)

        measureChildren(childWidthSpec, childHeightSpec)

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var widthUsed = paddingLeft
        var lineHeight = 0
        var heightUsed = paddingTop

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (widthUsed + child.measuredWidth + paddingRight >= measuredWidth) {
                widthUsed = paddingLeft
                heightUsed += lineHeight
                lineHeight = 0
            }

            child.layout(widthUsed, heightUsed, widthUsed + child.measuredWidth, heightUsed + child.measuredHeight)

            widthUsed += child.measuredWidth
            if (child.height > lineHeight) {
                lineHeight = child.height
            }
        }
    }
}