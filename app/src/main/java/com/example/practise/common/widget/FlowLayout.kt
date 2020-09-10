package com.example.practise.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.min

class FlowLayout : ViewGroup {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 获取元素padding
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        // 去掉padding后的spec
        val parentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - paddingLeft - paddingRight, widthMode)
        val parentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - paddingTop - paddingBottom, heightMode)

        var singleLine = true
        var widthUsed = paddingLeft
        var lineHeight = 0
        var heightUsed = paddingTop

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, parentWidthSpec, parentHeightSpec)

            widthUsed += child.measuredWidth
            if (widthUsed + paddingRight > widthSize) {
                singleLine = false
                widthUsed = paddingLeft + child.measuredWidth
                heightUsed += lineHeight
                lineHeight = child.measuredHeight
            } else {
                if (child.measuredHeight > lineHeight) {
                    lineHeight = child.measuredHeight
                }
            }
        }

        heightUsed += lineHeight + paddingBottom

        if (widthMode == MeasureSpec.AT_MOST || singleLine) {
            widthSize = widthUsed + paddingRight
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = min(heightSize, heightUsed)
        }

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