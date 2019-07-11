package com.maelstrom.mandelbrotgpu;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class FractalSettings {
    public int sizeX=500, sizeY=500;
    public double leftest=-2, rightest=2;
    public double highest=2, lowest=-2;
    public boolean mirrorXaxis=true; // Unused
    public int maxIterations = 5000;
    public String fractalType="Mandelbrot";
    public double f0Re=0, f0Im=0; // Only used for julia
    public String fn= "addComplex(powComplex(zn, 2), c)";//The formula that will be iterated. Available variables are zn, c and n.
    public ArrayList<Integer> transformOperators=new ArrayList();//A list of operator id's in RPN
    public int colorSchemeID = 1;
    //Maybe add threshold
    
    public FractalSettings(){
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
    
    public FractalSettings(int sizeX, int sizeY, double leftest, double rightest, double highest, double lowest, boolean mirrorXaxis, int maxIterations, String fractalType, double f0Re, double f0Im, String fn, ArrayList<Integer> transformOperators, int colorSchemeID) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.leftest = leftest;
        this.rightest = rightest;
        this.highest = highest;
        this.lowest = lowest;
        this.mirrorXaxis = mirrorXaxis;
        this.maxIterations = maxIterations;
        this.fractalType = fractalType;
        this.f0Re = f0Re;
        this.f0Im = f0Im;
        this.fn = fn;
        this.transformOperators = transformOperators;
        this.colorSchemeID = colorSchemeID;
    }
    
    public FractalSettings clone(){
        return new FractalSettings(sizeX, sizeY, leftest, rightest, highest, lowest, mirrorXaxis, maxIterations, fractalType, f0Re, f0Im, fn, transformOperators, colorSchemeID);
    }
    
    public void exportJSON(){
        
    }
    
    public void importJSON(){
        
    }
    
    public String toString(){
        return sizeX+" "+sizeY+" "+leftest+" "+rightest+" "+highest+" "+lowest+" "+fractalType;
    }
}
