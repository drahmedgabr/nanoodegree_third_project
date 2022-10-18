package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0f
    private var heightSize = 0f
    private var progress = 0f
    private var centerY = 0f
    private var indicatorLoc = 0f


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {
        isClickable = true
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0, 0
        ).apply {
            try {
                progress = getFloat(R.styleable.LoadingButton_progress, 0f)
            } finally {
                recycle()
            }
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        progress += 10
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            drawButton(canvas)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w.toFloat()
        heightSize = h.toFloat()
        setMeasuredDimension(w, h)
    }

    private fun drawButton(canvas: Canvas) {

        centerY = heightSize / 2 - ((paint.descent() + paint.ascent()) / 2)

        drawRect(canvas, resources.getColor(R.color.button_bg, context.theme))
        if (progress > 0) {
            drawRect(canvas, resources.getColor(R.color.anim_button_bg, context.theme), progress)
            drawText(canvas, resources.getString(R.string.button_loading))
            drawIndicator(canvas)
        } else {
            drawText(canvas, resources.getString(R.string.button_name))
        }
    }

    private fun drawRect(canvas: Canvas, color: Int, progress: Float = widthSize) {
        paint.color = color
        val progressW = (progress / 100) * widthSize
        canvas.drawRect(
            RectF(
                0f, 0f, progressW, heightSize
            ), paint
        )
    }

    private fun drawText(canvas: Canvas, text: String) {
        paint.color = resources.getColor(R.color.button_txt_color, context.theme)
        canvas.drawText(
            text,
            widthSize / 2,
            centerY,
            paint
        )
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        indicatorLoc = (widthSize / 2) + ( bounds.width() / 2 ) + 50
    }

    private fun drawIndicator(canvas: Canvas){
        paint.color = resources.getColor(R.color.loading_indicator_color, context.theme)
        val angle = (progress / 100) * 360
        val top = (heightSize / 2) - 40
        val bottom = (heightSize / 2) + 40
        canvas.drawArc(RectF(indicatorLoc, top, indicatorLoc + 80, bottom), 0f, angle, true, paint)
    }

}