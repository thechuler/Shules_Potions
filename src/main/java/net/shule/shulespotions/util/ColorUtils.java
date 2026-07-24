package net.shule.shulespotions.util;

import org.joml.Random;

import java.awt.*;

public class ColorUtils {


    public  static  int randomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        int a = 255;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }


    public static int fromHex(String hex) {
        hex = hex.trim()
                .replace("#", "")
                .replace("0x", "")
                .replace("0X", "");

        return Integer.parseUnsignedInt(hex, 16);
    }
    public static String toHex(int color) {
        return String.format("#%06X", color & 0xFFFFFF);
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


    public static float[] intToRGB(int color) {
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        return new float[]{r, g, b};
    }


    public static int rgbToInt(int r, int g, int b) {
        int red = (int) (r * 255.0F);
        int green = (int) (g * 255.0F);
        int blue = (int) (b * 255.0F);

        return (red << 16) | (green << 8) | blue;
    }


    public static int mixColors(int c1, int c2) {

        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;

        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = c2 & 0xFF;

        float[] hsv1 = Color.RGBtoHSB(r1, g1, b1, null);
        float[] hsv2 = Color.RGBtoHSB(r2, g2, b2, null);


        float h1 = hsv1[0];
        float h2 = hsv2[0];

        float dh = h2 - h1;

        if (Math.abs(dh) > 0.5f) {
            if (dh > 0) {
                h1 += 1.0f;
            } else {
                h2 += 1.0f;
            }
        }

        float h = (h1 + h2) / 2.0f;
        h %= 1.0f;

       
        float s = (hsv1[1] + hsv2[1]) / 2.0f;
        float v = (hsv1[2] + hsv2[2]) / 2.0f;

        return Color.HSBtoRGB(h, s, v) & 0xFFFFFF;
    }
}
