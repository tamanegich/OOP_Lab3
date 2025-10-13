package com.oop_kpi.ui_components

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color

class ColorDialog {
    interface OnColorSelected {
        fun onColor(color: Int)
    }

    private val colorNames = mapOf(
        Color.RED to "Червоний",
        Color.GREEN to "Зелений",
        Color.BLUE to "Синій",
        Color.YELLOW to "Жовтий",
        Color.BLACK to "Чорний",
        Color.GRAY to "Сірий",
        Color.WHITE to "Білий",
        Color.TRANSPARENT to "Прозорий"
    )

    fun getColorName(color: Int): String {
        return colorNames[color] ?: "Інший"
    }

    fun show(context: Context, listener: OnColorSelected) {
        val colorValues = colorNames.keys.toTypedArray()
        val colorLabels = colorNames.values.toTypedArray()

        AlertDialog.Builder(context)
            .setTitle("Оберіть колір")
            .setItems(colorLabels) { _, which ->
                val selectedColor = colorValues[which]
                listener.onColor(selectedColor)
            }.show()
        }
}
