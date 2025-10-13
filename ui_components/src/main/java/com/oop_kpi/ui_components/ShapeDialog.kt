package com.oop_kpi.ui_components

import android.app.AlertDialog
import android.content.Context
import com.oop_kpi.drawing.Ellipse
import com.oop_kpi.drawing.Circle
import com.oop_kpi.drawing.Line
import com.oop_kpi.drawing.Rectangle
import com.oop_kpi.drawing.Shape

class ShapeDialog {

    interface OnShapeSelected {
        fun onShape(factory: (Float, Float, Float, Float, Int) -> Shape)
    }

    private val shapeFactories = listOf(
        ::Ellipse,
        ::Rectangle,
        ::Circle,
        ::Line
    )

    private val shapeNames = listOf(
        "Еліпс",
        "Прямокутник",
        "Коло",
        "Лінія"
    )

    fun getShapeName(factory: (Float, Float, Float, Float, Int) -> Shape): String {
        val index = shapeFactories.indexOf(factory)
        return if (index != -1) shapeNames[index] else "Фігура"
    }

    fun show(context: Context, listener: OnShapeSelected) {
        val names = shapeNames.toTypedArray()
        AlertDialog.Builder(context)
            .setTitle("Оберіть фігуру")
            .setItems(names) { _, which ->
                val selectedFactory = shapeFactories[which]
                listener.onShape(selectedFactory)
            }
            .show()
    }
}

