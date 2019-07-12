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
    
    public static FractalSettings TheBox(){
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
    
    public static FractalSettings Cave(){
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
    
    public static FractalSettings Tree(){
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
    
    public static FractalSettings Spider(){
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
    
    public static FractalSettings Bedbug(){
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
    
    public static FractalSettings Snowglobe(){
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
    
    public static FractalSettings GatesOfHeaven(){
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
    
    public static FractalSettings BuddhaCubed(){
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
}
