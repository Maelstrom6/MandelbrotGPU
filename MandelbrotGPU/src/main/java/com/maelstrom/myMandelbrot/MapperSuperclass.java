package com.maelstrom.myMandelbrot;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;//for quality: increase num on lines 41,46,47,95

/**
 *
 * @author ChrisPC
 */
public class MapperSuperclass {

    protected int numer = 2, denom = 1;
    protected int sizeX=500, sizeY=500;
    protected double limX=2, limY=2;
    protected double centreX=0, centreY=0;
    protected boolean mirrorXaxis=true;
    protected BufferedImage map;
    protected int maxIterations = 5000;//500
    protected int noOfThreads = 4;
    protected int threadIterator=0;
    protected ArrayList<Integer> operatorString;//A list of operator id's in RPN

    public MapperSuperclass(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public MapperSuperclass(int sizeX, int sizeY, double limX, double limY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.operatorString = new ArrayList();
    }

    public MapperSuperclass(int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
        this.mirrorXaxis = (centreY == 0);
    }

    public MapperSuperclass(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        this.numer = numer;
        this.denom = denom;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
        this.mirrorXaxis = (centreY == 0);
    }

    public MapperSuperclass(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY, int maxI, int threads) {
        this.numer = numer;
        this.denom = denom;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
        this.mirrorXaxis = (centreY == 0);
        this.maxIterations = maxI;
        this.noOfThreads = threads;
    }
    
    public MapperSuperclass(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY, int maxI, ArrayList<Integer> operatorString) {
        this.numer = numer;
        this.denom = denom;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
        this.mirrorXaxis = (centreY == 0);
        this.maxIterations = maxI;
        this.operatorString = operatorString;
    }

    public Complex Transform(Complex z) {
        if(operatorString.size()>0){
            for(int i=0;i<operatorString.size();i++){
                z=z.operator(operatorString.get(i));
            }
            return z;
        }
        //You can write your custom transformation below:
        //Rember to change the InverseTransform too if you are finding buddha
        return z;
    }

    //This always has to be inverse of Transform
    //Used in MapperBuddha
    public Complex InverseTransform(Complex z) {
        if(operatorString.size()>0){
            for(int i=0;i<operatorString.size();i++){
                z=z.operator(-operatorString.get((operatorString.size()-i-1)));
            }
            return z;
        }
        //You can write your custom inverse transformation below:
        return z;
    }

    public BufferedImage getMap() {
        return map;
    }
    
    public double getXCoordinateFromPixel(int pixelX){
        return (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * pixelX / sizeY) + centreX);
        //return (-limX + (2.0 * limX * pixelX / sizeX) + centreX);
    }
    
    public double getYCoordinateFromPixel(int pixelY){
        return (limY - (2.0 * limY * pixelY / sizeY) + centreY);
    }
    
    public int getPixelFromXCoordinate(double xCoordinate){
        return (int) Math.round((xCoordinate + limX * (1.0 * sizeX / sizeY) - centreX) * (sizeY / 2.0 / limX));
    }
    
    public int getPixelFromYCoordinate(double yCoordinate){
        return (int) Math.round((-yCoordinate + limY + centreY) * (sizeY / 2.0 / limY));
    }

    public int getRedFromN(double n) {
        return (int) (Math.sin(n / Math.PI / 2 / 4) * 100 + 150);
    }

    public int getGreenFromN(double n) {
        return (int) (Math.sin(n / Math.PI / 4) * 100 + 150);
    }

    public int getBlueFromN(double n) {
        return (int) (Math.cos(n / Math.PI * 2 / 4) * 100 + 150);
    }
    
    public void savePNG(final BufferedImage bi, final String path) {
        try {
            RenderedImage rendImage = bi;
            //ImageIO.write(rendImage, "bmp", new File(path));
            ImageIO.write(rendImage, "PNG", new File(path));
            //ImageIO.write(rendImage, "jpeg", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
