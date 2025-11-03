package com.oop_kpi.lab3

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.oop_kpi.drawing.DrawingView
import com.oop_kpi.drawing.Shape
import com.oop_kpi.ui_components.ColorDialog
import com.oop_kpi.ui_components.ShapeDialog

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var colorLabel: TextView

    private val colorDialog = ColorDialog()
    private val shapeDialog = ShapeDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        colorLabel = findViewById(R.id.color_label)

        val colorButton: ImageButton = findViewById(R.id.color_button)
        val shapeButton: ImageButton = findViewById(R.id.ellipse_button)
        val eraseButton: ImageButton = findViewById(R.id.erase_button)

        colorButton.setOnClickListener {
            colorDialog.show(this, object : ColorDialog.OnColorSelected {
                override fun onColor(color: Int) {
                    drawingView.setColor(color)
                    colorLabel.text = colorDialog.getColorName(color)
                }
            })
        }

        shapeButton.setOnClickListener {
            shapeDialog.show(this, object : ShapeDialog.OnShapeSelected {
                override fun onShape(factory: (Float, Float, Float, Float, Int) -> Shape) {
                    drawingView.setShape(factory)
                }
            })
        }

        eraseButton.setOnClickListener {
            drawingView.clearCanvas()
        }
    }
}
