import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.MatrixUtils
import org.jetbrains.kotlinx.multik.api.d1array
import org.jetbrains.kotlinx.multik.api.identity
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.api.toNDArray
import org.jetbrains.kotlinx.multik.ndarray.data.D1
import org.jetbrains.kotlinx.multik.ndarray.data.D2
import org.jetbrains.kotlinx.multik.ndarray.data.MutableMultiArray
import org.jetbrains.kotlinx.multik.ndarray.data.NDArray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import org.jetbrains.kotlinx.multik.ndarray.operations.toMutableList
import kotlin.math.pow
import kotlin.math.sqrt


fun curveApacheMath() {
    var idt = MatrixUtils.createRealIdentityMatrix(4);
    idt = idt.scalarMultiply(4.0)

}

fun curveMultik(points: List<NDArray<Double, D1>>) {
    val identity : MutableMultiArray<Double, D2> = mk.identity<Double>(4).times(4.0)
    //val res = scalar.times(identity.asD2Array())
    val coefficientMatrix = fillDiagonal(identity)
    println(identity)
    //mk.linalg.solve(coefficientMatrix, )
    fillPointsVector(points)
}


private fun fillDiagonal(sqMatrix: MutableMultiArray<Double, D2> ) : MutableMultiArray<Double, D2> {
    val length  = sqMatrix.shape[0]
    val lastElement  = length - 1
    for (n in 0 .. lastElement) {
        val currentRow = sqMatrix[n].toMutableList()

        if (n == 0) {
            currentRow[0] = 2.0
        }

        val idxLeft = n-1
        if (idxLeft in 0 .. lastElement) {
            currentRow[idxLeft] = 1.0
        }
        val idxRight = n + 1
        if (idxRight in 0 .. lastElement) {
            currentRow[idxRight] = 1.0
        }

        else if (n == lastElement) {
            currentRow[lastElement] = 7.0
            currentRow[lastElement - 1] = 2.0

        }

        sqMatrix[n] = currentRow.toNDArray()

    }
    return sqMatrix
}

private fun fillPointsVector(points: List<NDArray<Double, D1>>) {

    for (i in 0 until points.size - 1) {
        val p = (points[i].times(2.0) + points[i+1]).times(2.0)
    }
    /*
    * # build points vector
	P = [2 * (2 * points[i] + points[i + 1]) for i in range(n)]
	P[0] = points[0] + 2 * points[1]
	P[n - 1] = 8 * points[n - 1] + points[n]

    *  */
}


private fun createPoints() : List<NDArray<Double, D1>> {
    // use x^2 + y^2 = r^2
    val offset = 300f
    val r = 200f
    val rSq = r.pow(2)
    val points = mutableListOf<NDArray<Double, D1>>()

    for (n in -200 .. 200 step 20) {
        // y = sqrt(r^2 - x^2)
        val x = (n + offset).toDouble()
        val y = sqrt(rSq - (n.toFloat().pow(2))).toDouble() + offset
        val point = mk.ndarray(mk[x, y])
        points.add(point)
       }
    return points
}
fun main() {
    val points = createPoints()
    curveApacheMath()
    curveMultik(points)
//    println(idt)
}