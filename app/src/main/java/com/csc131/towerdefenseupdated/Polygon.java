package com.csc131.towerdefenseupdated;

class Polygon
{
    private int[][] polyY, polyX;

    // Number of sides in the polygon.
    private int polySides;

    // Heading
    Tower1.Heading heading;

    Polygon(int[][] px, int[][] py, int ps, Tower1.Heading head)
    {
        polyX = px;
        polyY = py;
        polySides = ps;
        heading = head;
    }


    boolean contains(int x, int y, int index)
    {
        boolean c = false;
        int i, j;
        for (i = 0, j = polySides - 1; i < polySides; j = i++) {
            if (((polyY[index][i] > y) != (polyY[index][j] > y))
                    && (x < (polyX[index][j] - polyX[index][i]) * (y - polyY[index][i]) / (polyY[index][j] - polyY[index][i]) + polyX[index][i]))
                c = !c;
        }
        return c;
    }

}