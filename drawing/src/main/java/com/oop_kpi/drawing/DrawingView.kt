package com.oop_kpi.drawing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.hypot
import androidx.core.graphics.toColorInt

abstract class Shape(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val color: Int
) {
    abstract fun draw(canvas: Canvas, paint: Paint, isPreview: Boolean = false)

    protected fun configurePaint(paint: Paint, isPreview: Boolean) {
        if (isPreview) {
            paint.color = "#275BF5".toColorInt()
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 3f
        } else {
            if (color == Color.TRANSPARENT) {
                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE
            } else {
                paint.color = color
                paint.style = Paint.Style.FILL
            }
            paint.strokeWidth = 8f
        }
    }
}


class Ellipse(startX: Float, startY: Float, endX: Float, endY: Float, color: Int) :
    Shape(startX, startY, endX, endY, color) {
    override fun draw(canvas: Canvas, paint: Paint, isPreview: Boolean) {
        val left = minOf(startX, endX)
        val top = minOf(startY, endY)
        val right = maxOf(startX, endX)
        val bottom = maxOf(startY, endY)

        configurePaint(paint, isPreview)

        canvas.drawOval(RectF(left, top, right, bottom), paint)
    }
}

class Circle(startX: Float, startY: Float, endX: Float, endY: Float, color: Int) :
    Shape(startX, startY, endX, endY, color) {
    override fun draw(canvas: Canvas, paint: Paint, isPreview: Boolean) {
        val radius = hypot((endX - startX).toDouble(), (endY - startY).toDouble()).toFloat()

        configurePaint(paint, isPreview)

        canvas.drawCircle(startX, startY, radius, paint)
    }
}
class Rectangle(startX: Float, startY: Float, endX: Float, endY: Float, color: Int) :
    Shape(startX, startY, endX, endY, color) {
    override fun draw(canvas: Canvas, paint: Paint, isPreview: Boolean) {
        val left = minOf(startX, endX)
        val right = maxOf(startX, endX)
        val top = minOf(startY, endY)
        val bottom = maxOf(startY, endY)

        configurePaint(paint, isPreview)

        canvas.drawRect(left, top, right, bottom, paint)
    }
}
class Line(startX: Float, startY: Float, endX: Float, endY: Float, color: Int) :
    Shape(startX, startY, endX, endY, color) {
    override fun draw(canvas: Canvas, paint: Paint, isPreview: Boolean) {
        configurePaint(paint, isPreview)
        paint.style = Paint.Style.STROKE
        canvas.drawLine(startX, startY, endX, endY, paint)
    }
}

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private var isCancelled = false
    private var activePointerId = -1
    private val n = 122
    private val shapes = arrayOfNulls<Shape>(n)
    private var shapeCount = 0

    private var currentShapeFactory: (Float, Float, Float, Float, Int) -> Shape = ::Ellipse
    private var currentColor: Int = Color.BLACK

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f

    private var previewShape: Shape? = null

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
        previewShape = null
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                activePointerId = event.getPointerId(0)
                val index = event.findPointerIndex(activePointerId)
                startX = event.getX(index)
                startY = event.getY(index)
                endX = startX
                endY = startY
                isCancelled = false
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount > 1) {
                    isCancelled = true
                    previewShape = null
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isCancelled) {
                    val index = event.findPointerIndex(activePointerId)
                    if (index != -1) {
                        endX = event.getX(index)
                        endY = event.getY(index)
                        previewShape = currentShapeFactory(startX, startY, endX, endY, currentColor)
                        invalidate()
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val pointerId = event.getPointerId(event.actionIndex)
                if (pointerId == activePointerId && !isCancelled && shapeCount < shapes.size) {
                    shapes[shapeCount++] = currentShapeFactory(startX, startY, endX, endY, currentColor)
                }
                previewShape = null
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                isCancelled = true
                previewShape = null
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
        previewShape?.draw(canvas, paint, isPreview = true)
    }
}
