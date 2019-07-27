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
    /**
     * Other examples for fn include: Default: "addComplex(powComplex(zn, 2),
     * c)" Burning ship: "addComplex(powComplex(absComplex(zn), 2), c)" Tricorn:
     * "addComplex(powComplex(newComplex(zn.r, 0 - zn.theta, true), 2), c)"
     * Cardioid: "addComplex(cosComplex(zn), minverse(c))" (threshold should be
     * 20) Gamma: "multiplyComplex(powComplexComplex(c, subComplex(zn,
     * newComplex(1, 0, true))), expComplex(ainverse(c)))" (threshold should be
     * huge)
     *
     * NOTE: if you are getting a skewed looking image, you should increase the
     * threshold in the kernel to about 20 or higher
     */
    public ArrayList<Integer> transformOperators = new ArrayList();//A list of operator id's in RPN
    public int colorSchemeID = 1; // The ID of the color scheme
    public double threshold = 2; // The threshold radius of the complex number to consider it escaped. AKA bailout
    public boolean antiBuddha = false; // Whether the image should be an anti buddhabrot or a nornal buddhabrot if it is buddha
    public int orbitID = 1; // The ID other the orbit algorithm if fracalType is "orbit"

    public FractalSettings() {
    }

    public FractalSettings(int sizeX, int sizeY, double leftest, double rightest, double highest, double lowest, boolean mirrorXaxis, String fractalType, String fn, double threshold, ArrayList<Integer> transformOperators) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.leftest = leftest;
        this.rightest = rightest;
        this.highest = highest;
        this.lowest = lowest;
        this.mirrorXaxis = mirrorXaxis;
        this.fractalType = fractalType;
        this.fn = fn;
        this.threshold = threshold;
        this.transformOperators = transformOperators;
    }

    public FractalSettings(int sizeX, int sizeY, double leftest, double rightest, double highest, double lowest, boolean mirrorXaxis, boolean calculateComplex, int maxIterations, String fractalType, double f0Re, double f0Im, String fn, double threshold, boolean antiBuddha, int orbitID, ArrayList<Integer> transformOperators, int colorSchemeID) {
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
        this.threshold = threshold;
        this.antiBuddha = antiBuddha;
        this.orbitID = orbitID;
        this.transformOperators = transformOperators;
        this.colorSchemeID = colorSchemeID;
    }

    @Override
    public FractalSettings clone() {
        return new FractalSettings(sizeX, sizeY, leftest, rightest, highest, lowest, mirrorXaxis, calculateComplex, maxIterations, fractalType, f0Re, f0Im, fn, threshold, antiBuddha, orbitID, transformOperators, colorSchemeID);
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
