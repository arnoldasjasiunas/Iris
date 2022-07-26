package com.example.irisrouter.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.annotation.ColorInt
import kotlin.math.min

class SquarePaintable(
    @ColorInt val color: Int
): Paintable {
    // don't init on onDraw
    val paint = Paint().apply {
        color = this@SquarePaintable.color
    }
    override fun paint(canvas: Canvas) {
        val cx = canvas.width / 2.0F
        val cy = canvas.height / 2.0F
        val radius = min(cx, cy)
        val rect = RectF(cx - radius, cy - radius, cx + radius, cy + radius)
        canvas.drawRect(rect, paint)
    }
}