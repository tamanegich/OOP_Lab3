package com.oop_kpi.drawing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.hypot

abstract class Shape(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val color: Int
) {
    abstract fun draw(canvas: Canvas, paint: Paint)
}

class Ellipse(startX: Float, startY: Float, endX: Float, endY: Float, color: Int) :
    Shape(startX, startY, endX, endY, color) {

    override fun draw(canvas: Canvas, paint: Paint) {
        val left = minOf(startX, endX)
        val top = minOf(startY, endY)
        val right = maxOf(startX, endX)
        val bottom = maxOf(startY, endY)

        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawOval(RectF(left, top, right, bottom), paint)
    }
}

class Circle(startX: Float, startY: Float, endX: Float, endY: Float, color: Int) :
    Shape(startX, startY, endX, endY, color) {
    override fun draw(canvas: Canvas, paint: Paint) {
        val radius = hypot(
            (endX - startX).toDouble(),
            (endY - startY).toDouble()
        ).toFloat()
        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawCircle(startX, startY, radius, paint)
    }
}

class Rectangle(startX: Float, startY: Float, endX: Float, endY: Float, color: Int) :
    Shape(startX, startY, endX, endY, color) {
    override fun draw(canvas: Canvas, paint: Paint) {
        val left = minOf(startX, endX)
        val right = maxOf(startX, endX)
        val top = minOf(startY, endY)
        val bottom = maxOf(startY, endY)
        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawRect(left, top, right, bottom, paint)
    }
}

class Line(startX: Float, startY: Float, endX: Float, endY: Float, color: Int) :
    Shape(startX, startY, endX, endY, color) {
    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        paint.strokeWidth = 8f
        paint.style = Paint.Style.STROKE
        canvas.drawLine(startX, startY, endX, endY, paint)
    }
}

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        strokeWidth = 8f
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val n = 122
    private val shapes = arrayOfNulls<Shape>(n)
    private var shapeCount = 0

    private var currentShapeFactory: (Float, Float, Float, Float, Int) -> Shape = ::Ellipse

    private var currentColor: Int = Color.BLACK

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f

    init {
        setBackgroundColor(Color.WHITE)
    }

    fun setColor(color: Int) {
        currentColor = color
    }

    fun setShape(factory: (Float, Float, Float, Float, Int) -> Shape) {
        currentShapeFactory = factory
    }

    fun clearCanvas() {
        for (i in 0 until shapeCount) {
            shapes[i] = null
        }
        shapeCount = 0
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                val shape = currentShapeFactory(startX, startY, endX, endY, currentColor)
                if (shapeCount < n) {
                    shapes[shapeCount++] = currentShapeFactory(startX, startY, endX, endY, currentColor)
                }
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until shapeCount) {
            shapes[i]?.draw(canvas, paint)
        }
    }
}
