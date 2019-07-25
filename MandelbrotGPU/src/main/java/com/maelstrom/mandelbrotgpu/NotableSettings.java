/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maelstrom.mandelbrotgpu;

/**
 *
 * @author Chris
 */
public class NotableSettings {

    public static FractalSettings TheBox() {
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 500;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fractalType = "Buddha";
        settings.transformOperators.add(-2);
        settings.transformOperators.add(4);
        settings.transformOperators.add(5);
        settings.leftest = 4;
        settings.rightest = 4;
        settings.highest = 4;
        settings.lowest = -4;
        return settings;
    }

    public static FractalSettings Cave() {
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 500;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fractalType = "Buddha";
        settings.transformOperators.add(0);
        settings.leftest = -4;
        settings.rightest = 4;
        settings.highest = 4;
        settings.lowest = -4;
        return settings;
    }

    public static FractalSettings Tree() {
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 100;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.transformOperators.add(1);
        settings.transformOperators.add(2);
        settings.transformOperators.add(-4);
        settings.leftest = -4;
        settings.rightest = 4;
        settings.highest = 4;
        settings.lowest = -4;
        return settings;
    }

    public static FractalSettings Spider() {
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 100;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.transformOperators.add(-5);
        settings.transformOperators.add(-6);
        settings.transformOperators.add(-1);
        settings.leftest = -8;
        settings.rightest = 8;
        settings.highest = 8;
        settings.lowest = -8;
        return settings;
    }

    public static FractalSettings Bedbug() {
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 500;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fractalType = "Buddha";
        settings.transformOperators.add(-4);
        settings.transformOperators.add(-3);
        settings.transformOperators.add(4);
        settings.leftest = -4;
        settings.rightest = 4;
        settings.highest = 4;
        settings.lowest = -4;
        return settings;
    }

    public static FractalSettings Snowglobe() {
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 500;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fractalType = "Buddha";
        settings.transformOperators.add(-5);
        settings.transformOperators.add(-2);
        settings.transformOperators.add(1);
        settings.leftest = -4;
        settings.rightest = 4;
        settings.highest = 4;
        settings.lowest = -4;
        return settings;
    }

    public static FractalSettings GatesOfHeaven() {
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 500;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fractalType = "Buddha";
        settings.transformOperators.add(3);
        settings.transformOperators.add(2);
        settings.transformOperators.add(0);
        settings.leftest = -4;
        settings.rightest = 4;
        settings.highest = 4;
        settings.lowest = -4;
        return settings;
    }

    public static FractalSettings BuddhaCubed() {
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 500;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fractalType = "Buddha";
        settings.fn = "addComplex(powComplex(zn, 3), c)";
        settings.leftest = -2;
        settings.rightest = 2;
        settings.highest = 2;
        settings.lowest = -2;
        return settings;
    }

    public static FractalSettings Diamond() {
        // Credit to https://www.deviantart.com/matplotlib
        /*
        For all of matplotlib's art, maxIterations should be high to reduce 
        dotted artifacts and threshold should also be high in order to 
        attain an accurate image. Since some of the functions for some points 
        have a stationary point far greate than a radius of 2 from the cnetre.
         */
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 100;
        settings.sizeX = 500;
        settings.sizeY = 1000;
        settings.threshold = 1000;
        settings.fn = "cosComplex(divideComplex(zn, c))";
        settings.f0Re = 0;
        settings.f0Im = 0;
        settings.leftest = -1;
        settings.rightest = 1;
        settings.highest = 2;
        settings.lowest = -2;
        return settings;
    }

    public static FractalSettings Kidney() {
        // Credit to https://www.deviantart.com/matplotlib
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 2000;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.threshold = 1000;
        settings.fn = "addComplex(cosComplex(zn), minverse(c))";
        settings.f0Re = 0;
        settings.f0Im = 0;
        settings.leftest = -1;
        settings.rightest = 1;
        settings.highest = 1;
        settings.lowest = -1;
        return settings;
    }

    public static FractalSettings Starfish() {
        // Credit to https://www.deviantart.com/matplotlib
        // This is one of the computationally intensive fractals 
        // and I have not managed to attain a good result so far.
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 1000;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fn = "multiplyComplex(powComplexComplex(c, "
                + "subComplex(zn, newComplex(1, 0, true))),"
                + "expComplex(ainverse(c)))";
        settings.threshold = 100;
        settings.f0Re = 0;
        settings.f0Im = 0;
        settings.leftest = -2.8;
        settings.rightest = 1.3;
        settings.highest = 2;
        settings.lowest = -2;
        return settings;
    }

    public static FractalSettings Scorpion() {
        // Credit to https://www.deviantart.com/matplotlib
        // This is one of the computationally intensive fractals 
        // and I have not managed to attain a good result so far.
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 300;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fn = "addComplex(sinhComplex(zn), minverse(powComplex(c, 2)))";
        settings.threshold = 200;
        settings.f0Re = 1;
        settings.f0Im = 0.1;
        settings.leftest = -50;
        settings.rightest = 50;
        settings.highest = 50;
        settings.lowest = -50;
        return settings;
    }

    public static FractalSettings MandelbrotV2() {
        // Credit to https://www.reddit.com/r/mathpics/comments/bokheg/mandelbrot_set_of_z_pi_cosz_c/
        // and https://www.reddit.com/r/mathpics/comments/bokheg/mandelbrot_set_of_z_pi_cosz_c/eo0b7a2?utm_source=share&utm_medium=web2x
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 1000;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fn = "addComplex(multiplyScalarComplex(cosComplex(zn), 2 * M_PI), c)";
        settings.threshold = 100;
        settings.f0Re = 0;
        settings.f0Im = 0;
        settings.leftest = -1;
        settings.rightest = 1;
        settings.highest = 1;
        settings.lowest = -1;
        return settings;
    }

}
