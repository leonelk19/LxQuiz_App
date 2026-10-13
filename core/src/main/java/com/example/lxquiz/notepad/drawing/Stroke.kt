package com.example.lxquiz.notepad.drawing

import androidx.compose.ui.graphics.Color

enum class DrawingToolType {
    PEN,
    HIGHLIGHTER,
    ERASER
}

data class StrokePoint(
    val x: Float,
    val y: Float,
    val pressure: Float = 1.0f,
    val timestamp: Long = System.currentTimeMillis()
)

data class Stroke(
    val points: MutableList<StrokePoint> = mutableListOf(),
    val color: Color = Color(0xFF673AB7),
    val width: Float = 8f,
    val toolType: DrawingToolType = DrawingToolType.PEN,
    val alpha: Float = 1.0f
)
