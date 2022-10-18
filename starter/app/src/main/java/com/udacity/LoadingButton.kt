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


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {

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

    private fun drawButton(canvas: Canvas){
        drawRect(canvas)
        drawText(canvas, "Download")
    }

    private fun drawRect(canvas: Canvas){
        paint.color = resources.getColor(R.color.button_bg, context.theme)
        canvas.drawRect(
            RectF(
                0f, 0f, widthSize, heightSize
        ), paint)
    }

    private fun drawText(canvas: Canvas, text: String){
        paint.color = resources.getColor(R.color.button_txt_color, context.theme)
        canvas.drawText(text, widthSize/2, heightSize/2 - ((paint.descent() + paint.ascent()) / 2) , paint)
    }

}