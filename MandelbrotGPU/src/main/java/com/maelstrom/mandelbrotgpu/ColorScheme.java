package com.maelstrom.mandelbrotgpu;

import java.awt.Color;

/**
 *
 * @author Chris
 */
public class ColorScheme {

    public int[] iterationsToRGBMandelbrot(int schemeID, double[] data, int iterations) {
        // We need valid colors
        switch (schemeID) {
            case 2:
                return greenery(data, iterations);
            case 3:
                return reds(data, iterations);
            default:
                return blues(data, iterations);
        }

    }

    public int[] greenery(double[] data, int iterations) {
        Color base = new Color(0, 0, 0),
                limit = new Color(255, 255, 255);
        int[] results = new int[data.length * 3];

        // For each pixel pick the color we want
        for (int i = 0; i < data.length; i++) {
            results[i * 3 + 0] = data[i] >= 0 ? (int) Math.abs(limit.getRed() * Math.sin(data[i] / 4)) : base.getRed();
            results[i * 3 + 1] = data[i] >= 0 ? (int) Math.abs(limit.getGreen() * Math.sin(data[i] / 3)) : base.getGreen();
            results[i * 3 + 2] = data[i] >= 0 ? (int) Math.abs(limit.getBlue() * Math.sin(data[i] / 8)) : base.getBlue();
        }
        return results;
    }

    public int[] blues(double[] data, int iterations) {
        int[] results = new int[data.length * 3];
        for (int i = 0; i < data.length; i++) {
            if (data[i] >= 0) {
                results[i * 3 + 0] = (int) (Math.sin(data[i] / Math.PI / 2 / 4) * 100 + 150);
                results[i * 3 + 1] = (int) (Math.sin(data[i] / Math.PI / 4) * 100 + 150);
                results[i * 3 + 2] = (int) (Math.cos(data[i] / Math.PI * 2 / 4) * 100 + 150);
            } else {
                results[i * 3 + 0] = results[i * 3 + 1] = results[i * 3 + 2] = 0;
            }
        }
        return results;
    }

    public int[] reds(double[] data, int iterations) {
        int[] results = new int[data.length * 3];
        for (int i = 0; i < data.length; i++) {
            if (data[i] >= 0) {
                results[i * 3 + 0] = (int) (Math.sin(data[i] / Math.PI / 2 / 4) * 100 + 150);
                results[i * 3 + 1] = (int) (Math.sin(data[i] / Math.PI / 4) * 50 + 50);
                results[i * 3 + 2] = (int) (Math.cos(data[i] / Math.PI * 2 / 4) * 50 + 50);
            } else {
                results[i * 3 + 0] = results[i * 3 + 1] = results[i * 3 + 2] = 0;
            }
        }
        return results;
    }

    public int[] iterationsToRGBBuddha(int schemeID, double[] data, int iterations) {
        switch (schemeID) {
            case 2:
                return nova2(data, iterations);
            case 3:
                return nova3(data, iterations);
            default:
                return nova1(data, iterations);
        }
    }

    public int[] nova1(double[] data, int iterations) {
        int[] results = new int[data.length];
        int[] maxRGB = FindMax(data);
        for (int i = 0; i < data.length; i += 3) {
            results[i] = (int) (255 * Math.sqrt(1.0 * (data[i]) / maxRGB[0]));
            results[i + 1] = (int) (255 * Math.sqrt(1.0 * (data[i + 1]) / maxRGB[1]));
            results[i + 2] = (int) (255 * Math.sqrt(1.0 * (data[i + 2]) / maxRGB[2]));
        }
        return results;
    }

    public int[] nova2(double[] data, int iterations) {
        int[] results = new int[data.length];
        int[] maxRGB = FindMax(data);
        for (int i = 0; i < data.length; i += 3) {
            results[i] = (int) (255 * Math.sqrt(1.0 * (data[i + 1]) / maxRGB[1]));
            results[i + 1] = (int) (255 * Math.sqrt(1.0 * (data[i + 2]) / maxRGB[2]));
            results[i + 2] = (int) (255 * Math.sqrt(1.0 * (data[i]) / maxRGB[0]));
        }
        return results;
    }

    public int[] nova3(double[] data, int iterations) {
        int[] results = new int[data.length];
        int[] maxRGB = FindMax(data);
        for (int i = 0; i < data.length; i += 3) {
            results[i] = (int) (255 * Math.sqrt(1.0 * (data[i + 2]) / maxRGB[2]));
            results[i + 1] = (int) (255 * Math.sqrt(1.0 * (data[i]) / maxRGB[0]));
            results[i + 2] = (int) (255 * Math.sqrt(1.0 * (data[i + 1]) / maxRGB[1]));
        }
        return results;
    }

    public int[] FindMax(double[] a) {
        int[] max = new int[3];
        max[0] = 0;
        max[1] = 0;
        max[2] = 0;
        for (int i = 0; i < a.length; i += 3) {
            if (a[i] > max[0]) {
                max[0] = (int) a[i];
            }
            if (a[i + 1] > max[1]) {
                max[1] = (int) a[i + 1];
            }
            if (a[i + 2] > max[2]) {
                max[2] = (int) a[i + 2];
            }
        }
        return max;
    }

}
