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
        settings.maxIterations = 500;
        settings.sizeX = 1000;
        settings.sizeY = 1000;
        settings.fractalType = "Buddha";
        settings.fractalType = "orbit";
        settings.threshold = 100;
        settings.orbitID = 2;
        settings.calculateComplex = true;
        settings.antiBuddha = true;
        //settings.transformOperators.add(-5);
        //settings.transformOperators.add(4);
        settings.transformOperators.add(0);
        settings.leftest = -4;
        settings.rightest = 4;
        settings.highest = 4;
        settings.lowest = -4;

        Mapper obj = new Mapper(settings.fractalType);
        obj.savePNG(obj.createProgramAndImage(settings), System.getProperty("user.dir") + "\\MyNewTest.png");

        //Below are a few functions I am trying out
        /*
        settings.fn = "addComplex(multiplyScalarComplex(powComplex(zn, 2), 1.0 * fib(n+1) / (n+1)), c)";
        settings.fn = "multiplyScalarComplex(addComplex(powComplex(zn, 2), c), 1.0 * fib(n+1) / (n+1))";
        settings.fn = "multiplyScalarComplex(addComplex(multiplyScalarComplex(powComplex(zn, 2), 1.0 * fib(n+1) / (n+1)/(n+1)), c), 1.0 * (n+1)*(n+1) / fib(n+1))";
        settings.fn = "addComplex(powComplex(zn, 2), multiplyScalarComplex(c, 1.0 * (n+1) / fib(n+1)))";
         */
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
        /*
        double speed = 0.1;
        for (double i = -1; i <= 0; i += speed) {//going from 1/2 to 1/3

            settings.transformOperators.clear();
            //settings.transformOperators.add(-13); // The 1/10th power
            //settings.transformOperators.add(23+i); // The 20+ith power
            
            //settings.transformOperators.add(-2);
            //settings.transformOperators.add(4);
            //settings.transformOperators.add(-((int) (1 / speed) + 3)); // The 1/10th power
            //settings.transformOperators.add(((int) (2 / speed + i / speed)) + 3); // The 20+ith power
            
            settings.transformOperators.add(-(((int) (2 / speed + i / speed)) + 3));
            settings.transformOperators.add(((int) (1 / speed) + 3));
            settings.transformOperators.add(4);
            settings.transformOperators.add(0);
            //settings.transformOperators.add(-(23+i)); // The 20+ith power
            //settings.transformOperators.add(13); // The 1/10th power
            obj.savePNG(obj.createProgramAndImage(settings), System.getProperty("user.dir") + "\\Transitions5\\MyNewTest" + Math.round(i * 1000) + ".png");
        }*/
    }
}
