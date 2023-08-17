package com.github.goldy1992.mp3player.client.ui.components.equalizer.circular

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import org.apache.commons.math3.complex.Complex
import kotlin.math.pow
import kotlin.math.sqrt

data class CubicBezierPoint(
    var start: Complex,
    var controlPoint1: Complex,
    var controlPoint2: Complex,
    var end: Complex,

    )

private fun normalized(x: Complex) : Complex {
    return x.divide(x.abs())
}

fun autosmoothHandles(a: Complex, b: Complex, c: Complex, alpha : Double=1.0/3) : Pair<Complex, Complex> {
    """
    Computes the autosmooth handles for point `b` with `a` previuos and `c` next
    """
    val v_next = c.subtract(b)
    val v_prev = a.subtract(b)

    val l_next = v_next.abs()
    val l_prev = v_prev.abs()

    val u = normalized(v_next.subtract(v_prev).multiply((l_prev / l_next)))
    return Pair(
        u.multiply(-1.0).multiply(alpha * l_prev),
        u.multiply(alpha * l_next),
    )
}

/**
 *     Create a series of cubic bezier curves that pass smoothly through `points`
 *     This is the same method used in inkscape with auto-smooth node types
 *     Handles are oriented perpindicular to the angle bisector between prev, cur, next points
 *     Handles are alpha=1/3 the length of the distance between prev & cur (and cur & next respectively)
 */
fun autosmooth(points: List<Complex>, alpha: Double=1.0/3) : List<CubicBezierPoint> {


    if (points.size <= 2) {
        throw Exception("Need more than 2 points")
    }
    val toReturn = mutableListOf<CubicBezierPoint>()

    for ( i in 0 until(points.size - 1)) {
        toReturn.add(CubicBezierPoint(points[i], points[i], points[i + 1], points[i + 1]))
    }
    for ( i in 1 until(points.size - 1)) {
        val pairCompl : Pair<Complex, Complex> = autosmoothHandles(points[i-1], points[i], points[i+1], alpha = alpha)
        val u = pairCompl.first
        val v =  pairCompl.second
        toReturn[i - 1].controlPoint2 =  toReturn[i - 1].controlPoint2.add( u)
        toReturn[i].controlPoint1 =  toReturn[i - 1].controlPoint2.add( v)
    }

    // handle closed shape
    if( points[0].equals(points[points.size-1])) {
        val pairCompl : Pair<Complex, Complex> = autosmoothHandles(points[points.size-1], points[0], points[1])
        val u = pairCompl.first
        val v =  pairCompl.second
        toReturn[toReturn.size -1].controlPoint2 =  toReturn[toReturn.size -1].controlPoint2.add(u)
        toReturn[0].controlPoint1 = toReturn[0].controlPoint1.add(v)
    }
    return toReturn
}

@Preview
@Composable
fun PreviewAutoSmooth() {

    // use x^2 + y^2 = r^2
    val offset = 300f
    val r = 200f
    val rSq = r.pow(2)
    val complexPoints = mutableListOf<Complex>()
    val negativeComplexPoints = mutableListOf<Complex>()

    for (n in -200 .. 200 step 20) {
        // y = sqrt(r^2 - x^2)
        val x = n + offset
        val y = sqrt(rSq - (n.toFloat().pow(2))) + offset
        complexPoints.add(Complex(x.toDouble(), y.toDouble()))
        negativeComplexPoints.add(Complex(n.toDouble(), (y * -1f).toDouble()))
    }
    val res : List<CubicBezierPoint> = autosmooth(complexPoints)

    Canvas(Modifier.fillMaxSize()) {
        val path = Path().apply {

            moveTo(res.get(0).start.real.toFloat(), res.get(0).start.imaginary.toFloat() )
            for (i in res) {

                val cp1 = i.controlPoint1
                val cp2 = i.controlPoint2
                val end = i.end
                cubicTo(cp1.real.toFloat(),
                        cp1.imaginary.toFloat(),
                        cp2.real.toFloat(),
                        cp2.imaginary.toFloat(),
                        end.real.toFloat(),
                        end.imaginary.toFloat())
            }
        }

        drawLine(Color.Red, start = Offset(0f, 0f), end = Offset(size.width, size.height))
        drawPath(path, Color.Red,  style = Stroke(
            width = 5f,
            cap = StrokeCap.Round
        )
        )
    }
}

