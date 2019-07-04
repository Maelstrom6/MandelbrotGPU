/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot3;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Chris
 */
public class TransformImage {

    private int sizeX, sizeY;
    private double limX, limY;
    private double centreX, centreY;
    private BufferedImage newMap, oldMap;

    public TransformImage(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = 2;
        this.limY = 2;
        this.centreX = 0;
        this.centreY = 0;
    }

    public TransformImage(int sizeX, int sizeY, double limX, double limY, double centreX, double centreY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.limX = limX;
        this.limY = limY;
        this.centreX = centreX;
        this.centreY = centreY;
    }

    public BufferedImage getMap() {
        return newMap;
    }

    public BufferedImage doThings() {
        newMap = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < sizeX; x++) {
            for (int i = 0; i < sizeY; i++) {
                double startingxCoordinate = (-limX * (1.0 * sizeX / sizeY) + (2.0 * limX * x / sizeY) + centreX);
                double startingyCoordinate = (limY - (2.0 * limY * i / sizeY) + centreY);
                Myi coordinate = (Transform(new Myi(startingxCoordinate, startingyCoordinate, false)));
                //double xCoordinate = TransformX(startingxCoordinate, startingyCoordinate);
                //double yCoordinate = TransformY(startingxCoordinate, startingyCoordinate);
                double xCoordinate = coordinate.getRe();
                double yCoordinate = coordinate.getIm();
                int xPixel = (int) ((xCoordinate + limX * (1.0 * sizeX / sizeY) - centreX) * (sizeY / 2.0 / limX));
                int yPixel = (int) ((yCoordinate + limY + centreY) * (sizeY / 2.0 / limY));
                if (0 <= xPixel && xPixel < sizeX && 0 <= yPixel && yPixel < sizeX) {
                    newMap.setRGB(xPixel, yPixel, oldMap.getRGB(x, i));
                }
            }
        }
        return newMap;
    }

    public void read(String fileName) {
        File file = new File(fileName);
        try {
            oldMap = ImageIO.read(file);
            sizeX = oldMap.getWidth();
            sizeY = oldMap.getHeight();
        } catch (IOException ex) {
            Logger.getLogger(TransformImage.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public Myi Transform(Myi z) {

        return z.exp();
    }
}
