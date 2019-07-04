/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot3;


import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;//for quality: increase num on lines 41,46,47,95

/**
 *
 * @author ChrisPC
 */
public class MandQuar {
    private double R=-0.10125, K=-0.0025;

    private int numer=2, denom=1;
    private int sizeX, sizeY;
    private double limX, limY;
    private double centreX, centreY;
    private boolean mirror;
    private BufferedImage map;
    private int maxI = 500;//500
    private int threads = 4;
    private int iter;

    public MandQuar(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        limX = 2;
        limY = 2;
        centreX = 0;
        centreY = 0;
        mirror = true;
    }

    public MandQuar(int sizeX, int sizeY, double xLim, double yLim) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = xLim;
        this.limY = yLim;
        centreX = 0;
        centreY = 0;
        mirror = true;
    }

    public MandQuar(int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
        this.mirror = (centreY == 0);
    }
    
    public MandQuar(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY){
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

    public MandQuar(int numer, int denom, int sizeX, int sizeY, double limX, double limY, double centreX, double centreY, int maxI, int threads) {
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
    
    

    public BufferedImage mapAutoMirror() {
        
        if (mirror) {
            return mapMirror();
        } else {
            return mapNoMirror();
        }
    }

    public BufferedImage mapNoMirror() {
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter=0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id=iter;
                    iter++;
                    int num;
                    int scaler, scaleg, scaleb;
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY; i++) {
                            double xCoordinate = (-limX*(1.0*sizeX/sizeY) + (2.0 * limY * x / sizeY) + centreX);
                            double yCoordinate = (limY - (2.0 * limY * i / sizeY)+centreY);
                            num = (int) (PartOfMand(R, xCoordinate, yCoordinate, K));
                            //num = (int) (PartSet((-limX + (2.0 * limX * x / sizeX) + centreX), (limY - (2.0 * limY * i / sizeY)+centreY)));//-(2-xLim) for x to move it left
                            if (num == maxI) {//need to change this if yuou change i<100
                                map.setRGB(x, i, Color.BLACK.getRGB());
                            } else {
                                hi();
                                scaler = getRedFromN(num, xCoordinate, yCoordinate);//divide everything by 4 for smoother transition
                                scaleg = getGreenFromN(num, xCoordinate, yCoordinate);
                                scaleb = getBlueFromN(num, xCoordinate, yCoordinate);
                                map.setRGB(x, i, new Color(scaler, scaleg, scaleb).hashCode());
                            }
                        }
                    }
                }
            });
            column[i].setPriority(Thread.MIN_PRIORITY);
            column[i].start();
        }
        for(int i=0;i<threads;i++){
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }
    
    public void hi(){
        getRedFromN(2);
    }

    public synchronized BufferedImage mapMirror() {
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter=0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id=iter;
                    iter++;
                    double num;
                    int scaler, scaleg, scaleb;
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY / 2; i++) {
                            num = (int) (PartOfMand(R, (-limX*(1.0*sizeX/sizeY) + (2.0 * limY * x / sizeY) + centreX), (limY - (2.0 * limY * i / sizeY)), K));
                            //num = (int) (PartSet((-limX + (2.0 * limX * x / sizeX) + centreX), (limY - (2.0 * limY * i / sizeY))));//-(2-xLim) for x to move it left
                            if (num == maxI) {//need to change this if yuou change i<100
                                map.setRGB(x, i, Color.BLACK.getRGB());
                            } else {
                                scaler = (int) (Math.sin(num / Math.PI / 2 / 4) * 100 + 150);//divide everything by 4 for smoother transition
                                scaleg = (int) (Math.sin(num / Math.PI / 4) * 100 + 150);
                                scaleb = (int) (Math.cos(num / Math.PI * 2 / 4) * 100 + 150);
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
        for(int i=0;i<threads;i++){
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }
    
    public BufferedImage mapJulia(){
        map = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        iter=0;
        Thread[] column = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            column[i] = new Thread(new Runnable() {
                public void run() {
                    int id=iter;
                    iter++;
                    double num;
                    int scaler, scaleg, scaleb;
                    for (int x = id; x < sizeX; x += threads) {
                        for (int i = 0; i < sizeY / 2; i++) {
                            num = (int) (PartOfJulia((-limX*(1.0*sizeX/sizeY) + (2.0 * limY * x / sizeY)), (limY - (2.0 * limY * i / sizeY)), centreX, centreY));
                            //num = (int) (PartSet((-limX + (2.0 * limX * x / sizeX) + centreX), (limY - (2.0 * limY * i / sizeY))));//-(2-xLim) for x to move it left
                            if (num == maxI) {//need to change this if yuou change i<100
                                map.setRGB(x, i, Color.BLACK.getRGB());
                            } else {
                                scaler = (int) (Math.sin(num / Math.PI / 2 / 4) * 100 + 150);//divide everything by 4 for smoother transition
                                scaleg = (int) (Math.sin(num / Math.PI / 4) * 100 + 150);
                                scaleb = (int) (Math.cos(num / Math.PI * 2 / 4) * 100 + 150);
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
        for(int i=0;i<threads;i++){
            try {
                column[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
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

    public int PartOfMand(double r, double i, double j, double k) {
        MyQ point = new MyQ(r, i, j, k);
        MyQ tot = new MyQ(r, i, j, k);
        int it = 0;
        while (it < maxI && tot.norm() < 2) {
            tot=tot.square();
            tot=tot.add(point);
            it++;
        }
        return it;
    }
    
    public int PartOfJulia(double a, double y, double cx, double cy){
        Myi tot=new Myi(a,y,false);
        int i=0;
        while(i<maxI && tot.getR()<10){
            tot=tot.power(numer, denom);
            tot=tot.add(cx,cy);
            i++;
        }
        return i;
    }
    
    public BufferedImage getMap(){
        return map;
    }
    
    public int getRedFromN(int n){
        return (int) (Math.sin(n / Math.PI / 2 / 4) * 100 + 150);
    }
    
    public int getGreenFromN(int n){
        return (int) (Math.sin(n / Math.PI / 4) * 100 + 150);
    }
    
    public int getBlueFromN(int n){
        return (int) (Math.cos(n / Math.PI * 2 / 4) * 100 + 150);
    }
    
    public int getRedFromN(int n, double x, double y){
        return (int) (Math.sin(n / Math.PI / 2 / 4 + 2*x*x + 2.5*y*y) * 100 + 150);
    }
    
    public int getGreenFromN(int n, double x, double y){
        return (int) (Math.sin(n / Math.PI / 4 + (3*x)*(3*x)/y + (2*y)) * 100 + 150);
    }
    
    public int getBlueFromN(int n, double x, double y){
        return (int) (Math.cos(n / Math.PI * 2 / 4 + 2*x * 3*y) * 100 + 150);
    }
}
