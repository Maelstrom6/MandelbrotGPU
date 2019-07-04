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
        settings.maxIterations = 500;
        FractalManager obj;
        settings.sizeX = 1000;
        settings.sizeY = 1000;
        settings.fractalType = "Buddha";
        //settings.fractalType = "Mandelbrot";
        settings.transformOperators.add(-6);
        settings.transformOperators.add(-2);
        settings.transformOperators.add(6);
        settings.leftest = -4;
        settings.rightest = 4;
        settings.highest  = 4;
        settings.lowest  = -4;
        obj = new FractalManager(settings.fractalType, settings.fn, settings.transformOperators, settings.maxIterations);
        obj.savePNG(obj.createImageSimple(settings, 0), System.getProperty("user.dir") + "\\MyNewTest.png");
        
        
        
        /*for(int i=1;i<6;i++){
            for(int j=-5;j<5;j++){
                settings.transformOperators.clear();
                settings.transformOperators.add(i);
                settings.transformOperators.add(j);
                obj = new FractalManager(settings.fractalType, settings.fn, settings.transformOperators, settings.maxIterations);
                obj.savePNG(obj.createImageSimple(settings, 0), System.getProperty("user.dir") + "\\Buddhas\\MyNewTest"+i+" "+j+".png");
                obj.finalize();
                obj = null;
            }
        }*/
        
        // TODO code application logic here
    }
    
}
