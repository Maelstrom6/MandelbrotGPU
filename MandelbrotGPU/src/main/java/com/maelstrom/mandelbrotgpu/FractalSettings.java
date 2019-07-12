package com.maelstrom.mandelbrotgpu;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class FractalSettings {

    public int sizeX = 500, sizeY = 500;
    public double leftest = -2, rightest = 2; // The real component of the left and right edge of the image
    public double highest = 2, lowest = -2; // The imaginary compmonent of the top and bottom of the image
    public boolean mirrorXaxis = true; // used for buddha. Still need to implement for mandelbrot
    public boolean calculateComplex = false; // Whether to create the image with blocks
    public int maxIterations = 5000; // The maximum iterations of fn
    public String fractalType = "Mandelbrot";
    public double f0Re = 0, f0Im = 0; // Only used for julia. This is the value of c
    public String fn = "addComplex(powComplex(zn, 2), c)";//The formula that will be iterated. Available variables are zn, c and n.
    public ArrayList<Integer> transformOperators = new ArrayList();//A list of operator id's in RPN
    public int colorSchemeID = 1; // The ID of the color scheme
    //Maybe add threshold

    public FractalSettings() {
    }

    public FractalSettings(int sizeX, int sizeY, double leftest, double rightest, double highest, double lowest, boolean mirrorXaxis, String fractalType, String fn, ArrayList<Integer> transformOperators) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.leftest = leftest;
        this.rightest = rightest;
        this.highest = highest;
        this.lowest = lowest;
        this.mirrorXaxis = mirrorXaxis;
        this.fractalType = fractalType;
        this.fn = fn;
        this.transformOperators = transformOperators;
    }

    public FractalSettings(int sizeX, int sizeY, double leftest, double rightest, double highest, double lowest, boolean mirrorXaxis, boolean calculateComplex, int maxIterations, String fractalType, double f0Re, double f0Im, String fn, ArrayList<Integer> transformOperators, int colorSchemeID) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.leftest = leftest;
        this.rightest = rightest;
        this.highest = highest;
        this.lowest = lowest;
        this.mirrorXaxis = mirrorXaxis;
        this.calculateComplex = calculateComplex;
        this.maxIterations = maxIterations;
        this.fractalType = fractalType;
        this.f0Re = f0Re;
        this.f0Im = f0Im;
        this.fn = fn;
        this.transformOperators = transformOperators;
        this.colorSchemeID = colorSchemeID;
    }

    @Override
    public FractalSettings clone() {
        return new FractalSettings(sizeX, sizeY, leftest, rightest, highest, lowest, mirrorXaxis, calculateComplex, maxIterations, fractalType, f0Re, f0Im, fn, transformOperators, colorSchemeID);
    }

    public void exportJSON() {

    }

    public void importJSON() {

    }

    @Override
    public String toString() {
        return sizeX + " " + sizeY + " " + leftest + " " + rightest + " " + highest + " " + lowest + " " + fractalType;
    }
}
