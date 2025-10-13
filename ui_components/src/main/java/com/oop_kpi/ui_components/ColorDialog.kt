package com.oop_kpi.ui_components

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color

class ColorDialog {
    interface OnColorSelected {
        fun onColor(color: Int)
    }

    fun show(context: Context, listener: OnColorSelected) {
        val colors = arrayOf("Червоний", "Зелений", "Синій", "Жовтий", "Чорний", "Білий", "Прозорий")
        val colorValues = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK, Color.WHITE,
            Color.TRANSPARENT)

        AlertDialog.Builder(context)
            .setTitle("Select Color")
            .setItems(colors) { _, which ->
                listener.onColor(colorValues[which])
            }
            .show()
    }
}
