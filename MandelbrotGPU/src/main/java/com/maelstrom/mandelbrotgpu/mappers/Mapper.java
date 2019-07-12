package com.maelstrom.mandelbrotgpu.mappers;

import com.maelstrom.mandelbrotgpu.FractalSettings;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        }else if("julia".equals(fractalType.toLowerCase())){
            instance = new MapperJulia();
        } else if("julia buddha".equals(fractalType.toLowerCase())){
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
     * @param colorSchemeID The ID of the coloring scheme to be parsed to
     * ColorScheme
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

}
