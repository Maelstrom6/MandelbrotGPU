package mandelbrot3;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;//for quality: increase num on lines 41,46,47,95

/**
 *
 * @author ChrisPC
 */
public class Mand {

    private int numer = 2, denom = 1;
    private int sizeX, sizeY;
    private double limX, limY;
    private double centreX, centreY;
    private boolean mirror;
    private BufferedImage map;
    private int maxI = 5000;//500
    private int threads = 4;
    private int iter;
    private ArrayList<Integer> operatorString;//A list of operator id's in RPN

    public Mand(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        limX = 2;
        limY = 2;
        centreX = 0;
        centreY = 0;
        mirror = true;
    }

    public Mand(int sizeX, int sizeY, double xLim, double yLim) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = xLim;
        this.limY = yLim;
        centreX = 0;
        centreY = 0;
        mirror = true;
    }

    public Mand(int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
        this.mirror = (centreY == 0);
    }

    public Mand(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        this.numer = numer;
        this.denom = denom;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
        this.mirror = (centreY == 0);
    }

    public Mand(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY, int maxI, int threads) {
        this.numer = numer;
        this.denom = denom;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
        this.mirror = (centreY == 0);
        this.maxI = maxI;
        this.threads = threads;
    }

    public Mand(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY, int maxI, ArrayList<Integer> operatorString) {
        this.numer = numer;
        this.denom = denom;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
        this.mirror = (centreY == 0);
        this.maxI = maxI;
        this.operatorString = operatorString;
    }

    public BufferedImage mapAutoMirror() {

        if (mirror) {
            return mapMirror();
        } else {
            return mapNoMirror();
        }
    }
    
    public BufferedImage mapNoMirror() {
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter = 0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id = iter;
                    iter++;
                    //int num;
                    double num;
                    int scaler, scaleg, scaleb;
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY; i++) {
                            double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
                            double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
                            Myi coordinate = (Transform(new Myi(startingxCoordinate, startingyCoordinate, false)));
                            //double xCoordinate = TransformX(startingxCoordinate, startingyCoordinate);
                            //double yCoordinate = TransformY(startingxCoordinate, startingyCoordinate);
                            double xCoordinate = coordinate.getRe();
                            double yCoordinate = coordinate.getIm();
                            num = (double) (PartOfMand(xCoordinate, yCoordinate));
                            //num = (int) (PartSet((-limX + (2.0 * limX * x / sizeX) + centreX), (limY - (2.0 * limY * i / sizeY)+centreY)));//-(2-xLim) for x to move it left
                            if (num == maxI) {//need to change this if yuou change i<100
                                map.setRGB(x, i, Color.BLACK.getRGB());
                            } else {
                                scaler = getRedFromN(num);//divide everything by 4 for smoother transition
                                scaleg = getGreenFromN(num);
                                scaleb = getBlueFromN(num);
                                map.setRGB(x, i, new Color(scaler, scaleg, scaleb).hashCode());
                            }
                        }
                    }
                }
            });
            column[i].setPriority(Thread.MIN_PRIORITY);
            column[i].start();
        }
        for (int i = 0; i < threads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    public BufferedImage mapChangedFn() {
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter = 0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id = iter;
                    iter++;
                    //int num;
                    double num;
                    int scaler, scaleg, scaleb;
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY; i++) {
                            double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
                            double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
                            Myi coordinate = (Transform(new Myi(startingxCoordinate, startingyCoordinate, false)));
                            //double xCoordinate = TransformX(startingxCoordinate, startingyCoordinate);
                            //double yCoordinate = TransformY(startingxCoordinate, startingyCoordinate);
                            double xCoordinate = coordinate.getRe();
                            double yCoordinate = coordinate.getIm();
                            num = (double) (PartOfMyMand(xCoordinate, yCoordinate));
                            //num = (int) (PartSet((-limX + (2.0 * limX * x / sizeX) + centreX), (limY - (2.0 * limY * i / sizeY)+centreY)));//-(2-xLim) for x to move it left
                            if (num == maxI) {//need to change this if yuou change i<100
                                map.setRGB(x, i, Color.BLACK.getRGB());
                            } else {
                                scaler = getRedFromN(num);//divide everything by 4 for smoother transition
                                scaleg = getGreenFromN(num);
                                scaleb = getBlueFromN(num);
                                map.setRGB(x, i, new Color(scaler, scaleg, scaleb).hashCode());
                            }
                        }
                    }
                }
            });
            column[i].setPriority(Thread.MIN_PRIORITY);
            column[i].start();
        }
        for (int i = 0; i < threads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }
    
    public double PartOfMyMand(double x, double y){
        Myi c = new Myi(x, y, false);
        Myi zn = c;
        int i=1;
        double coeff;
        while (i < maxI && zn.getR() < 200) {
            //coeff = Math.pow(2, i)/fib(i);
            zn = zn.power(2).multiply(1/coeff(i)).add(c.multiply(1-1/coeff(i)));
            i++;
        }
        return i;
    }
    
    public double coeff(int i){
        double phi = (Math.sqrt(5)+1)/2;
        //double a = Math.pow(phi/2, i);
        //double b = Math.pow(1/phi/2, i);
        //return (a-b)/Math.sqrt(5);
        return 1.0/fib(i)*Math.pow(2, i);
    }
    
    public int fib(int n){
        
        double phi = (Math.sqrt(5)+1)/2;
        return (int)Math.round((Math.pow(phi, n) - Math.pow(-phi, -n))/Math.sqrt(5));
    }

    public synchronized BufferedImage doNotUseMore() {
        map = new BufferedImage(1, sizeY, BufferedImage.TYPE_INT_RGB);
        double num;
        int scaler, scaleg, scaleb;
        int x = 151;
        int i = 249;
        System.out.println("y = " + i);
        double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
        double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
        System.out.println(startingxCoordinate + " " + startingyCoordinate);
        Myi starting = new Myi(startingxCoordinate, startingyCoordinate, false);
        Myi thing = new Myi(7.05385662596046E15, 2.3089524255209666,false);
        
        System.out.println("Assigned coordinate");
        Myi coordinate = starting.tan();
        System.out.println(coordinate);
        coordinate = coordinate.sin();
        System.out.println(coordinate);
        coordinate = coordinate.exp();
        System.out.println(coordinate);
        //Myi coordinate = (Transform(new Myi(startingxCoordinate, startingyCoordinate, false)));
        System.out.println("Done transform");
        //double xCoordinate = TransformX(startingxCoordinate, startingyCoordinate);
        //double yCoordinate = TransformY(startingxCoordinate, startingyCoordinate);
        double xCoordinate = coordinate.getRe();
        double yCoordinate = coordinate.getIm();
        num = (double) (PartOfMandSmooth(xCoordinate, yCoordinate));
        //num = (int) (PartOfMand((-limX * (1.0 * sizeX / sizeY) + (2.0 * limY * x / sizeY) + centreX), (limY - (2.0 * limY * i / sizeY))));
        //num = (int) (PartSet((-limX + (2.0 * limX * x / sizeX) + centreX), (limY - (2.0 * limY * i / sizeY))));//-(2-xLim) for x to move it left
        if (num == maxI) {//need to change this if yuou change i<100
            map.setRGB(0, sizeY - i - 1, Color.BLACK.getRGB());
            map.setRGB(0, i, Color.BLACK.getRGB());
        } else {
            scaler = getRedFromN(num);//divide everything by 4 for smoother transition
            scaleg = getGreenFromN(num);
            scaleb = getBlueFromN(num);
            map.setRGB(0, sizeY - i - 1, new Color(scaler, scaleg, scaleb).hashCode());
            map.setRGB(0, i, new Color(scaler, scaleg, scaleb).hashCode());
        }
        System.out.println("Done looping y = " + i);

        return map;
    }

    public synchronized BufferedImage doNotUse() {
        map = new BufferedImage(1, sizeY, BufferedImage.TYPE_INT_RGB);
        double num;
        int scaler, scaleg, scaleb;
        int x = 151;
        for (int i = 0; i < sizeY / 2; i++) {
            System.out.println("y = " + i);
            double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
            double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
            Myi coordinate = (Transform(new Myi(startingxCoordinate, startingyCoordinate, false)));
            //double xCoordinate = TransformX(startingxCoordinate, startingyCoordinate);
            //double yCoordinate = TransformY(startingxCoordinate, startingyCoordinate);
            double xCoordinate = coordinate.getRe();
            double yCoordinate = coordinate.getIm();
            num = (double) (PartOfMandSmooth(xCoordinate, yCoordinate));
            //num = (int) (PartOfMand((-limX * (1.0 * sizeX / sizeY) + (2.0 * limY * x / sizeY) + centreX), (limY - (2.0 * limY * i / sizeY))));
            //num = (int) (PartSet((-limX + (2.0 * limX * x / sizeX) + centreX), (limY - (2.0 * limY * i / sizeY))));//-(2-xLim) for x to move it left
            if (num == maxI) {//need to change this if yuou change i<100
                map.setRGB(0, sizeY - i - 1, Color.BLACK.getRGB());
                map.setRGB(0, i, Color.BLACK.getRGB());
            } else {
                scaler = getRedFromN(num);//divide everything by 4 for smoother transition
                scaleg = getGreenFromN(num);
                scaleb = getBlueFromN(num);
                map.setRGB(0, sizeY - i - 1, new Color(scaler, scaleg, scaleb).hashCode());
                map.setRGB(0, i, new Color(scaler, scaleg, scaleb).hashCode());
            }
            System.out.println("Done looping y = " + i);
        }
        return map;
    }

    public synchronized BufferedImage mapMirror() {
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter = 0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id = iter;
                    iter++;
                    double num;
                    int scaler, scaleg, scaleb;
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY / 2; i++) {
                            double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
                            double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
                            Myi coordinate = (Transform(new Myi(startingxCoordinate, startingyCoordinate, false)));
                            //double xCoordinate = TransformX(startingxCoordinate, startingyCoordinate);
                            //double yCoordinate = TransformY(startingxCoordinate, startingyCoordinate);
                            double xCoordinate = coordinate.getRe();
                            double yCoordinate = coordinate.getIm();
                            num = (double) (PartOfMandSmooth(xCoordinate, yCoordinate));
                            //num = (int) (PartOfMand((-limX * (1.0 * sizeX / sizeY) + (2.0 * limY * x / sizeY) + centreX), (limY - (2.0 * limY * i / sizeY))));
                            //num = (int) (PartSet((-limX + (2.0 * limX * x / sizeX) + centreX), (limY - (2.0 * limY * i / sizeY))));//-(2-xLim) for x to move it left
                            if (num == maxI) {//need to change this if yuou change i<100
                                map.setRGB(x, sizeY - i - 1, Color.BLACK.getRGB());
                                map.setRGB(x, i, Color.BLACK.getRGB());
                            } else {
                                scaler = getRedFromN(num);//divide everything by 4 for smoother transition
                                scaleg = getGreenFromN(num);
                                scaleb = getBlueFromN(num);
                                map.setRGB(x, sizeY - i - 1, new Color(scaler, scaleg, scaleb).hashCode());
                                map.setRGB(x, i, new Color(scaler, scaleg, scaleb).hashCode());
                            }
                        }
                    }
                }
            });
            column[i].setPriority(Thread.MIN_PRIORITY);
            column[i].start();
        }
        for (int i = 0; i < threads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    public BufferedImage mapJulia() {
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter = 0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id = iter;
                    iter++;
                    double num;
                    int scaler, scaleg, scaleb;
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY / 2; i++) {
                            double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
                            double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
                            Myi coordinate = (Transform(new Myi(startingxCoordinate, startingyCoordinate, false)));
                            //double xCoordinate = TransformX(startingxCoordinate, startingyCoordinate);
                            //double yCoordinate = TransformY(startingxCoordinate, startingyCoordinate);
                            double xCoordinate = coordinate.getRe();
                            double yCoordinate = coordinate.getIm();
                            num = (double) (PartOfJulia(xCoordinate, yCoordinate, centreX, centreY));
                            //num = (int) (PartOfJulia((-limX * (1.0 * sizeX / sizeY) + (2.0 * limY * x / sizeY)), (limY - (2.0 * limY * i / sizeY)), centreX, centreY));
                            //num = (int) (PartSet((-limX + (2.0 * limX * x / sizeX) + centreX), (limY - (2.0 * limY * i / sizeY))));//-(2-xLim) for x to move it left
                            if (num == maxI) {//need to change this if yuou change i<100
                                map.setRGB(x, sizeY - i - 1, Color.BLACK.getRGB());
                                map.setRGB(x, i, Color.BLACK.getRGB());
                            } else {
                                scaler = getRedFromN(num);//divide everything by 4 for smoother transition
                                scaleg = getGreenFromN(num);
                                scaleb = getBlueFromN(num);
                                map.setRGB(x, sizeY - i - 1, new Color(scaler, scaleg, scaleb).hashCode());
                                map.setRGB(x, i, new Color(scaler, scaleg, scaleb).hashCode());
                            }
                        }
                    }
                }
            });
            column[i].setPriority(Thread.MIN_PRIORITY);
            column[i].start();
        }
        for (int i = 0; i < threads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    public BufferedImage mapBuddha() {
        int[][] mapR = new int[sizeX][sizeY];
        int[][] mapG = new int[sizeX][sizeY];
        int[][] mapB = new int[sizeX][sizeY];
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter = 0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id = iter;
                    iter++;
                    //int num;
                    ArrayList<Myi> coords = new ArrayList();
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY; i++) {
                            double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
                            double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
                            Myi coordinate = Transform(new Myi(startingxCoordinate, startingyCoordinate, false));
                            double xCoordinate = coordinate.getRe();
                            double yCoordinate = coordinate.getIm();
                            coords = (PartOfBuddha(xCoordinate, yCoordinate));
                            for (int j = 0; j < coords.size(); j++) {
                                coords.set(j, InverseTransform(coords.get(j)));
                                int xPixel = (int) Math.round((coords.get(j).getRe() + limX * (1.0 * sizeX / sizeY) - centreX) * (sizeY / 2.0 / limX));
                                int yPixel = (int) Math.round((-coords.get(j).getIm() + limY + centreY) * (sizeY / 2.0 / limY));
                                if (0 <= xPixel && xPixel < sizeX && 0 <= yPixel && yPixel < sizeX) {
                                    if (j < 0.01 * maxI) {
                                        mapB[xPixel][yPixel]++;
                                    }
                                    if (j < 0.1 * maxI) {
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
        for (int i = 0; i < threads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
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

    public BufferedImage mapBuddhaMirrorNotRand() {
        int[][] mapR = new int[sizeX][sizeY];
        int[][] mapG = new int[sizeX][sizeY];
        int[][] mapB = new int[sizeX][sizeY];
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter = 0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id = iter;
                    iter++;
                    //int num;
                    ArrayList<Myi> coords = new ArrayList();
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY / 2; i++) {
                            //Generate a raondom coordinate
                            double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
                            double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
                            Myi coordinate = Transform(new Myi(startingxCoordinate, startingyCoordinate, false));
                            double xCoordinate = coordinate.getRe();
                            double yCoordinate = coordinate.getIm();
                            coords = (PartOfBuddha(xCoordinate, yCoordinate));
                            for (int j = 0; j < coords.size(); j++) {
                                coords.set(j, InverseTransform(coords.get(j)));
                                int xPixel = (int) Math.round((coords.get(j).getRe() + limX * (1.0 * sizeX / sizeY) - centreX) * (sizeY / 2.0 / limX));
                                int yPixel = (int) Math.round((-coords.get(j).getIm() + limY + centreY) * (sizeY / 2.0 / limY));
                                if (0 <= xPixel && xPixel < sizeX && 0 <= yPixel && yPixel < sizeX) {
                                    if (j < 0.01 * maxI) {
                                        mapB[xPixel][yPixel]++;
                                    }
                                    if (j < 0.1 * maxI) {
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
        for (int i = 0; i < threads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int maxR = FindMaxMirror(mapR);
        int maxG = FindMaxMirror(mapG);
        int maxB = FindMaxMirror(mapB);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                map.setRGB(x, y, new Color(
                        (int) (255 * Math.sqrt(1.0 * (mapR[x][y] + mapR[x][sizeY - y - 1]) / maxR)),
                        (int) (255 * Math.sqrt(1.0 * (mapG[x][y] + mapG[x][sizeY - y - 1]) / maxG)),
                        (int) (255 * Math.sqrt(1.0 * (mapB[x][y] + mapB[x][sizeY - y - 1]) / maxB))).hashCode());
            }
        }
        return map;
    }

    public BufferedImage mapBuddhaMirrorRand() {
        int[][] mapR = new int[sizeX][sizeY];
        int[][] mapG = new int[sizeX][sizeY];
        int[][] mapB = new int[sizeX][sizeY];
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter = 0;
        int multiplier = 2;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id = iter;
                    iter++;
                    //int num;
                    ArrayList<Myi> coords = new ArrayList();
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY / 2; i++) {
                            for (int w = 0; w < multiplier; w++) {
                                //Generate a raondom coordinate
                                double startingxCoordinate = Math.random() * 2 * limX - limX + centreX;
                                double startingyCoordinate = Math.random() * 2 * limY - limY + centreY;
                                Myi coordinate = Transform(new Myi(startingxCoordinate, startingyCoordinate, false));
                                double xCoordinate = coordinate.getRe();
                                double yCoordinate = coordinate.getIm();
                                coords = (PartOfBuddha(xCoordinate, yCoordinate));
                                for (int j = 0; j < coords.size(); j++) {
                                    coords.set(j, InverseTransform(coords.get(j)));
                                    int xPixel = (int) Math.round((coords.get(j).getRe() + limX * (1.0 * sizeX / sizeY) - centreX) * (sizeY / 2.0 / limX));
                                    int yPixel = (int) Math.round((-coords.get(j).getIm() + limY + centreY) * (sizeY / 2.0 / limY));
                                    if (0 <= xPixel && xPixel < sizeX && 0 <= yPixel && yPixel < sizeX) {
                                        if (j < 0.01 * maxI) {
                                            mapB[xPixel][yPixel]++;
                                        }
                                        if (j < 0.1 * maxI) {
                                            mapG[xPixel][yPixel]++;
                                        }
                                        mapR[xPixel][yPixel]++;
                                    }
                                }
                            }
                        }
                    }
                }
            });
            column[i].setPriority(Thread.MIN_PRIORITY);
            column[i].start();
        }
        for (int i = 0; i < threads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public BufferedImage mapDistanceBuddha() {
        int[][] mapR = new int[sizeX][sizeY];
        int[][] mapG = new int[sizeX][sizeY];
        int[][] mapB = new int[sizeX][sizeY];
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter = 0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id = iter;
                    iter++;
                    //int num;
                    ArrayList<Myi> coords = new ArrayList();
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY / 2; i++) {
                            //Generate a raondom coordinate
                            double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
                            double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
                            Myi coordinate = Transform(new Myi(startingxCoordinate, startingyCoordinate, false));
                            double xCoordinate = coordinate.getRe();
                            double yCoordinate = coordinate.getIm();
                            coords = (PartOfBuddha(xCoordinate, yCoordinate));
                            
                            if(coords.size()!=0){
                                coords.set(0, InverseTransform(coords.get(0)));
                            }
                            for (int j = 0; j < coords.size()-1; j++) {
                                coords.set(j+1, InverseTransform(coords.get(j+1)));
                                int xPixel = (int) Math.round((coords.get(j).getRe() + limX * (1.0 * sizeX / sizeY) - centreX) * (sizeY / 2.0 / limX));
                                int yPixel = (int) Math.round((-coords.get(j).getIm() + limY + centreY) * (sizeY / 2.0 / limY));
                                if (0 <= xPixel && xPixel < sizeX && 0 <= yPixel && yPixel < sizeX) {
                                    if (j < 0.01 * maxI) {
                                        mapB[xPixel][yPixel]=100000*(int)calcDistance(coords.get(j), coords.get(j+1));
                                    }
                                    if (j < 0.1 * maxI) {
                                        mapG[xPixel][yPixel]=100000*(int)calcDistance(coords.get(j), coords.get(j+1));
                                    }
                                    mapR[xPixel][yPixel]=100000*(int)calcDistance(coords.get(j), coords.get(j+1));
                                }
                            }

                        }
                    }
                }
            });
            column[i].setPriority(Thread.MIN_PRIORITY);
            column[i].start();
        }
        for (int i = 0; i < threads; i++) {
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int maxR = FindMaxMirror(mapR);
        int maxG = FindMaxMirror(mapG);
        int maxB = FindMaxMirror(mapB);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                map.setRGB(x, y, new Color(
                        (int) (255 * Math.sqrt(1.0 * (mapR[x][y] + mapR[x][sizeY - y - 1]) / maxR)),
                        (int) (255 * Math.sqrt(1.0 * (mapG[x][y] + mapG[x][sizeY - y - 1]) / maxG)),
                        (int) (255 * Math.sqrt(1.0 * (mapB[x][y] + mapB[x][sizeY - y - 1]) / maxB))).hashCode());
            }
        }
        return map;
    }
    
    public double calcDistance(Myi a, Myi b){
        return Math.sqrt((a.getRe()-b.getRe())*(a.getRe()-b.getRe()) + (a.getIm()-b.getIm())*(a.getIm()-b.getIm()));
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

    public int PartOfMand(double a, double y) {
        Myi tot = new Myi(a, y, false);
        int i = 0;
        while (i < maxI && tot.getR() < 2) {
            tot = tot.power(numer, denom);
            tot = tot.add(a, y);
            i++;
        }
        return i;
    }

    public double PartOfMandSmooth(double a, double y) {
        Myi tot = new Myi(a, y, false);
        //Myi tot = new Myi(0.5,0,true);
        //Myi c = new Myi(a, y, false);
        int i = 0;
        while (i < maxI && tot.getR() < 2) {
            tot = tot.power(numer, denom);
            tot = tot.add(a, y);
            //tot = c.add(tot.multiply(Myi.ONE.add(tot.ainverse())));
            i++;
        }
        double logzn = Math.log(tot.getR());
        double nu = Math.log(logzn / Math.log(2)) / Math.log(2);
        if (i == maxI) {
            return maxI;
        } else {
            return i - nu;
        }
    }

    public ArrayList<Myi> PartOfBuddha(double x, double y) {
        ArrayList<Myi> points = new ArrayList();
        Myi tot = new Myi(x, y, false);
        int i = 0;
        while (i < maxI && tot.getR() < 2) {
            tot = tot.power(numer, denom);
            tot = tot.add(x, y);
            points.add(tot);
            i++;
        }
        if (i == maxI) {
            return new ArrayList();
        } else {
            return points;
        }
    }

    public int PartOfJulia(double a, double y, double cx, double cy) {
        Myi tot = new Myi(a, y, false);
        int i = 0;
        while (i < maxI && tot.getR() < 10) {
            tot = tot.power(numer, denom);
            tot = tot.add(cx, cy);
            i++;
        }
        return i;
    }

    public double TransformX(double x, double y) {
        //Myi c=new Myi(x, y, false);
        //Myi num = new Myi(x, y, true); // xexp[yi]
        //Myi num = c.power(3, 2); // c^(3/2)
        //return num.getRe()/(num.getR()*num.getR());

        //Myi num = new Myi(Math.exp(x), y, true); // exp[c]
        //return -num.getRe();
        //return x; // no transform
        //return x/(x*x+y*y); // Inverse transform
        //return (x*x-y*y)/(x*x*x*x + 2*x*x*y*y + y*y*y*y); // Inverse square
        //return (x*x - y*y + x)/(x*x*x*x + 2*x*x*y*y + y*y*y*y); // 1/p^2 + p
        //return Math.sin(x)*Math.cosh(y); // sinp
        /*
        // arctan c
        double a = (1 - x*x - y*y)/(x*x + (1+y)*(1+y));
        double b = (2*x)/(x*x + (1+y)*(1+y));
        Myi ln = ln(a, b);
        ln = ln.multiply(0.5);
        return ln.getIm();
         */
 /*
        // ln ((i-c)/(i+c))
        double a = (1 - x*x - y*y)/(x*x + (1+y)*(1+y));
        double b = (2*x)/(x*x + (1+y)*(1+y));
        return ln(a, b).getRe();
         */
 /*
        // ln c
        Myi num = new Myi(x, y, false);
        return num.ln().minverse().getRe();
         */
        //Myi num = new Myi(x, y, false);
        //return num.acos().getRe();
        /*
        //tear if complx is more than 4, use inverse square, otherwise use lni-c/i+c
        if (y > 0.5) {//2.5 wide from centre when size is doubled
            y = y - 3;
            x=0.5*x;
            y=0.5*y;
            return (x * x - y * y) / (x * x * x * x + 2 * x * x * y * y + y * y * y * y);
        } else {
            y = -y-4;
            x=2*x;
            y=2*y;
            double a = (1 - x * x - y * y) / (x * x + (1 + y) * (1 + y));
            double b = (2 * x) / (x * x + (1 + y) * (1 + y));
            return ln(a, b).getRe();
        }
         */
        //Mobius transform
        double a = 1, b = 1, d = 1;
        double c = (a * d - 1) / b;
        return ((a * x + b) * (c * x + d) + a * c * y * y) / ((c * x + d) * (c * x + d) + c * c * y * y);
    }

    public double TransformY(double x, double y) {
        //Myi c=new Myi(x, y, false);
        //Myi num = new Myi(x, y, true); // xexp[yi]
        //Myi numX = c.power(3, 2); // c^(3/2)
        //return num.getIm()/(num.getR()*num.getR());

        //Myi num = new Myi(Math.exp(x), y, true); // exp[c]
        //return num.getIm();
        //return y; // no transform
        //return y/(x*x+y*y); // Inverse transform
        //return (2*x*y)/(x*x*x*x + 2*x*x*y*y + y*y*y*y); // Inverse square
        //return (2*x*y + y)/(x*x*x*x + 2*x*x*y*y + y*y*y*y); // 1/p^2 + p
        //return Math.sinh(y)*Math.cos(x); // sinp
        /*
        //arctan c
        double a = (1 - x*x - y*y)/(x*x + (1+y)*(1+y));
        double b = (2*x)/(x*x + (1+y)*(1+y));
        Myi ln = ln(a, b);
        ln = ln.multiply(0.5);
        return -ln.getRe();
         */
 /*
        // ln ((i-c)/(i+c))
        double a = (1 - x*x - y*y)/(x*x + (1+y)*(1+y));
        double b = (2*x)/(x*x + (1+y)*(1+y));
        return ln(a, b).getIm();
         */
 /*
        // ln c
        Myi num = new Myi(x, y, false);
        return num.ln().minverse().getIm();
         */
        //Myi num = new Myi(x, y, false);
        //return num.acos().getIm();
        /*
        //tear if complx is more than 4, use inverse square, otherwise use lni-c/i+c
        if (y > 0.5) {
            y = y - 3;
            x=0.5*x;
            y=0.5*y;
            return (2 * x * y) / (x * x * x * x + 2 * x * x * y * y + y * y * y * y);
        } else {
            y = -y-4;
            x=2*x;
            y=2*y;
            double a = (1 - x * x - y * y) / (x * x + (1 + y) * (1 + y));
            double b = (2 * x) / (x * x + (1 + y) * (1 + y));
            return ln(a, b).getIm();
        }
         */
        //Mobius transform
        double a = 1, b = 0, d = 1;
        double c = (a * d - 1) / b;
        return (a * d * y - b * c * y) / ((c * x + d) * (c * x + d) + c * c * y * y);
    }

    public Myi Transform(Myi z) {
        if (operatorString.size() > 0) {
            for (int i = 0; i < operatorString.size(); i++) {
                z = z.operator(operatorString.get(i));
            }
            return z;
        }
        return z;
        //return z.ln();

        //return z.atan().power(2).minverse();
        //return z.atan().minverse();
        //return z.tan();
        //return z.acos().minverse();
        // ln ((i-c)/(i+c))
        //Myi iMinusZ = Myi.I.add(z.ainverse());
        //Myi iPlusZ = Myi.I.add(z);
        //return (iMinusZ.divide(iPlusZ)).ln();
        //inverse square
        //return z.minverse().power(2);
        //return z.tan();
        //return z.minverse();
        //return z.asin();
        //return z.acos();
        //return z.multiply(0.1).add(z.minverse().multiply(0.9));
        //return z.power(2).multiply(0.25).add(z.multiply(0.5));
    }

    //This always has to be inverse of Transform
    //Used in mapBuddha()
    public Myi InverseTransform(Myi z) {
        if (operatorString.size() > 0) {
            for (int i = 0; i < operatorString.size(); i++) {
                z = z.operator(-operatorString.get((operatorString.size() - i - 1)));
            }
            return z;
        }

        //return z.minverse().power(1,2).tan();
        //return z.minverse().tan();
        return z;
        //return z.minverse().cos();

        // ln ((i-c)/(i+c))
        //Myi expz = z.exp();
        //return (Myi.I.multiply(Myi.ONE.add(expz.ainverse()))).divide(expz.add(Myi.ONE));
        // inverse square
        //return z.minverse().power(1,2);
        //return z.minverse();
        //return z.sin();
        //return z.acos();
    }

    public BufferedImage getMap() {
        return map;
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

    public int getRedFromN(double n) {
        return (int) (Math.sin(n / Math.PI / 2 / 4) * 100 + 149);
    }

    public int getGreenFromN(double n) {
        return (int) (Math.sin(n / Math.PI / 4) * 100 + 149);
    }

    public int getBlueFromN(double n) {
        return (int) (Math.cos(n / Math.PI * 2 / 4) * 100 + 149);
    }

    public int getRedFromN(int n, double x, double y) {
        return (int) (Math.sin(n / Math.PI / 2 / 4 + 2 * x * x + 2.5 * y * y) * 100 + 150);
    }

    public int getGreenFromN(int n, double x, double y) {
        return (int) (Math.sin(n / Math.PI / 4 + (3 * x) * (3 * x) / y + (2 * y)) * 100 + 150);
    }

    public int getBlueFromN(int n, double x, double y) {
        return (int) (Math.cos(n / Math.PI * 2 / 4 + 2 * x * 3 * y) * 100 + 150);
    }

    public Myi ln(double a, double b) {
        return new Myi(Math.log(a * a + b * b) / 2, Math.asin(b / Math.sqrt(a * a + b * b)), false);
    }
}
