package com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic

object Tridiagonal {
    /// <summary>
    /// Solves a tridiagonal system.
    /// </summary>
    /// <remarks>
    /// All vectors have size of n although some elements are not used.
    /// </remarks>
    /// <param name="a">Lower diagonal vector; a[0] not used.</param>
    /// <param name="b">Main diagonal vector.</param>
    /// <param name="c">Upper diagonal vector; c[n-1] not used.</param>
    /// <param name="rhs">Right hand side vector</param>
    /// <returns>system solution vector</returns>
    fun solve( a : DoubleArray ,  b: DoubleArray,  c: DoubleArray,  rhs: DoubleArray) : DoubleArray
    {
        // a, b, c and rhs vectors must have the same size.
        if (a.size != b.size || c.size != b.size || rhs.size != b.size)
            throw  IllegalArgumentException("Diagonal and rhs vectors must have the same size.");
        if (b[0] == 0.0)
            throw  Exception("Singular matrix.");
        // If this happens then you should rewrite your equations as a set of
        // order N - 1, with u2 trivially eliminated.

        val n = rhs.size
        val u = DoubleArray(n)
        val gam = DoubleArray(n) // One vector of workspace, gam is needed.

        var bet = b[0];
        u[0] = rhs[0] / bet;
        for (j in 1 until n) // Decomposition and forward substitution.
        {
            gam[j] = c[j-1] / bet;
            bet = b[j] - a[j] * gam[j];
            if (bet == 0.0)
            // Algorithm fails.
                throw Exception("Singular matrix.");
            u[j] = (rhs[j] - a[j] * u[j - 1]) / bet;
        }
        for (j in 1 until n) {
            u[n - j - 1] -= gam[n - j] * u[n - j]; // Backsubstitution.
        }
        return u;
    }
}
