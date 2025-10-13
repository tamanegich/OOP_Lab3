package com.oop_kpi.drawing

import android.graphics.*
import androidx.core.graphics.toColorInt
import kotlin.math.hypot

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
