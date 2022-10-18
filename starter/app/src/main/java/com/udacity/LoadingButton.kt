package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
    private var buttonColor = 0
    private var animatedButtonColor = 0
    private var textColor = 0
    private var progressIndicatorColor = 0


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private val valueAnimator = ValueAnimator.ofFloat(1f, 100f)

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        if(new == ButtonState.Clicked){
            drawAnimation()
        }
    }



    init {
        isClickable = true
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0, 0
        ).apply {
            try {
                buttonColor = getInt(R.styleable.LoadingButton_bgColor, 0)
                animatedButtonColor = getInt(R.styleable.LoadingButton_animatedBtnColor, 0)
                textColor = getInt(R.styleable.LoadingButton_textColor, 0)
                progressIndicatorColor = getInt(R.styleable.LoadingButton_progressIndicatorColor, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        if(buttonState != ButtonState.Clicked){
            buttonState = ButtonState.Clicked
        }
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

    private fun drawAnimation(){
        valueAnimator.duration = 10000
        valueAnimator.addUpdateListener(object: ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(p0: ValueAnimator) {
                progress = p0.animatedValue as Float
                invalidate()
            }
        })
        valueAnimator.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                buttonState = ButtonState.Completed
            }
        })
        valueAnimator.start()
    }

    private fun drawButton(canvas: Canvas) {

        centerY = heightSize / 2 - ((paint.descent() + paint.ascent()) / 2)

        drawRect(canvas, buttonColor)
        if (buttonState == ButtonState.Clicked) {
            drawRect(canvas, animatedButtonColor, progress)
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
        paint.color = textColor
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
        paint.color = progressIndicatorColor
        val angle = (progress / 100) * 360
        val top = (heightSize / 2) - 40
        val bottom = (heightSize / 2) + 40
        canvas.drawArc(RectF(indicatorLoc, top, indicatorLoc + 80, bottom), 0f, angle, true, paint)
    }

}