package is.hi.hbv501.pirategame.pirategame.game.util;

public class SquareGradient {
    public static double[][] Generate(int w, int h)
    {
        int halfWidth = w / 2;
        int halfHeight = h / 2;

        double[][] gradient = new double[w][];

        for (int i = 0; i < w; i++)
        {
            gradient[i] = new double[h];

            for (int j = 0; j < h; j++)
            {
                int x = i;
                int y = j;

                float colorValue;

                x = x > halfWidth ? w - x : x;
                y = y > halfHeight ? h - y : y;

                int smaller = Math.min(x, y);
                colorValue = smaller / (float)halfWidth;

                colorValue = 1 - colorValue;
                colorValue *= colorValue * colorValue;
                gradient[i][j] = colorValue;
            }
        }

        return gradient;
    }
}
