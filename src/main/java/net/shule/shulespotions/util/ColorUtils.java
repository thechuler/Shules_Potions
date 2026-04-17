package net.shule.shulespotions.util;

import org.joml.Random;

public class ColorUtils {


    public  static  int randomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        int a = 255;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int lerpColor(int colorA, int colorB, float t) {
        t = Math.max(0, Math.min(1, t));

        int a1 = (colorA >> 24) & 0xFF;
        int r1 = (colorA >> 16) & 0xFF;
        int g1 = (colorA >> 8) & 0xFF;
        int b1 = colorA & 0xFF;

        int a2 = (colorB >> 24) & 0xFF;
        int r2 = (colorB >> 16) & 0xFF;
        int g2 = (colorB >> 8) & 0xFF;
        int b2 = colorB & 0xFF;

        int a = (int)(a1 + (a2 - a1) * t);
        int r = (int)(r1 + (r2 - r1) * t);
        int g = (int)(g1 + (g2 - g1) * t);
        int b = (int)(b1 + (b2 - b1) * t);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}
