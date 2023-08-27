package com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic

object ClosedBezierSpline {
    /// <summary>
    /// Get Closed Bezier Spline Control Points.
    /// </summary>
    /// <param name="knots">Input Knot Bezier spline points.</param>
    /// <param name="firstControlPoints">
    /// Output First Control points array of the same
    /// length as the <paramref name="knots"> array.</param>
    /// <param name="secondControlPoints">
    /// Output Second Control points array of the same
    /// length as the <paramref name="knots"> array.</param>
    fun getCurveControlPoints(knots : Array<Point>) :  Pair<Array<Point>, Array<Point>>
    {
        val firstControlPoints : Array<Point>
        val secondControlPoints : Array<Point>
        val n = knots.size;
        if (n <= 2)
        { // There should be at least 3 knots to draw closed curve.
            firstControlPoints = arrayOf();
            secondControlPoints = arrayOf();
            return Pair(firstControlPoints, secondControlPoints);
        }

        // Calculate first Bezier control points

        // The matrix.
        val a = DoubleArray(n)
        val b = DoubleArray(n)
        val c = DoubleArray(n)
        for (i in 0 until n)
        {
            a[i] = 1.0;
            b[i] = 4.0;
            c[i] = 1.0;
        }

        // Right hand side vector for points X coordinates.
        val rhs = DoubleArray(n)
        for (i in 0 until n)
        {
            val j : Int = if (i == n - 1) 0 else i + 1
            rhs[i] = 4 * knots[i].X + 2 * knots[j].X;
        }
        // Solve the system for X.
        val x : DoubleArray = Cyclic.solve(a, b, c, 1.0, 1.0, rhs);

        // Right hand side vector for points Y coordinates.
        for (i in 0 until n)
        {
            val j : Int = if (i == n - 1) 0 else i + 1
            rhs[i] = 4 * knots[i].Y + 2 * knots[j].Y;
        }
        // Solve the system for Y.
        val y : DoubleArray = Cyclic.solve(a, b, c, 1.0, 1.0, rhs);


        // Fill output arrays.
        firstControlPoints = Array(n) {
            Point(x[it], y[it])
        }
        secondControlPoints = Array(n) {
            Point(2 * knots[it].X - x[it], 2 * knots[it].Y - y[it])
        }

        return Pair(firstControlPoints, secondControlPoints)

    }
}