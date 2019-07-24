package com.maelstrom.mandelbrotgpu;

import com.maelstrom.mandelbrotgpu.mappers.Mapper;

/**
 *
 * @author Chris
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // All the uncommmented code is just what I've been working on latley
        // You should probably ignore that and scroll down to the notable fractals section
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 100;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fractalType = "Julia Buddha";
        //settings.fractalType = "Mandelbrot";
        settings.calculateComplex = true;
        //settings.fn = "addComplex(multiplyScalarComplex(powComplex(zn, 2), 1.0 * fib(n+1) / (n+1)), c)";
        
        //settings.fn = "addComplex(powComplex(zn, 2), multiplyScalarComplex(c, 1.0 * (n+1) / fib(n+1)))";
        
        //settings.fn = "addComplex( multiplyScalarComplex(cosComplex(zn), M_PI), c)";
        //settings.fn = "addComplex(cosComplex(multiplyScalarComplex(zn, M_PI)), c)";
        
        //settings.fn = "multiplyComplex(powComplexComplex(c, subComplex(zn, newComplex(1, 0, true))),"
        //        + "expComplex(ainverse(c)))";
        //settings.fn = "cosComplex(divideComplex(zn, c))";
        settings.threshold = 1000;
        settings.fn = "addComplex(cosComplex(zn), minverse(c))";
        
        //settings.fn = "addComplex(powComplex(multiplyScalarComplex(zn, 1.0 * n / fib(n)), 2), c)";
        
        //settings.f0Re = 0.285;
        //settings.f0Im = 0.01;
        //settings.transformOperators.add(-5);
        //settings.transformOperators.add(-1);
        //settings.transformOperators.add(6);
        settings.leftest = -2;
        settings.rightest = 2;
        settings.highest = 2;
        settings.lowest = -2;

        //obj.savePNG(obj.createProgramAndImage(settings, 0), System.getProperty("user.dir") + "\\MyNewTest.png");
        Mapper obj = new Mapper(settings.fractalType);
        obj.savePNG(obj.createProgramAndImage(settings), System.getProperty("user.dir") + "\\MyNewTest.png");

        // Below are some notable fractals:
        /*
        obj = new Mapper("buddha");
        obj.savePNG(obj.createProgramAndImage(NotableSettings.Bedbug()), System.getProperty("user.dir") + "\\Bedbug.png");
        obj.savePNG(obj.createProgramAndImage(NotableSettings.Snowglobe()), System.getProperty("user.dir") + "\\Snowglobe.png");
        obj.savePNG(obj.createProgramAndImage(NotableSettings.GatesOfHeaven()), System.getProperty("user.dir") + "\\GatesOfHeaven.png");
        obj.savePNG(obj.createProgramAndImage(NotableSettings.BuddhaCubed()), System.getProperty("user.dir") + "\\BuddhaCubed.png");
        obj.savePNG(obj.createProgramAndImage(NotableSettings.TheBox()), System.getProperty("user.dir") + "\\TheBox.png");
        
        obj = new Mapper("mandelbrot");
        obj.savePNG(obj.createProgramAndImage(NotableSettings.Tree()), System.getProperty("user.dir") + "\\Tree.png");
        obj.savePNG(obj.createProgramAndImage(NotableSettings.Spider()), System.getProperty("user.dir") + "\\Spider.png");
         */

        /*double speed = 0.01;
        for (double i = 0; i <= 1; i += speed) {//going from 1/2 to 1/3

            settings.transformOperators.clear();
            //settings.transformOperators.add(-13); // The 1/10th power
            //settings.transformOperators.add(23+i); // The 20+ith power
            settings.transformOperators.add(-2);
            settings.transformOperators.add(4);
            settings.transformOperators.add(-((int) (1 / speed) + 3)); // The 1/10th power
            settings.transformOperators.add(((int) (2 / speed + i / speed)) + 3); // The 20+ith power
            //settings.transformOperators.add(-(23+i)); // The 20+ith power
            //settings.transformOperators.add(13); // The 1/10th power
            obj.savePNG(obj.createProgramAndImage(settings), System.getProperty("user.dir") + "\\Transitions4\\MyNewTest" + Math.round(i * 1000) + ".png");
        }*/
    }
}
