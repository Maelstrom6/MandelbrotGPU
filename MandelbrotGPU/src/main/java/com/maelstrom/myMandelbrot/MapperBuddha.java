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
public class MapperBuddha extends MapperSuperclass{
    public MapperBuddha(int sizeX, int sizeY) {
        super(sizeX, sizeY);
    }

    public MapperBuddha(int sizeX, int sizeY, double limX, double limY) {
        super(sizeX, sizeY, limX, limY);
    }

    public MapperBuddha(int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        super(sizeX, sizeY, limX, limY, centreX, centreY);
    }

    public MapperBuddha(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        super(numer, denom, sizeX, sizeY, limX, limY, centreX, centreY);
    }

    public MapperBuddha(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY, int maxI, int threads) {
        super(numer, denom, sizeX, sizeY, limX, limY, centreX, centreY, maxI, threads);
    }

    public MapperBuddha(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY, int maxI, ArrayList<Integer> operatorString) {
        super(numer, denom, sizeX, sizeY, limX, limY, centreX, centreY, maxI, operatorString);
    }

    public BufferedImage createImage() {
        if (mirrorXaxis) {
            return createImageMirror();
        } else {
            return createImageNoMirror();
        }
    }
    
    public BufferedImage createImageNoMirror() {
        int[][] mapR = new int[sizeX][sizeY];
        int[][] mapG = new int[sizeX][sizeY];
        int[][] mapB = new int[sizeX][sizeY];
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        threadIterator = 0;
        Thread[] column = new Thread[noOfThreads];
        for (int i = 0; i < noOfThreads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    threadIterator++;
                    int threadID = threadIterator-1;
                    
                    ArrayList<Complex> visitedCoordinates = new ArrayList();
                    double startingxCoordinate, startingyCoordinate, xCoordinate,yCoordinate;
                    Complex coordinate;
                    
                    for (int pixelX = threadID; pixelX < sizeX; pixelX += noOfThreads) {
                        for (int pixelY = 0; pixelY < sizeY; pixelY++) {
                            startingxCoordinate = getXCoordinateFromPixel(pixelX);
                            startingyCoordinate = getYCoordinateFromPixel(pixelY);
                            coordinate = Transform(new Complex(startingxCoordinate, startingyCoordinate, false));
                            xCoordinate = coordinate.getRe();
                            yCoordinate = coordinate.getIm();
                            visitedCoordinates = PartOfBuddha(xCoordinate, yCoordinate);
                            for (int j = 0; j < visitedCoordinates.size(); j++) {
                                visitedCoordinates.set(j, InverseTransform(visitedCoordinates.get(j)));
                                int xPixel = getPixelFromXCoordinate(visitedCoordinates.get(j).getRe());
                                int yPixel = getPixelFromYCoordinate(visitedCoordinates.get(j).getIm());
                                if (0 <= xPixel && xPixel < sizeX && 0 <= yPixel && yPixel < sizeX) {
                                    if (j < 0.01 * maxIterations) {
                                        mapB[xPixel][yPixel]++;
                                    }
                                    if (j < 0.1 * maxIterations) {
                                        mapG[xPixel][yPixel]++;
                                    }
                                    mapR[xPixel][yPixel]++;
                                }
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
        int maxR = FindMax(mapR);
        int maxG = FindMax(mapG);
        int maxB = FindMax(mapB);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                map.setRGB(x, y, new Color(255 * mapR[x][y] / maxR, 255 * mapG[x][y] / maxG, 255 * mapB[x][y] / maxB).hashCode());
            }
        }
        return map;
    }
    
    public BufferedImage createImageMirror() {
        int[][] mapR = new int[sizeX][sizeY];
        int[][] mapG = new int[sizeX][sizeY];
        int[][] mapB = new int[sizeX][sizeY];
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        threadIterator = 0;
        Thread[] column = new Thread[noOfThreads];
        for (int i = 0; i < noOfThreads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    threadIterator++;
                    int threadID = threadIterator-1;
                    
                    ArrayList<Complex> visitedCoordinates = new ArrayList();
                    double startingxCoordinate, startingyCoordinate, xCoordinate,yCoordinate;
                    Complex coordinate;
                    
                    for (int pixelX = threadID; pixelX < sizeX; pixelX += noOfThreads) {
                        for (int pixelY = 0; pixelY < sizeY; pixelY++) {
                            startingxCoordinate = getXCoordinateFromPixel(pixelX);
                            startingyCoordinate = getYCoordinateFromPixel(pixelY);
                            coordinate = Transform(new Complex(startingxCoordinate, startingyCoordinate, false));
                            xCoordinate = coordinate.getRe();
                            yCoordinate = coordinate.getIm();
                            visitedCoordinates = PartOfBuddha(xCoordinate, yCoordinate);
                            for (int j = 0; j < visitedCoordinates.size(); j++) {
                                visitedCoordinates.set(j, InverseTransform(visitedCoordinates.get(j)));
                                int xPixel = getPixelFromXCoordinate(visitedCoordinates.get(j).getRe());
                                int yPixel = getPixelFromYCoordinate(visitedCoordinates.get(j).getIm());
                                if (0 <= xPixel && xPixel < sizeX && 0 <= yPixel && yPixel < sizeX) {
                                    if (j < 0.01 * maxIterations) {
                                        mapB[xPixel][yPixel]++;
                                    }
                                    if (j < 0.1 * maxIterations) {
                                        mapG[xPixel][yPixel]++;
                                    }
                                    mapR[xPixel][yPixel]++;
                                }
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
        int maxR = FindMaxMirror(mapR);
        int maxG = FindMaxMirror(mapG);
        int maxB = FindMaxMirror(mapB);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                map.setRGB(x, y, new Color(
                        255 * (mapR[x][y] + mapR[x][sizeY - y - 1]) / maxR,
                        255 * (mapG[x][y] + mapG[x][sizeY - y - 1]) / maxG,
                        255 * (mapB[x][y] + mapB[x][sizeY - y - 1]) / maxB).hashCode());
            }
        }
        return map;
    }
    
    public ArrayList<Complex> PartOfBuddha(double x, double y) {
        ArrayList<Complex> points = new ArrayList();
        Complex tot = new Complex(x, y, false);
        int i = 0;
        while (i < maxIterations && tot.getR() < 2) {
            tot = tot.power(numer, denom);
            tot = tot.add(x, y);
            points.add(tot);
            i++;
        }
        if (i == maxIterations) {
            return new ArrayList();
        } else {
            return points;
        }
    }
    
    public int FindMax(int[][] a) {
        int max = 0;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (a[x][y] > max) {
                    max = a[x][y];
                }
            }
        }
        return max;
    }

    public int FindMaxMirror(int[][] a) {
        int max = 0;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY / 2; y++) {
                if (a[x][y] + a[x][sizeY - y - 1] > max) {
                    max = a[x][y] + a[x][sizeY - y - 1];
                }
            }
        }
        return max;
    }
}
