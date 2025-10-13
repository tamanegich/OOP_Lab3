package com.oop_kpi.lab21

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.oop_kpi.drawing.DrawingView
import com.oop_kpi.ui_components.ColorDialog
import com.oop_kpi.ui_components.ShapeDialog
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private val colorDialog = ColorDialog()
    private val shapeDialog = ShapeDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_color -> {
                    colorDialog.show(this, object : ColorDialog.OnColorSelected {
                        override fun onColor(color: Int) {
                            drawingView.setColor(color)
                        }
                    })
                    true
                }
                R.id.navigation_shape -> {
                    shapeDialog.show(this, object : ShapeDialog.OnShapeSelected {
                        override fun onShape(factory: (Float, Float, Float, Float, Int) -> com.oop_kpi.drawing.Shape) {
                            drawingView.setShape(factory)
                        }
                    })
                    true
                }
                R.id.navigation_erase -> {
                    drawingView.clearCanvas()
                    true
                }
                else -> false
            }
        }
    }
}
