package fr.atraore.edl.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class LoadingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }

    private val housePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private val housePath = Path()

    private var progress = 0f

    init {
        startAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dessiner la maison
        canvas.drawPath(housePath, housePaint)

        // Dessiner les blocs de remplissage
        val fillHeight = height * progress
        canvas.drawRect(0f, fillHeight, width.toFloat(), height.toFloat(), paint)
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener {
            progress = it.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Créer le chemin pour dessiner la maison
        housePath.moveTo(width * 0.5f, height * 0.1f)
        housePath.lineTo(width * 0.9f, height * 0.4f)
        housePath.lineTo(width * 0.9f, height * 0.8f)
        housePath.lineTo(width * 0.5f, height * 0.5f)
        housePath.lineTo(width * 0.1f, height * 0.8f)
        housePath.lineTo(width * 0.1f, height * 0.4f)
        housePath.close()
    }
}
