package com.example.lxquiz.notepad.input

import android.view.MotionEvent
import com.example.lxquiz.notepad.drawing.StrokePoint

class MotionEventProcessor {

    private var activeToolType: Int = MotionEvent.TOOL_TYPE_UNKNOWN

    /**
     * Extracts raw and historical touch samples from MotionEvent.
     */
    fun processMotionEvent(event: MotionEvent): List<StrokePoint> {
        val extractedPoints = mutableListOf<StrokePoint>()
        val pointerCount = event.pointerCount

        if (pointerCount == 0) return extractedPoints

        // Detect primary pointer tool type
        val primaryTool = event.getToolType(0)
        if (primaryTool == MotionEvent.TOOL_TYPE_STYLUS || primaryTool == MotionEvent.TOOL_TYPE_ERASER) {
            activeToolType = primaryTool
        }

        // Palm Rejection: If stylus is active, ignore multi-touch palm contacts (pointerCount > 1)
        if (activeToolType == MotionEvent.TOOL_TYPE_STYLUS && pointerCount > 1) {
            val major = event.touchMajor
            if (major > 60f) {
                // Reject palm touch
                return extractedPoints
            }
        }

        // 1. Process Historical Touch Samples (delivered between display refresh frames)
        val historySize = event.historySize
        for (i in 0 until historySize) {
            val hX = event.getHistoricalX(0, i)
            val hY = event.getHistoricalY(0, i)
            val hPressure = try { event.getHistoricalPressure(0, i) } catch (_: Exception) { 1.0f }
            val hTime = event.getHistoricalEventTime(i)

            extractedPoints.add(
                StrokePoint(
                    x = hX,
                    y = hY,
                    pressure = if (hPressure > 0f) hPressure else 1.0f,
                    timestamp = hTime
                )
            )
        }

        // 2. Process Current Touch Sample
        val cX = event.x
        val cY = event.y
        val cPressure = try { event.pressure } catch (_: Exception) { 1.0f }
        val cTime = event.eventTime

        extractedPoints.add(
            StrokePoint(
                x = cX,
                y = cY,
                pressure = if (cPressure > 0f) cPressure else 1.0f,
                timestamp = cTime
            )
        )

        return extractedPoints
    }
}
