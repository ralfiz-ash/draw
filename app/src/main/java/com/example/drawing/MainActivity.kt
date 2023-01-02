package com.example.drawing

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.drawing.databinding.ActivityMainBinding
import kotlin.math.cos
import kotlin.math.sin


val Context.dp: Float
    get() = resources.displayMetrics.density

val View.dp: Float
    get() = context.dp

val Fragment.dp: Float
    get() = requireContext().dp

var viewValue: Int = 0
public var angle: Float = 0f

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root);

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                angle = seekBar.progress.toFloat()

                binding.sunPath.setProgress(angle.toDouble())
                //Toast.makeText(applicationContext, "$angle", Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //Toast.makeText(applicationContext, "seekbar touch stopped!", Toast.LENGTH_SHORT).show()
            }
        })

    }
}

class CustomView : View {
    private var width = 0f
    private var height = 0f
    private val paint: Paint = Paint()
    private val padding = 100f
    private var originStart = padding + 50f
    private var originEnd = 0f
    private var degrees = angle

    //var degrees = 20.0
    private var radians: Double = 0.0

    // private var radians:Double= Math.toRadians(degrees.toDouble())
    //var radians:Double= Math.toRadians(degrees)
    var mainArcRadius = 400f
    var movingPointX = 0f
    var movingPointY = 0f

    init {
        // create the Paint and set its color
        paint.color = Color.parseColor("#D6D6D6")
        paint.strokeWidth = dp * 2
        paint.style = Paint.Style.FILL

    }


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context?, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    protected override fun onDraw(canvas: Canvas) {
        var circlePaint = Paint()
        circlePaint.color = Color.parseColor("#0095DA")
        paint.style = Paint.Style.FILL
        var blackPaint = Paint()
        blackPaint.color = Color.parseColor("#000000")
        paint.style = Paint.Style.FILL
        val w: Int = Resources.getSystem().displayMetrics.widthPixels
        val h: Int = Resources.getSystem().displayMetrics.heightPixels
        originEnd = w - originStart
        //movingPointY = 0f
        var lineView = (h / 2f)
        val path = Path()
        val pathPaint = Paint().apply {
            color = Color.parseColor("#300095DA")
            isAntiAlias = true
        }
        canvas.drawColor(Color.WHITE)
//        canvas.drawLine(padding.toFloat(), lineView, w - padding.toFloat(), lineView, paint)
//        canvas.drawCircle(originStart, lineView, 20f, circlePaint)
//        canvas.drawCircle(originEnd, lineView, 20f, circlePaint)

//Dotted Arc line Drawing
        val dottedLine = Paint()
        dottedLine.color = Color.parseColor("#0095DA")
        val dashPath = DashPathEffect(floatArrayOf(20f, 20f), 10.0.toFloat())
        dottedLine.pathEffect = dashPath
        dottedLine.style = Paint.Style.STROKE
        dottedLine.strokeWidth = 5f
        invalidate()
        val rect = RectF()
        rect.set(originStart, lineView - mainArcRadius, originEnd, lineView + mainArcRadius)
        canvas.drawArc(rect, 180f, 180f, false, dottedLine)

        //setting radian values
        radians = Math.toRadians(degrees.toDouble())

//finding triangle sides
        val bc = sin(radians) * mainArcRadius
        val ab = cos(radians) * mainArcRadius
        var arcCenter: Float = ((originStart) + originEnd) / 2f
       // canvas.drawCircle(arcCenter, lineView, 20f, paint)

        val movingX = (arcCenter - ab)
        val movingY = (lineView - bc)

//        if (degrees > 90.0) {
//            canvas.drawCircle(movingX.toFloat(), lineView, 20f, paint)//xpoint
//            canvas.drawCircle(movingX.toFloat(), movingY.toFloat(), 20f, paint)//y point
//        } else {
//            canvas.drawCircle(movingX.toFloat(), lineView, 20f, paint)//xpoint
//            canvas.drawCircle(movingX.toFloat() + 10f, movingY.toFloat(), 20f, paint)   //ypoint
//        }
        /*val finalDraw = Path().apply {
            moveTo(originStart, lineView)
            if (degrees > 90.0) {
                arcTo(
                    originStart,
                    lineView - mainArcRadius,
                    originEnd,
                    lineView + mainArcRadius,
                    180f,
                    (degrees + 1f).toFloat(),
                    false
                )
                lineTo(movingX.toFloat(), lineView)
                //Log.d("angles", "onangles: $angle//$radians||top-${lineView+mainArcRadius}bottom-${h/2f}")
            } else {
                arcTo(
                    originStart,
                    lineView - mainArcRadius,
                    originEnd,
                    lineView + mainArcRadius,
                    180f,
                    degrees,
                    false
                )
                lineTo(movingX.toFloat(), lineView)
            }
        }*/
        path.close()
       // canvas.drawPath(finalDraw, pathPaint)
        //drawing bitmap
        val res = resources
        val bitmap = BitmapFactory.decodeResource(res, R.drawable.sun_image)
        if (degrees > 90.0) {
            canvas.translate(movingX.toFloat(), movingY.toFloat())
            canvas.save()
            canvas.drawBitmap(bitmap, 0f - bitmap.width / 2f, 0f - bitmap.height / 2f, paint)
            canvas.restore()
        } else {
            canvas.translate(movingX.toFloat() + 10f, movingY.toFloat())
            canvas.save()
            canvas.drawBitmap(bitmap, 0f - bitmap.width / 2f, 0f - bitmap.height / 2f, paint)
            canvas.restore()
        }
        invalidate()
        Log.d("@", "onDraw: $angle")
        Log.d("size", "size: ${bitmap.width}||${bitmap.height}")


        /*var ab: Double = sin(radians) * mainArcRadius
        var ac: Double = cos(radians) * mainArcRadius
        var arcCenter: Float = ((originStart) + w-( originEnd)) / 2f
        movingPointX = arcCenter-ac.toFloat()
        //movingPointX = firstHalf//y=h/2f of A
        movingPointY = (lineView) - ab.toFloat()
        canvas.drawCircle(arcCenter, lineView, 20f, paint) //center of arc
        if(degrees<90){
            canvas.drawCircle((movingPointX), lineView, 10f, paint)//x Point
            canvas.drawCircle(movingPointX+5f, movingPointY-5f, 10f, paint)//y Point on top
        }
        else
        {
            canvas.drawCircle((movingPointX)-10f, lineView, 10f, paint)//x Point
            canvas.drawCircle(movingPointX, movingPointY+5f, 10f, paint)//y Point on top
        }


        path.moveTo(originStart.toFloat(), lineView)//initial point

        val res = resources
        val bitmap = BitmapFactory.decodeResource(res, R.drawable.sun_image)
        //canvas?.drawBitmap(bitmap, firstHalf-50f,movingPointY-50f, paint)
        if(degrees<90){
            path.arcTo(
                originStart.toFloat(),
                (lineView)-mainArcRadius,
                w - (originEnd.toFloat()),
                (lineView) + mainArcRadius,
                180f,
                degrees.toFloat()+0.9f,
                false
            )
        }
        else
        {
            path.arcTo(
                originStart.toFloat(),
                (lineView)-mainArcRadius,
                w - (originEnd.toFloat()),
                (lineView) + mainArcRadius,
                180f,
                degrees.toFloat(),
                true
            )
        }
        path.arcTo(
            originStart.toFloat(),
            (lineView)-mainArcRadius,
            w - (originEnd.toFloat()),
            (lineView) + mainArcRadius,
            180f,
            degrees.toFloat(),
            false
        )
        path.lineTo(movingPointX-10f, lineView)
        //path.lineTo(firstHalf, movingPointY)
        path.close()
        canvas?.drawPath(path, pathPaint)//drawing triangles*/
        /*//seperate triangle with line test
        path.moveTo(padding+50f, h / 2f)
        path.lineTo(firstHalf, h/2f)
        path.lineTo(firstHalf, movingPointY-10f)
        path.close()
        canvas?.drawPath(path,pathPaint)*/
        /* path.moveTo(firstHalf, h / 2f)
         path.lineTo(firstHalf, movingPointY)
         path.lineTo(padding + 50f, h / 2f)
         path.moveTo(padding+50f,h/2f)
         path.arcTo(
             padding + 50,
             (h/2f)-mainArcRadius,
             w - padding - 50,
             (h / 2f) + mainArcRadius,
             180f,
             degrees.toFloat(),
             false
         )

         //path.lineTo(padding+50f,h/2f)
         path.close()
         canvas?.drawPath(path, pathPaint)//drawing triangles*/

    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = Resources.getSystem().displayMetrics.widthPixels;
        val h = Resources.getSystem().displayMetrics.heightPixels;
        this.width = w.toFloat()
        this.height = h.toFloat()
        Log.d("@", "width-${w}//height-${h}")

    }
    /*fun dpToPx(dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }*/

   /* fun setProgress(PROGRESS_VALUE: Double) {
        angleInDegrees=PROGRESS_VALUE
        radianValue = Math.toRadians(PROGRESS_VALUE)
        invalidate()
    }*/
}
