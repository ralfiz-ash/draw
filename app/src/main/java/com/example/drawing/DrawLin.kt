package com.example.drawing

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowInsets

class DrawSunPath : View {
    private val paintGray = Paint()
    private val paintBlue = Paint()
    private val paintBlueLight = Paint()
    private var radius = 0f
    private var width: Float = 0f
    private var height: Float = 0f
    private val padding = 100f
    private var originStart = padding + 50f
    private var originEnd = 0f
    var angleInDegrees: Double = angle.toDouble()
    var radianValue: Double = Math.toRadians(angleInDegrees)


    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setProgress(angleInDegrees)
        canvas.drawColor(Color.WHITE)
        canvas.drawLine((width / 2) - radius - 50, (height / 2) , (width / 2) + radius + 50, height / 2, paintGray)
        canvas.drawCircle((width / 2) - radius,(height / 2),20f,paintBlue)
        canvas.drawCircle((width / 2) + radius,(height / 2),20f,paintBlue)

        val paintB = Paint()
        paintB.color = Color.parseColor("#0095DA")
        val dashPath = DashPathEffect(floatArrayOf(20f, 20f), 10.0.toFloat())
        paintB.pathEffect = dashPath
        paintB.strokeWidth = 5f
        paintB.style = Paint.Style.STROKE
        invalidate()
        val rect2 = RectF()
        rect2.set(
            (width / 2) - radius,
            (height / 2) - radius,
            (width / 2) + radius,
            (height / 2) + radius
        )

        //  canvas!!.drawRect(rect2,paintB)
        canvas!!.drawArc(rect2, 180f, 180f, false, paintB)

        val bc = kotlin.math.sin(radianValue) * radius
        val ab = kotlin.math.cos(radianValue) * radius

        val movingX = ((width / 2) - ab)
        val movingY = ((height / 2) - bc)
        //canvas.drawCircle(movingX.toFloat(), height / 2, 20f, paintGray)//X point

        val movingY_Adjusted: Float = movingY.toFloat()
       /* if (radianValue > 90.0) {
            movingY_Adjusted = movingY.toFloat() - 10f
           // canvas.drawCircle(movingX.toFloat(), movingY.toFloat() - 10f, 20f, paintBlue)
            //  canvas.drawBitmap(bitmapImage!!, movingX.toFloat(), movingY.toFloat()-10f , null)

        } else {
            movingY_Adjusted = movingY.toFloat()
            //canvas.drawCircle(movingX.toFloat(), movingY.toFloat(), 20f, paintBlue)
//            canvas.drawBitmap(bitmapImage!!, movingX.toFloat()-20f, movingY.toFloat()-30f , null)

        }*/


        val curvePath2 = Path().apply {
            moveTo((width / 2) - radius, height / 2)
            if (radianValue > 90.0)
                arcTo(
                    (width / 2) - radius,
                    (height / 2) - radius,
                    (width / 2) + radius,
                    (height / 2) + radius,
                    180f,
                    (angleInDegrees - 0.9).toFloat(),
                    false
                )
            else
                arcTo(
                    (width / 2) - radius,
                    (height / 2) - radius,
                    (width / 2) + radius,
                    (height / 2) + radius,
                    180f,
                    angleInDegrees.toFloat(),
                    false
                )
            lineTo(movingX.toFloat(), height / 2)

            close()
        }
        canvas.drawPath(curvePath2, paintBlueLight)
       // canvas.drawCircle(movingX.toFloat(), movingY_Adjusted, 26f, paintBlue)
        //drawing bitmap
        val res = resources
        val bitmap = BitmapFactory.decodeResource(res, R.drawable.sun_image)
        canvas.translate(movingX.toFloat(), movingY_Adjusted.toFloat())
        canvas.save()
        canvas.drawBitmap(bitmap, 0f - bitmap.width / 2f, 0f - bitmap.height / 2f, paintBlue)
        canvas.restore()
        invalidate()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getScreenWidth(context as MainActivity),
            getScreenHeight(context as MainActivity)
        )
    }

    init {

        width = (getScreenWidth(context as MainActivity)).toFloat()
        Log.v("screenWidth", width.toString())
        height = getScreenHeight(context as MainActivity).toFloat()
        radius = (width - 200) / 2
        paintGray.color = Color.parseColor("#595958")
        paintGray.strokeWidth = 3f
        paintGray.isAntiAlias = true
        paintBlue.color = Color.parseColor("#0095DA")
        paintBlue.strokeWidth = 10f
        paintBlue.isAntiAlias = true
        paintBlueLight.color = Color.parseColor("#AEDEF4")
        paintBlueLight.style = Paint.Style.FILL
        paintBlueLight.strokeWidth = 6f
        paintBlueLight.isAntiAlias = true
    }

    private fun getScreenWidth(activity: MainActivity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    private fun getScreenHeight(activity: MainActivity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    private fun getDrawable(drawable: Drawable): Bitmap? {
        return try {
            val bitmap: Bitmap
            bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: OutOfMemoryError) {
// Handle the error
            null
        }
    }

    fun setProgress(PROGRESS_VALUE: Double) {
        angleInDegrees=PROGRESS_VALUE
        radianValue = Math.toRadians(PROGRESS_VALUE)
        invalidate()
    }
}