package com.maelstrom.mandelbrotgpu.mappers;

import com.maelstrom.mandelbrotgpu.FractalSettings;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Chris
 */
public class Mapper {

    // The interface for each type of fractal
    protected static MapperInterface instance;

    public Mapper(String fractalType) {
        if ("buddha".equals(fractalType.toLowerCase())) {
            instance = new MapperBuddha();
        } else if ("julia".equals(fractalType.toLowerCase())) {
            instance = new MapperJulia();
        } else if ("julia buddha".equals(fractalType.toLowerCase())) {
            instance = new MapperJuliaBuddha();
        } else {
            instance = new MapperMandelbrot();
        }
    }

    /**
     * Creates the program and image of the desired implementation declared at
     * initialization.
     *
     * @param settings The settings of the fractal
     * @return The BufferedImage of our fractal
     */
    public BufferedImage createProgramAndImage(final FractalSettings settings) {
        loadProgram(settings.fn, settings.transformOperators, settings.maxIterations, settings.calculateComplex);
        return createImage(settings);
    }

    /**
     * Creates the kernel and program for Mandelbrot
     *
     * @param fn The function to be iterated. Written in the language of C99.
     * This can be a function of zn, c and n and can use the methods in
     * ComplexFunctions.cl file
     * @param transforms The ArrayList of operator ID's
     * @param maxIterations The maximum number of iterations of the fn
     * @param calculateComplex Whether the program should calculate in blocks
     */
    public void loadProgram(final String fn, final ArrayList<Integer> transforms, final int maxIterations, final boolean calculateComplex) {
        instance.loadProgram(fn, transforms, maxIterations, calculateComplex);
    }

    /**
     * Calculate the fractal for given settings and color scheme Also print out
     * time taken
     *
     * @param settings The fractal settings we want to render
     * @return The BufferedImage of our fractal
     */
    public BufferedImage createImage(final FractalSettings settings) {
        final long time = System.nanoTime();

        BufferedImage result = instance.createImage(settings);

        System.out.println("Took " + (System.nanoTime() - time) / 1_000_000_000.0);

        return result;
    }

    public void savePNG(final BufferedImage bi, final String path) {
        final long time = System.nanoTime();
        try {
            ImageIO.write(bi, "PNG", new File(path));
        } catch (IOException e) {
            System.out.println("Error saving png.");
        }
        System.out.println("Took " + (System.nanoTime() - time) / 1_000_000_000.0 + " to save the image");
    }

    public BufferedImage readPNG(final String path) {
        BufferedImage result = null;
        try {
            result = ImageIO.read(new File(path));
        } catch (IOException ex) {
            Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public BufferedImage removeMiddlePixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width - 2, height - 2, BufferedImage.TYPE_INT_RGB);
        BufferedImage topLeft = image.getSubimage(0, 0, width / 2 - 1, height / 2 - 1);
        BufferedImage topRight = image.getSubimage(width / 2 + 1, 0, width / 2 - 1, height / 2 - 1);
        BufferedImage bottomLeft = image.getSubimage(0, height / 2 + 1, width / 2 - 1, height / 2 - 1);
        BufferedImage bottomRight = image.getSubimage(width / 2 + 1, height / 2 + 1, width / 2 - 1, height / 2 - 1);
        Graphics2D g2 = result.createGraphics();
        Color oldColor = g2.getColor();
        g2.setPaint(Color.BLACK);
        g2.fillRect(0, 0, width, height);
        g2.setColor(oldColor);
        g2.drawImage(topLeft, null, 0, 0);
        g2.drawImage(topRight, null, topRight.getWidth(), 0);
        g2.drawImage(bottomLeft, null, 0, bottomLeft.getHeight());
        g2.drawImage(bottomRight, null, topRight.getWidth(), bottomLeft.getHeight());
        g2.dispose();
        return result;
    }
    
    public BufferedImage removeMiddlePixelsHorizontal(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width - 2, height - 2, BufferedImage.TYPE_INT_RGB);
        BufferedImage top = image.getSubimage(0, 0, width, height / 2 - 1);
        BufferedImage bottom = image.getSubimage(0, height / 2 + 1, width, height / 2 - 1);
        Graphics2D g2 = result.createGraphics();
        Color oldColor = g2.getColor();
        g2.setPaint(Color.BLACK);
        g2.fillRect(0, 0, width, height);
        g2.setColor(oldColor);
        g2.drawImage(top, null, 0, 0);
        g2.drawImage(bottom, null, 0, top.getHeight());
        g2.dispose();
        return result;
    }

}
