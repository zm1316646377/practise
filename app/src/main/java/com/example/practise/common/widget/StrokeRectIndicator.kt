package com.example.practise.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.youth.banner.indicator.BaseIndicator

// 轮播图的indicator, selected 实心, 其他空心
class StrokeRectIndicator(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseIndicator(context, attrs, defStyleAttr) {

    private val rectF = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config.indicatorSize
        if (count < 1) return
        val width = ((count -1) * config.indicatorSpace + count * config.normalWidth).toInt()
        setMeasuredDimension(width, config.height)
    }

    override fun onDraw(canvas: Canvas) {
        val count = config.indicatorSize
        if (count <= 1) return

        var left = 0F
        for (i in 0 until count) {
            mPaint.color = if (config.currentPosition == i) config.selectedColor else config.normalColor
            mPaint.style = if (config.currentPosition == i) Paint.Style.FILL else Paint.Style.STROKE
            rectF.apply {
                this.left = left
                this.top = 0F
                this.right = left + config.normalWidth
                this.bottom = config.height.toFloat()
            }
            left = (i + 1) * (config.normalWidth + config.indicatorSpace)
            canvas.drawRect(rectF, mPaint)
        }
    }
}