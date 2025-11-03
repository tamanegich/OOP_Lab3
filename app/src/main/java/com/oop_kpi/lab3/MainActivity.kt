package com.oop_kpi.lab3

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.oop_kpi.drawing.*
import com.oop_kpi.ui_components.ColorDialog

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView

    private val colorDialog = ColorDialog()
    private lateinit var shapeButtons: List<ImageButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)

        val ellipseButton: ImageButton = findViewById(R.id.ellipse_button)
        val rectangleButton: ImageButton = findViewById(R.id.rectangle_button)
        val circleButton: ImageButton = findViewById(R.id.circle_button)
        val lineButton: ImageButton = findViewById(R.id.line_button)
        val colorButton: ImageButton = findViewById(R.id.color_button)
        val eraseButton: ImageButton = findViewById(R.id.erase_button)

        val undoButton: ImageButton = findViewById(R.id.undo_button)
        val redoButton: ImageButton = findViewById(R.id.redo_button)

        undoButton.setOnClickListener { drawingView.undo() }
        redoButton.setOnClickListener { drawingView.redo() }

        shapeButtons = listOf(ellipseButton, rectangleButton, circleButton, lineButton)

        highlightActiveButton(ellipseButton)

        colorButton.setOnClickListener {
            colorDialog.show(this, object : ColorDialog.OnColorSelected {
                override fun onColor(color: Int) {
                    drawingView.setColor(color)
                    colorButton.setColorFilter(color)
                    if (color == Color.TRANSPARENT) {
                        colorButton.setImageResource(R.drawable.ic_color_transparent)
                        colorButton.colorFilter = null
                    } else {
                        colorButton.setImageResource(R.drawable.ic_color)
                        colorButton.setColorFilter(color)
                    }
                }
            })
        }

        ellipseButton.setOnClickListener {
            drawingView.setShape(::Ellipse)
            highlightActiveButton(ellipseButton)
        }

        rectangleButton.setOnClickListener {
            drawingView.setShape(::Rectangle)
            highlightActiveButton(rectangleButton)
        }

        circleButton.setOnClickListener {
            drawingView.setShape(::Circle)
            highlightActiveButton(circleButton)
        }

        lineButton.setOnClickListener {
            drawingView.setShape(::Line)
            highlightActiveButton(lineButton)
        }

        eraseButton.setOnClickListener {
            drawingView.clearCanvas()
        }
    }

    private fun highlightActiveButton(activeButton: ImageButton) {
        for (button in shapeButtons) {
            button.colorFilter = null
        }
        activeButton.setColorFilter(getColor(android.R.color.white))
    }
}
