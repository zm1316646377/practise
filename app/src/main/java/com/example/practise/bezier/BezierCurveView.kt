package com.example.practise.bezier

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

// 画二阶贝塞尔曲线
class BezierCurveView: View {

    private val paint: Paint
    private val path: Path

    private var centerX: Float = 0F
    private var centerY: Float = 0F
    private var start: PointF
    private var end: PointF
    private var control:PointF

    constructor(context: Context): super(context)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        paint = Paint()
        path = Path()
        paint.color = Color.BLACK
        paint.strokeWidth = 8f
        paint.style = Paint.Style.STROKE
        paint.textSize = 60f

        start = PointF(0F, 0F)
        end = PointF(0F, 0F)
        control = PointF(0F, 0F)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制数据点和控制点
        paint.apply {
            color = Color.GRAY
            strokeWidth = 20F
        }
        canvas.apply {
            drawPoint(start.x, start.y, paint)
            drawPoint(end.x, end.y, paint)
            drawPoint(control.x, control.y, paint)
        }

        //绘制辅助线
        paint.strokeWidth = 4F
        canvas.drawLine(start.x, start.y, control.x, control.y, paint)
        canvas.drawLine(control.x, control.y, end.x, end.y, paint)

        //绘制二阶贝塞尔曲线
        paint.apply {
            color = Color.RED
            strokeWidth = 8F
        }
        path.apply {
            reset()
            moveTo(start.x, start.y)
            quadTo(control.x, control.y, end.x, end.y)
        }
        canvas.drawPath(path, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w/2.toFloat()
        centerY = h/2.toFloat()
        //初始化数据点和控制点的位置
        start.x = centerX - 200
        start.y = centerY
        end.x = centerX + 200;
        end.y = centerY;
        control.x = centerX
        control.y = centerY - 100
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                control.x = event.x
                control.y = event.y
                invalidate()
            }
        }
        return true
    }
}