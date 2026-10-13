package com.example.lxquiz.notepad.drawing

import androidx.compose.ui.graphics.Path

object StrokeSmoother {

    /**
     * Converts a list of raw Touch Points into a smooth Path using Quadratic Bézier Curves.
     */
    fun createSmoothPath(points: List<StrokePoint>): Path {
        val path = Path()
        if (points.isEmpty()) return path

        if (points.size == 1) {
            val pt = points.first()
            path.moveTo(pt.x, pt.y)
            path.lineTo(pt.x + 0.1f, pt.y + 0.1f)
            return path
        }

        path.moveTo(points[0].x, points[0].y)

        if (points.size == 2) {
            path.lineTo(points[1].x, points[1].y)
            return path
        }

        // Quadratic Bézier curve interpolation through midpoints
        for (i in 1 until points.size - 1) {
            val p0 = points[i - 1]
            val p1 = points[i]
            val p2 = points[i + 1]

            val mid1X = (p0.x + p1.x) / 2f
            val mid1Y = (p0.y + p1.y) / 2f
            val mid2X = (p1.x + p2.x) / 2f
            val mid2Y = (p1.y + p2.y) / 2f

            if (i == 1) {
                path.lineTo(mid1X, mid1Y)
            }
            path.quadraticTo(p1.x, p1.y, mid2X, mid2Y)
        }

        val lastPoint = points.last()
        path.lineTo(lastPoint.x, lastPoint.y)

        return path
    }
}
