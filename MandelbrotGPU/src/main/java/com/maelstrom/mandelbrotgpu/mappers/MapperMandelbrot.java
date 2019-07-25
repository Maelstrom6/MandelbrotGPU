package com.maelstrom.mandelbrotgpu.mappers;

import com.maelstrom.mandelbrotgpu.ColorScheme;
import com.maelstrom.mandelbrotgpu.FractalSettings;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.jocl.CL;
import static org.jocl.CL.*;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;

/**
 *
 * @author Chris
 */
public class MapperMandelbrot extends MapperSuperclass implements MapperInterface {

    public MapperMandelbrot() {
        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain a platform ID
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[0];

        // Initialise the context properties
        cl_context_properties properties = new cl_context_properties();
        properties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain a device ID
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
        cl_device_id devices[] = new cl_device_id[numDevicesArray[0]];
        clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, numDevicesArray[0], devices, null);
        cl_device_id device = devices[0];

        // Create a context and command queue for the selected device
        context = clCreateContext(properties, 1, new cl_device_id[]{device}, null, null, null);
        queue = clCreateCommandQueue(context, device, 0, null);
    }

    /**
     * Creates the kernel and creates the image
     *
     * @param settings The settings of the fractal
     * @return The BufferedImage of our fractal
     */
    @Override
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
     * @param transformOperators The ArrayList of operator ID's to transform
     * @param maxIterations Not needed for the Mandelbrot implementation
     * @param calculateComplex Not needed for the Mandelbrot implementation
     */
    @Override
    public void loadProgram(final String fn, final ArrayList<Integer> transformOperators, final int maxIterations, final boolean calculateComplex) {
        
        String mandelbrotSRC = getMandelbrotSRC();
        String complexSRC = getComplexSRC(fn, transformOperators);

        // Create the kernel
        program = clCreateProgramWithSource(context, 2, new String[]{complexSRC, mandelbrotSRC}, null, null);
        clBuildProgram(program, 0, null, null, null, null);
        kernel = clCreateKernel(program, "fractalKernel", null);
    }
    
    /**
     * Gets the openCL source code for the MandelbrotKernel
     * 
     * @return the openCL source code for the MandelbrotKernel.
     */
    private String getMandelbrotSRC() {
        String mandFileName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu\\kernels" + "\\MandelbrotKernel.cl";
        String mandelbrotSRC = readFile(mandFileName);
        return mandelbrotSRC;
    }

    /**
     * Calculate the fractal for given settings and color scheme
     *
     * @param settings The fractal settings we want to render
     * @return The BufferedImage of our fractal
     */
    @Override
    public BufferedImage createImage(final FractalSettings settings) {
        BufferedImage image = new BufferedImage(settings.sizeX, settings.sizeY, BufferedImage.TYPE_INT_RGB);
        ColorScheme scheme = new ColorScheme();

        // Create the image depending on how it should be calculated
        if (settings.calculateComplex) {
            return createDataMandelbrotComplex(settings, settings.colorSchemeID);
        } else {
            fractalData = createDataMandelbrot(settings);
            image.getRaster().setPixels(0, 0, settings.sizeX, settings.sizeY,
                    scheme.iterationsToRGBMandelbrot(settings.colorSchemeID, fractalData, settings.maxIterations));
        }

        return image;
    }

    /**
     * Calculate the fractal for given settings
     *
     * @param settings The fractal settings we want to render
     * @return The double[] of our fractal
     */
    protected double[] createDataMandelbrot(final FractalSettings settings) {

        // Create a new blank array to store the results
        double[] results = new double[settings.sizeX * settings.sizeY];

        // Allocate the memory objects for the input and output data
        cl_mem mem[] = new cl_mem[]{
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.leftest}), null),
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.rightest}), null),
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.highest}), null),
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.lowest}), null),
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, Pointer.to(new int[]{settings.sizeX}), null),
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, Pointer.to(new int[]{settings.sizeY}), null),
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, Pointer.to(new int[]{settings.maxIterations}), null),
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.f0Re}), null),
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.f0Im}), null),
            clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.threshold}), null),
            clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * settings.sizeX * settings.sizeY, null, null)
        };

        // Set the arguments for the kernel
        for (int i = 0; i < mem.length; i++) {
            clSetKernelArg(kernel, i, Sizeof.cl_mem, Pointer.to(mem[i]));
        }

        // Execute the kernel
        clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{settings.sizeX * settings.sizeY}, null, 0, null, null);

        // Read the output data
        clEnqueueReadBuffer(queue, mem[mem.length - 1], CL_TRUE, 0, Sizeof.cl_double * settings.sizeX * settings.sizeY, Pointer.to(results), 0, null, null);

        // Release kernel, program, and memory objects
        for (cl_mem m : mem) {
            clReleaseMemObject(m);
        }

        return results;
    }

    /**
     * Calculate the fractal for given settings and color scheme only for
     * Mandelbrot. Should be used for large Mandelbrot images like 3000x3000 or
     * bigger. This is because the graphics card could timeout and produce an
     * out of resources error.
     *
     * @param settings The fractal settings we want to render
     * @param colorSchemeID The ID of the coloring scheme to be parsed to
     * ColorScheme
     * @return The BufferedImage of our fractal
     */
    public BufferedImage createDataMandelbrotComplex(final FractalSettings settings, final int colorSchemeID) {
        int blockSize = 1000;
        BufferedImage image = new BufferedImage(settings.sizeX, settings.sizeY, BufferedImage.TYPE_INT_RGB);
        ColorScheme scheme = new ColorScheme();
        FractalSettings blockSettings = settings.clone();
        for (int x = 0; x < settings.sizeX; x += blockSize) {
            for (int y = 0; y < settings.sizeY; y += blockSize) {
                System.out.println("Creating block " + ((x / blockSize) * (settings.sizeY / blockSize) + y / blockSize + 1) + " of " + (int) (Math.ceil(1.0 * settings.sizeX / blockSize) * Math.ceil(1.0 * settings.sizeY / blockSize)));
                blockSettings.sizeX = Math.min(blockSize, settings.sizeX - x);
                blockSettings.sizeY = Math.min(blockSize, settings.sizeY - y);
                blockSettings.leftest = settings.leftest + (settings.rightest - settings.leftest) * x / settings.sizeX;
                blockSettings.rightest = settings.leftest + (settings.rightest - settings.leftest) * (x + blockSettings.sizeX) / settings.sizeX;
                blockSettings.highest = settings.highest + (settings.lowest - settings.highest) * y / settings.sizeY;
                blockSettings.lowest = settings.highest + (settings.lowest - settings.highest) * (y + blockSettings.sizeY) / settings.sizeY;
                fractalData = createDataMandelbrot(blockSettings);
                image.getRaster().setPixels(x, y, blockSettings.sizeX, blockSettings.sizeY,
                        scheme.iterationsToRGBMandelbrot(colorSchemeID, fractalData, settings.maxIterations));
            }
        }

        return image;
    }
}
