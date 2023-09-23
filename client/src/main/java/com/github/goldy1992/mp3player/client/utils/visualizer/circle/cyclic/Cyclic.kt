package com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic

// <copyright file="Cyclic.cs" company="Numerical Recipes">
// Copyright © Numerical Recipes.</copyright>
// <email>ov-p@yandex.ru</email>
// <summary>Chapter 2.7 Sparse Linear Systems.
// Solution of the cyclic set of linear equations.</summary>


    /// <summary>
    /// Solves the cyclic set of linear equations.
    /// </summary>
    /// <remarks>
    /// The cyclic set of equations have the form
    /// ---------------------------
    /// b0 c0  0 · · · · · · β
    ///	a1 b1 c1 · · · · · · ·
    ///  · · · · · · · · · · · 
    ///  · · · a[n−2] b[n−2] c[n−2]
    /// α  · · · · 0  a[n-1] b[n-1]
    /// ---------------------------
    /// This is a tridiagonal system, except for the matrix elements α and β in the corners.
    /// </remarks> 
object Cyclic {
        /// <summary>
        /// Solves the cyclic set of linear equations. 
        /// </summary>
        /// <remarks>
        /// All vectors have size of n although some elements are not used.
        /// The input is not modified.
        /// </remarks>
        /// <param name="a">Lower diagonal vector of size n; a[0] not used.</param>
        /// <param name="b">Main diagonal vector of size n.</param>
        /// <param name="c">Upper diagonal vector of size n; c[n-1] not used.</param>
        /// <param name="alpha">Bottom-left corner value.</param>
        /// <param name="beta">Top-right corner value.</param>
        /// <param name="rhs">Right hand side vector.</param>
        /// <returns>The solution vector of size n.</returns>
    fun solve(a : DoubleArray, b : DoubleArray, c : DoubleArray, alpha : Double, beta : Double, rhs: DoubleArray) : DoubleArray
    {
        // a, b, c and rhs vectors must have the same size.
        if (a.size != b.size || c.size != b.size || rhs.size != b.size)
            throw IllegalArgumentException("Diagonal and rhs vectors must have the same size.")
        val n = b.size
        if (n <= 2)
            throw IllegalArgumentException("n too small in Cyclic; must be greater than 2.");

        val gamma = -b[0]; // Avoid subtraction error in forming bb[0].
        // Set up the diagonal of the modified tridiagonal system.
        val bb = DoubleArray(n)
        bb[0] = b[0] - gamma;
        bb[n-1] = b[n - 1] - alpha * beta / gamma;
        for (i in 1 until n-1) {
            bb[i] = b[i];
        }
        // Solve A · x = rhs.
        var solution = Tridiagonal.solve(a, bb, c, rhs);
        val x = DoubleArray(n)
        for (k in 0 until n) {
            x[k] = solution[k];
        }

        // Set up the vector u.
        val u = DoubleArray(n)
        u[0] = gamma;
        u[n-1] = alpha;
        for (i in 1 until n-1) {
            u[i] = 0.0;
        }
        // Solve A · z = u.
        solution = Tridiagonal.solve(a, bb, c, u)
        val z = DoubleArray(n)
        for (k in 0 until n) {
            z[k] = solution[k];
        }

        // Form v · x/(1 + v · z).
        val fact : Double = (x[0] + beta * x[n - 1] / gamma) / (1.0 + z[0] + beta * z[n - 1] / gamma)

        // Now get the solution vector x.

        for (i in 0 until n) {
            x[i] -= fact * z[i]
        }
        return x
    }
}

/*
private void button1_Click(object sender, System.EventArgs e)
{
    double[] a = new Double[4];
    double[] b = new Double[4];
    double[] c = new Double[4];
    double[] r = new Double[4];
    double[] x = new Double[4];
    
    a[0] = 1.0;
    a[1] = 2.0;
    a[2] = 3.0;
    a[3] = 4.0;

    b[0] = 1.0;
    b[1] = 2.0;
    b[2] = 3.0;
    b[3] = 4.0;

    c[0] = 1.0;
    c[1] = 2.0;
    c[2] = 3.0;
    c[3] = 4.0;

    r[0] = 1.0;
    r[1] = 2.0;
    r[2] = 3.0;
    r[3] = 4.0;

    double alpha = 1.0;
    double beta = 1.0;

    
    
    NR.SolutionOfLinearAlgebraicEquations.Cyclic ob = 
        new NR.SolutionOfLinearAlgebraicEquations.Cyclic();
    
    ob.cyclic(a,b,c,alpha,beta,r,x,4);

    for(int i = 0; i < 4; i++)
        textBox1.Text += Convert.ToString(x[i]) + "\r\n"; 
}  
*/