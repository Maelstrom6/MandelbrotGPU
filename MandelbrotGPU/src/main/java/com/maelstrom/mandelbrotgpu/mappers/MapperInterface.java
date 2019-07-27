package com.maelstrom.mandelbrotgpu.mappers;

import com.maelstrom.mandelbrotgpu.FractalSettings;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public interface MapperInterface {
    BufferedImage createProgramAndImage(final FractalSettings settings);
    
    void loadProgram(final String fn, final ArrayList<Integer> transformOperators, final int maxIterations, final boolean calculateComplex, boolean antiBuddha);
    
    BufferedImage createImage(final FractalSettings settings);
}
