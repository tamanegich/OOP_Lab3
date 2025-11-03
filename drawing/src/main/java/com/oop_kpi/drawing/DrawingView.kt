package com.oop_kpi.drawing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        private const val MAX_SHAPES = 122
        val shapes = arrayOfNulls<Shape>(MAX_SHAPES)
        var shapeCount = 0
    }

    private val paint = Paint().apply {
        isAntiAlias = true
    }

    private var isCancelled = false
    private var activePointerId = -1

    private var currentShapeFactory: (Float, Float, Float, Float, Int) -> Shape = ::Ellipse
    private var currentColor: Int = Color.BLACK

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f

    private var previewShape: Shape? = null
    private val undoStack = mutableListOf<Shape>()
    private val redoStack = mutableListOf<Shape>()

    private val eventHandlers: Map<Int, (MotionEvent) -> Unit> = mapOf(
        MotionEvent.ACTION_DOWN to ::handleActionDown,
        MotionEvent.ACTION_POINTER_DOWN to ::handlePointerDown,
        MotionEvent.ACTION_MOVE to ::handleActionMove,
        MotionEvent.ACTION_UP to ::handleActionUp,
        MotionEvent.ACTION_POINTER_UP to ::handlePointerUp,
        MotionEvent.ACTION_CANCEL to ::handleActionCancel
    ) as Map<Int, (MotionEvent) -> Unit>

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
        undoStack.clear()
        redoStack.clear()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        eventHandlers[event.actionMasked]?.invoke(event)
        return true
    }

    private fun handleActionDown(event: MotionEvent) {
        activePointerId = event.getPointerId(0)
        val index = event.findPointerIndex(activePointerId)
        if (index != -1) {
            startX = event.getX(index)
            startY = event.getY(index)
            endX = startX
            endY = startY
            isCancelled = false
        }
    }

    private fun handlePointerDown(event: MotionEvent) {
        if (event.pointerCount > 1) {
            isCancelled = true
            previewShape = null
            invalidate()
        }
    }

    private fun handleActionMove(event: MotionEvent) {
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

    private fun handleActionUp(event: MotionEvent) {
        addShapeIfValid(event.getPointerId(event.actionIndex))
    }

    private fun handlePointerUp(event: MotionEvent) {
        addShapeIfValid(event.getPointerId(event.actionIndex))
    }

    private fun addShapeIfValid(pointerId: Int) {
        if (pointerId == activePointerId && !isCancelled && shapeCount < shapes.size) {
            val shape = currentShapeFactory(startX, startY, endX, endY, currentColor)
            shapes[shapeCount++] = shape
            undoStack.add(shape)
            redoStack.clear()
        }
        previewShape = null
        invalidate()
    }

    private fun handleActionCancel() {
        isCancelled = true
        previewShape = null
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until shapeCount) {
            shapes[i]?.draw(canvas, paint)
        }
        previewShape?.draw(canvas, paint, isPreview = true)
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val shape = undoStack.removeAt(undoStack.lastIndex)
            redoStack.add(shape)
            val index = shapes.indexOf(shape)
            if (index != -1) {
                shapes[index] = null
                shapeCount--
            }
            invalidate()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val shape = redoStack.removeAt(redoStack.lastIndex)
            undoStack.add(shape)
            shapes[shapeCount++] = shape
            invalidate()
        }
    }
}
