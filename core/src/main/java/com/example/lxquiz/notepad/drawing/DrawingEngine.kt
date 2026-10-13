package com.example.lxquiz.notepad.drawing

import android.view.MotionEvent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke as DrawStrokeStyle
import com.example.lxquiz.notepad.input.MotionEventProcessor

class DrawingEngine {

    val strokes: SnapshotStateList<Stroke> = mutableStateListOf()
    private val processor = MotionEventProcessor()

    var activeToolType: DrawingToolType = DrawingToolType.PEN
    var strokeColor: Color = Color(0xFF673AB7)
    var strokeWidth: Float = 8f

    private var activeStroke: Stroke? = null

    fun handleMotionEvent(event: MotionEvent): Boolean {
        val extractedPoints = processor.processMotionEvent(event)
        if (extractedPoints.isEmpty()) return false

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (activeToolType == DrawingToolType.ERASER) {
                    eraseAtPoints(extractedPoints)
                } else {
                    val newStroke = Stroke(
                        points = extractedPoints.toMutableList(),
                        color = strokeColor,
                        width = if (activeToolType == DrawingToolType.HIGHLIGHTER) strokeWidth * 2.5f else strokeWidth,
                        toolType = activeToolType,
                        alpha = if (activeToolType == DrawingToolType.HIGHLIGHTER) 0.35f else 1.0f
                    )
                    strokes.add(newStroke)
                    activeStroke = newStroke
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (activeToolType == DrawingToolType.ERASER) {
                    eraseAtPoints(extractedPoints)
                } else {
                    val current = activeStroke ?: strokes.lastOrNull()
                    if (current != null) {
                        current.points.addAll(extractedPoints)
                        if (strokes.isNotEmpty()) {
                            // Invalidate state to trigger recomposition
                            strokes[strokes.size - 1] = current.copy()
                        }
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                activeStroke = null
            }
        }
        return true
    }

    private fun eraseAtPoints(points: List<StrokePoint>) {
        val eraseRadius = strokeWidth * 3f
        strokes.removeAll { stroke ->
            stroke.points.any { p ->
                points.any { ep ->
                    val dx = p.x - ep.x
                    val dy = p.y - ep.y
                    (dx * dx + dy * dy) <= (eraseRadius * eraseRadius)
                }
            }
        }
    }

    fun clear() {
        strokes.clear()
        activeStroke = null
    }

    fun renderOnCanvas(drawScope: DrawScope) {
        for (stroke in strokes) {
            val points = stroke.points
            if (points.isEmpty()) continue

            val color = stroke.color.copy(alpha = stroke.alpha)

            if (points.size == 1) {
                val pt = points.first()
                drawScope.drawCircle(
                    color = color,
                    radius = stroke.width / 2f,
                    center = androidx.compose.ui.geometry.Offset(pt.x, pt.y)
                )
            } else {
                val path = StrokeSmoother.createSmoothPath(points)
                drawScope.drawPath(
                    path = path,
                    color = color,
                    style = DrawStrokeStyle(
                        width = stroke.width,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}
