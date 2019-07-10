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
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 100;
        FractalManager obj;
        settings.sizeX = 300;
        settings.sizeY = 300;
        settings.fractalType = "Buddha";
        //settings.fractalType = "Mandelbrot";
        //settings.transformOperators.add(-4);
        //settings.transformOperators.add(6);
        //settings.transformOperators.add(4);
        settings.leftest = -2;
        settings.rightest = 2;
        settings.highest = 2;
        settings.lowest = -2;
        settings.fn = "addComplex(powComplex(zn, 3), c)";
        obj = new FractalManager();
        
        //obj.LoadProgram(settings.fractalType, settings.fn, settings.transformOperators, 0);
        //obj.savePNG(obj.createImageSimple(settings, 0), System.getProperty("user.dir") + "\\MyNewTest.png");
        
        /*for (int i = -6; i <= 6; i++) {
            settings.transformOperators.clear();
            settings.transformOperators.add(i);
            obj.LoadProgram(settings.fractalType, settings.fn, settings.transformOperators, 0);
            obj.savePNG(obj.createImageSimple(settings, 0), System.getProperty("user.dir") + "\\Buddhas cubed 1\\MyNewTest" + i + ".png");
        }*/

        /*for (int i = -6; i <= 6; i++) {
            for (int j = -6; j <= 6; j++) {
                for (int k = -6; k <= 6; k++) {
                    if (i == -j || j == -k) {
                        continue;
                    }
                    settings.transformOperators.clear();
                    settings.transformOperators.add(i);
                    settings.transformOperators.add(j);
                    settings.transformOperators.add(k);
                    obj.LoadProgram(settings.fractalType, settings.fn, settings.transformOperators, 0);
                    obj.savePNG(obj.createImageSimple(settings, 0), System.getProperty("user.dir") + "\\Mandelbrots 3\\MyNewTest" + i + " " + j + " " + k + ".png");
                }
            }
        }*/

        // TODO code application logic here
    }

}
