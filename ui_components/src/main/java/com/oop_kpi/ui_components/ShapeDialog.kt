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

    fun show(context: Context, listener: OnShapeSelected) {
        val shapes = listOf(
            "Еліпс" to ::Ellipse,
            "Круг" to ::Circle,
            "Прямокутник" to ::Rectangle,
            "Лінія" to ::Line
        )

        AlertDialog.Builder(context)
            .setTitle("Оберіть фігуру")
            .setItems(shapes.map { it.first }.toTypedArray()) { _, which ->
                val factory = shapes[which].second
                listener.onShape(factory)
            }
            .show()
    }
}
