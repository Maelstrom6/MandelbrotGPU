package com.maelstrom.mandelbrotgpu;

import java.awt.Color;

/**
 *
 * @author Chris
 */
public class ColorScheme {

    public int[] iterationsToRGBMandelbrot(int schemeID, double[] data, int iterations) {
        // We need valid colors
        if (schemeID == 2) {
            return greenery(data, iterations);
        } else {
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

    public int[] iterationsToRGBBuddha(int schemeID, double[] data, int iterations) {
        if(schemeID == 1){
            return nova(data, iterations);
        }else{
            return nova(data, iterations);
        }
    }
    
    public int[] nova(double[] data, int iterations){
        int[] results = new int[data.length];
        int[] maxRGB = FindMax(data);
        for (int i = 0; i < data.length; i += 3) {
            results[i] = (int)(255 * Math.sqrt(1.0*(data[i]) / maxRGB[0]));
            results[i + 1] = (int)(255 * Math.sqrt(1.0*(data[i + 1]) / maxRGB[1]));
            results[i + 2] = (int)(255 * Math.sqrt(1.0*(data[i + 2]) / maxRGB[2]));
        }
        return results;
    }

    public int[] FindMax(double[] a) {
        int[] max = new int[3];
        max[0] = 0;
        max[1] = 0;
        max[2] = 0;
        for (int i = 0; i < a.length; i+=3) {
            if (a[i] > max[0]) {
                max[0] = (int) a[i];
            }
            if (a[i+1] > max[1]) {
                max[1] = (int) a[i+1];
            }
            if (a[i+2] > max[2]) {
                max[2] = (int) a[i+2];
            }
        }
        return max;
    }

}
