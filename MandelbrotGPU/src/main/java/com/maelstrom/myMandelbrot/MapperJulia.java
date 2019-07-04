/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maelstrom.myMandelbrot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chris
 */
public class MapperJulia extends MapperSuperclass {

    public MapperJulia(int sizeX, int sizeY) {
        super(sizeX, sizeY);
    }

    public MapperJulia(int sizeX, int sizeY, double limX, double limY) {
        super(sizeX, sizeY, limX, limY);
    }

    public MapperJulia(int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        super(sizeX, sizeY, limX, limY, centreX, centreY);
    }

    public MapperJulia(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        super(numer, denom, sizeX, sizeY, limX, limY, centreX, centreY);
    }

    public MapperJulia(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY, int maxI, int threads) {
        super(numer, denom, sizeX, sizeY, limX, limY, centreX, centreY, maxI, threads);
    }

    public MapperJulia(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY, int maxI, ArrayList<Integer> operatorString) {
        super(numer, denom, sizeX, sizeY, limX, limY, centreX, centreY, maxI, operatorString);
    }

    public BufferedImage createImage(double x0, double y0) {
        if (mirrorXaxis) {
            return createImageMirror(x0, y0);
        } else {
            return createImageNoMirror(x0, y0);
        }
    }

    public BufferedImage createImageNoMirror(double x0, double y0) {
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        threadIterator = 0;
        Thread[] column = new Thread[noOfThreads];
        for (int i = 0; i < noOfThreads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    threadIterator++;
                    int threadID = threadIterator - 1;

                    double num;
                    int red, green, blue;
                    double sampleCoordinateX, sampleCoordinateY, xCoordinate, yCoordinate;
                    Complex transformedCoordinate;

                    for (int pixelX = threadID; pixelX < sizeX; pixelX += noOfThreads) {
                        for (int pixelY = 0; pixelY < sizeY; pixelY++) {
                            sampleCoordinateX = getXCoordinateFromPixel(pixelX);
                            sampleCoordinateY = getYCoordinateFromPixel(pixelY);
                            transformedCoordinate = Transform(new Complex(sampleCoordinateX, sampleCoordinateY, false));
                            xCoordinate = transformedCoordinate.getRe();
                            yCoordinate = transformedCoordinate.getIm();
                            num = (double) PartOfJulia(x0, y0, xCoordinate, yCoordinate);
                            if (num == maxIterations) {
                                map.setRGB(pixelX, pixelY, Color.BLACK.getRGB());
                            } else {
                                red = getRedFromN(num);
                                green = getGreenFromN(num);
                                blue = getBlueFromN(num);
                                map.setRGB(pixelX, pixelY, new Color(red, green, blue).hashCode());
                            }
                        }
                    }
                }
            });
            column[i].setPriority(Thread.MIN_PRIORITY);
            column[i].start();
        }

        for (int i = 0; i < noOfThreads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MapperSuperclass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    public BufferedImage createImageMirror(double x0, double y0) {
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        threadIterator = 0;
        Thread[] column = new Thread[noOfThreads];
        for (int i = 0; i < noOfThreads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    threadIterator++;
                    int threadID = threadIterator - 1;

                    double num;
                    int red, green, blue;
                    double sampleCoordinateX, sampleCoordinateY, xCoordinate, yCoordinate;
                    Complex transformedCoordinate;

                    for (int pixelX = threadID; pixelX < sizeX; pixelX += noOfThreads) {
                        for (int pixelY = 0; pixelY < sizeY; pixelY++) {
                            sampleCoordinateX = getXCoordinateFromPixel(pixelX);
                            sampleCoordinateY = getYCoordinateFromPixel(pixelY);
                            transformedCoordinate = Transform(new Complex(sampleCoordinateX, sampleCoordinateY, false));
                            xCoordinate = transformedCoordinate.getRe();
                            yCoordinate = transformedCoordinate.getIm();
                            num = (double) PartOfJulia(x0, y0, xCoordinate, yCoordinate);
                            if (num == maxIterations) {
                                map.setRGB(pixelX, sizeY - pixelY - 1, Color.BLACK.getRGB());
                                map.setRGB(pixelX, pixelY, Color.BLACK.getRGB());
                            } else {
                                red = getRedFromN(num);
                                green = getGreenFromN(num);
                                blue = getBlueFromN(num);
                                map.setRGB(pixelX, sizeY - pixelY - 1, new Color(red, green, blue).hashCode());
                                map.setRGB(pixelX, pixelY, new Color(red, green, blue).hashCode());
                            }
                        }
                    }
                }
            });
            column[i].setPriority(Thread.MIN_PRIORITY);
            column[i].start();
        }

        for (int i = 0; i < noOfThreads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MapperSuperclass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    public int PartOfJulia(double x0, double y0, double cx, double cy) {
        Complex tot = new Complex(x0, y0, false);
        int i = 0;
        while (i < maxIterations && tot.getR() < 10) {
            tot = tot.power(numer, denom);
            tot = tot.add(cx, cy);
            i++;
        }
        return i;
    }
}
