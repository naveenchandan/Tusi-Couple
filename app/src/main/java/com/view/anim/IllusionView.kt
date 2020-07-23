package com.view.anim

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class IllusionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var numberOfLines = 1
        set(value) {
            if (value < MIN_DOTS || value > MAX_DOTS) {
                return
            }
            field = value
            linesToDraw = value * 2
            invalidate()
        }

    var tracePath = false
        set(value) {
            field = value
            invalidate()
        }

    var innerCircleVisible = false
        set(value) {
            field = value
            invalidate()
        }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5.0f
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 5.0f
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
    }

    private val centerPointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
    }

    private var radius = 0.0f

    private var angle = 0.0f

    private var linesToDraw = numberOfLines * 2

    private val valueAnimator = ValueAnimator().apply {
        setFloatValues(0.0f, 360.0f)
        duration = ANIM_DURATION
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        interpolator = null
        addUpdateListener {
            angle = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (valueAnimator.isRunning) {
            valueAnimator.cancel()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = min(width, height) * 0.5f - POINT_RADIUS
        if (valueAnimator.isRunning) {
            valueAnimator.cancel()
        }
        valueAnimator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawCircle(pivotX, pivotY, radius, circlePaint)
            if (tracePath) {
                drawLines(it)
            }
            drawInnerCircle(it)
        }

    }

    private fun drawLines(canvas: Canvas) {
        val theta = 360.0 / linesToDraw
        for (i in 0 until linesToDraw) {
            val distanceX =
                pivotX + (radius * cos(Math.toRadians(theta * i)).toFloat())
            val distanceY =
                pivotY + (radius * sin(Math.toRadians(theta * i)).toFloat())
            canvas.drawLine(
                pivotX, pivotY, distanceX, distanceY, linePaint
            )
        }
    }

    private fun drawInnerCircle(canvas: Canvas) {
        val theta = 360.0f / numberOfLines
        for (i in 0 until numberOfLines) {
            val cX =
                pivotX + ((radius / 2) * cos(Math.toRadians((theta * i) + angle.toDouble())).toFloat())
            val cY =
                pivotY + ((radius / 2) * sin(Math.toRadians((theta * i) + angle.toDouble())).toFloat())
            val dX =
                cX + ((radius / 2) * cos(Math.toRadians(360.0f - angle.toDouble())).toFloat())
            val dY =
                cY + ((radius / 2) * sin(Math.toRadians(360.0f - angle.toDouble())).toFloat())
            if (innerCircleVisible) {
                canvas.drawCircle(cX, cY, radius / 2, circlePaint)
                canvas.drawLine(cX, cY, dX, dY, linePaint)
                canvas.drawCircle(cX, cY, POINT_RADIUS / 2, centerPointPaint)
            }
            canvas.drawCircle(dX, dY, POINT_RADIUS, dotPaint)
        }
    }

    companion object {
        private const val MAX_DOTS = 20
        private const val MIN_DOTS = 1
        private const val ANIM_DURATION = 4000L
        private const val POINT_RADIUS = 20.0f
    }
}