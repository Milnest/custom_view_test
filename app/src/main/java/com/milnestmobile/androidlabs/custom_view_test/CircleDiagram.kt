package com.milnestmobile.androidlabs.custom_view_test

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.util.TypedValue


class CircleDiagram(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    val a: TypedArray = context!!.theme.obtainStyledAttributes(attrs, R.styleable.CircleDiagram, 0, 0)
    var value = a.getFloat(R.styleable.CircleDiagram_cd_value, 100F)
    var baseColor = a.getColor(R.styleable.CircleDiagram_cd_base_color, resources.getColor(R.color.colorDarkYellow))
    var baseColorLine = a.getColor(R.styleable.CircleDiagram_cd_base_color_line, resources.getColor(R.color.colorBlueGray_500))
    var secondColor = a.getColor(R.styleable.CircleDiagram_cd_second_color, resources.getColor(R.color.colorDarkGreen))
    var secondColorLine = a.getColor(R.styleable.CircleDiagram_cd_second_color_line, resources.getColor(R.color.colorBlueGray_900))
    var lineColor = a.getColor(R.styleable.CircleDiagram_cd_line_color, resources.getColor(R.color.colorBlack))
    var textColor = a.getColor(R.styleable.CircleDiagram_cd_text_color, resources.getColor(R.color.colorBlack))
    var textSize = a.getDimensionPixelSize(R.styleable.CircleDiagram_cd_text_size, 20)
    var h = a.getDimensionPixelSize(R.styleable.CircleDiagram_cd_h, 0)
    var rectBig = RectF()
    var rectSmall = RectF()
    var size = height
    var x_center = width / 2
    var y_center = height / 2
    var left = 0f
    var top = 0f
    var right = 0f
    var bottom = 0f
    val dip: Float = 1f
    var px: Float = 1f
    val paint = Paint()
    val text = (value * 100).toString() + "%"
    val textBounds = Rect()
    var textWidth = 0f
    var textHeight = 0f
    val textPaint: Paint
        get() {
            val textPaint = Paint()
            textPaint.isAntiAlias = true
            textPaint.color = textColor
            textPaint.textSize = textSize.toFloat()/*35.0f*/
            textPaint.strokeWidth = 2.0f
            textPaint.style = Paint.Style.STROKE
            textPaint.setShadowLayer(5.0f, 10.0f, 10.0f, textColor)
            return textPaint
        }

    init {
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /*val width = this.width - paddingStart - paddingEnd
        val height = this.height - paddingTop - paddingBottom*/
        x_center = width / 2
        y_center = height / 2
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
        if (height < width) {
            size = height //size/2 = radius
            left = x_center - size / 2.toFloat() + paddingStart + px
            top = paddingTop.toFloat() + px
            right = x_center + size / 2.toFloat() - paddingEnd - px
            bottom = height.toFloat() - paddingBottom - px
            rectBig.set(left, top, right, bottom)
            rectSmall.set(h + left, h + top, right - h, bottom - h)

        } else {
            size = width
            left = paddingStart.toFloat() + px
            top = y_center - size / 2 + paddingTop.toFloat() + px
            right = width - paddingEnd.toFloat() - px
            bottom = y_center + size / 2 - paddingBottom.toFloat() - px
            rectBig.set(left, top, right, bottom)
            rectSmall.set(h + left, h + top, right - h, bottom - h)
        }
        //textBounds.set(rectSmall.left.toInt(), rectSmall.top.toInt(), rectSmall.right.toInt(), rectSmall.bottom.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = baseColorLine
        canvas!!.drawOval(rectBig.left - px, rectBig.top - px, rectBig.right + px, rectBig.bottom + px, paint)
        paint.style = Paint.Style.FILL
        paint.color = baseColor
        canvas.drawOval(rectBig, paint)
        //canvas!!.drawCircle(x_center.toFloat(), y_center.toFloat(), radiusBig.toFloat(), paint)
        paint.style = Paint.Style.FILL
        paint.color = lineColor
        canvas.drawArc(rectBig, -90f, (value * 100 * 3.6F), true, paint)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = secondColorLine
        canvas.drawOval(rectSmall.left - px, rectSmall.top - px, rectSmall.right + px, rectSmall.bottom + px, paint)
        paint.color = secondColor
        canvas.drawOval(rectSmall, paint)

        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textWidth = textPaint.measureText(text);
        textHeight = textBounds.height().toFloat();
        if(textBounds.height() <= size && textBounds.width() <= size) canvas.drawText(text, x_center - textWidth/2, y_center + textHeight/2, textPaint)
        /*else{
            textPaint.textSize = 10f
            textPaint.getTextBounds(text, 0, text.length, textBounds)
//            textBounds.set(rectSmall.left.toInt(), rectSmall.top.toInt(), rectSmall.right.toInt(), rectSmall.bottom.toInt())
            canvas.drawText(text, x_center - textWidth/2, y_center + textHeight/2, textPaint)
        }*/
    }
}