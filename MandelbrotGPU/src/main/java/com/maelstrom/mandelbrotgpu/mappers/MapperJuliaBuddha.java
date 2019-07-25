package com.maelstrom.mandelbrotgpu.mappers;

import com.maelstrom.mandelbrotgpu.ColorScheme;
import com.maelstrom.mandelbrotgpu.FractalSettings;
import static com.maelstrom.mandelbrotgpu.mappers.MapperSuperclass.fractalData;
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
public class MapperJuliaBuddha extends MapperSuperclass implements MapperInterface {

    public MapperJuliaBuddha() {
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
     * Creates the kernel and program for Buddha
     *
     * @param fn The function to be iterated. Written in the language of C99.
     * This can be a function of zn, c and n and can use the methods in
     * ComplexFunctions.cl file
     * @param transformOperators The ArrayList of operator ID's to transform
     * @param maxIterations The maximum number of iterations
     * @param calculateComplex Whether the image should be calculated in blocks
     */
    @Override
    public void loadProgram(final String fn, final ArrayList<Integer> transformOperators, final int maxIterations, final boolean calculateComplex) {
        if (calculateComplex) {
            LoadProgramComplex(fn, transformOperators, maxIterations);
        } else {
            LoadProgramSimple(fn, transformOperators, maxIterations);
        }
    }

    /**
     * Calculate the fractal for given settings and color scheme
     *
     * @param settings The fractal settings we want to render
     * @return The BufferedImage of our fractal
     */
    @Override
    public BufferedImage createImage(final FractalSettings settings) {
        final long time = System.nanoTime();
        BufferedImage image = new BufferedImage(settings.sizeX, settings.sizeY, BufferedImage.TYPE_INT_RGB);
        ColorScheme scheme = new ColorScheme();

        // Create the image depending on how it should be calculated
        if (settings.calculateComplex) {
            fractalData = createDataJuliaBuddhaComplex(settings);
            image.getRaster().setPixels(0, 0, settings.sizeX, settings.sizeY,
                    scheme.iterationsToRGBBuddha(settings.colorSchemeID, fractalData, settings.maxIterations));
        } else {
            fractalData = createDataJuliaBuddhaSimple(settings);
            image.getRaster().setPixels(0, 0, settings.sizeX, settings.sizeY,
                    scheme.iterationsToRGBBuddha(settings.colorSchemeID, fractalData, settings.maxIterations));
        }
        return image;
    }

    /**
     * Creates the kernel and program for Buddha
     *
     * @param fn The function to be iterated. Written in the language of C99.
     * This can be a function of zn, c and n and can use the methods in
     * ComplexFunctions.cl file
     * @param transforms The ArrayList of operator ID's
     * @param iterations The maximum number of iterations to be done
     */
    public void LoadProgramSimple(final String fn, final ArrayList<Integer> transforms, int iterations) {
        // Load the BuddhaKernel source code
        String buddhaFileName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu\\kernels" + "\\JuliaBuddhaKernel.cl";
        String buddhaSRC = readFile(buddhaFileName);
        // OpenCL does not allow for dynmaic length arrays so we simply change the length in the source code:
        buddhaSRC = buddhaSRC.replace("INSERT ITERATIONS HERE", "" + iterations);

        // Load the ComplexFunctions source code
        String complexSRC = getComplexSRC(fn, transforms);

        // Create the kernel
        program = clCreateProgramWithSource(context, 2, new String[]{complexSRC, buddhaSRC}, null, null);
        clBuildProgram(program, 0, null, null, null, null);
        kernel = clCreateKernel(program, "fractalKernel", null);
    }

    /**
     * Creates the kernel and program for Buddha
     *
     * @param fn The function to be iterated. Written in the language of C99.
     * This can be a function of zn, c and n and can use the methods in
     * ComplexFunctions.cl file
     * @param transforms The ArrayList of operator ID's
     * @param iterations The maximum number of iterations to be done
     */
    public void LoadProgramComplex(final String fn, final ArrayList<Integer> transforms, int iterations) {
        // Load the BuddhaKernelComplex source code
        String buddhaFileName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu\\kernels" + "\\JuliaBuddhaKernelComplex.cl";
        String buddhaSRC = readFile(buddhaFileName);
        // OpenCL does not allow for dynmaic length arrays so we simply change the length in the source code:
        buddhaSRC = buddhaSRC.replace("INSERT ITERATIONS HERE", "" + iterations);

        // Load the ComplexFunctions source code
        String complexSRC = getComplexSRC(fn, transforms);

        // Create the kernel
        program = clCreateProgramWithSource(context, 2, new String[]{complexSRC, buddhaSRC}, null, null);
        clBuildProgram(program, 0, null, null, null, null);
        kernel = clCreateKernel(program, "fractalKernel", null);
    }

    /**
     * Calculate the Buddha in blockSize x blockSize blocks
     *
     * @param settings The fractal settings we want to render
     * @return double[] of our Buddha
     */
    protected double[] createDataJuliaBuddhaComplex(final FractalSettings settings) {
        // Define the side length of each block to render
        int blockSize = 1000;

        // Create a new blank array to store the results
        double[] results = new double[settings.sizeX * settings.sizeY * 3];
        double[] tempResults;
        for (int i = 0; i < results.length; i++) {
            results[i] = 0;
        }
        // Initialize the settings for each block
        FractalSettings blockSettings = settings.clone();

        // Loop through each block
        for (int x = 0; x < settings.sizeX; x += blockSize) {
            for (int y = 0; y < settings.sizeY; y += blockSize) {

                // Reset tempResults
                tempResults = new double[settings.sizeX * settings.sizeY * 3];
                for (int i = 0; i < tempResults.length; i++) {
                    tempResults[i] = 0;
                }

                // Adjust the settings for the current block
                System.out.println("Creating block " + ((x / blockSize) * (settings.sizeY / blockSize) + y / blockSize + 1) + " of " + (int) (Math.ceil(1.0 * settings.sizeX / blockSize) * Math.ceil(1.0 * settings.sizeY / blockSize)));
                blockSettings.sizeX = Math.min(blockSize, settings.sizeX - x);
                blockSettings.sizeY = Math.min(blockSize, settings.sizeY - y);
                blockSettings.leftest = settings.leftest + (settings.rightest - settings.leftest) * x / settings.sizeX;
                blockSettings.rightest = settings.leftest + (settings.rightest - settings.leftest) * (x + blockSettings.sizeX) / settings.sizeX;
                blockSettings.highest = settings.highest + (settings.lowest - settings.highest) * y / settings.sizeY;
                blockSettings.lowest = settings.highest + (settings.lowest - settings.highest) * (y + blockSettings.sizeY) / settings.sizeY;

                // Allocate the memory objects for the input and output data
                cl_mem mem[] = new cl_mem[]{
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{blockSettings.leftest}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{blockSettings.rightest}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{blockSettings.highest}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{blockSettings.lowest}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, Pointer.to(new int[]{blockSettings.sizeX}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, Pointer.to(new int[]{blockSettings.sizeY}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, Pointer.to(new int[]{blockSettings.maxIterations}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.leftest}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.rightest}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.highest}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.lowest}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, Pointer.to(new int[]{settings.sizeX}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, Pointer.to(new int[]{settings.sizeY}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.f0Re}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.f0Im}), null),
                    clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double, Pointer.to(new double[]{settings.threshold}), null),
                    clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * settings.sizeX * settings.sizeY * 3, null, null)
                };

                // Set the arguments for the kernel
                for (int i = 0; i < mem.length; i++) {
                    clSetKernelArg(kernel, i, Sizeof.cl_mem, Pointer.to(mem[i]));
                }

                // Execute the kernel
                clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{blockSettings.sizeX * blockSettings.sizeY}, null, 0, null, null);

                // Read the output data
                clEnqueueReadBuffer(queue, mem[mem.length - 1], CL_TRUE, 0, Sizeof.cl_double * settings.sizeX * settings.sizeY * 3, Pointer.to(tempResults), 0, null, null);

                // Release kernel, program, and memory objects
                for (cl_mem m : mem) {
                    clReleaseMemObject(m);
                }

                //add tempResults to results
                for (int i = 0; i < settings.sizeX * settings.sizeY * 3; i++) {
                    results[i] += tempResults[i];
                }
            }
        }

        return results;
    }

    /**
     * Calculate the Buddha for given settings
     *
     * @param settings The fractal settings we want to render
     * @return double[] of our Buddha
     */
    protected double[] createDataJuliaBuddhaSimple(final FractalSettings settings) {

        // Create a blank array to store the output
        double[] results = new double[settings.sizeX * settings.sizeY * 3];
        for (int i = 0; i < results.length; i++) {
            results[i] = 0;
        }

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
            clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * settings.sizeX * settings.sizeY * 3, null, null)
        };

        // Set the arguments for the kernel
        for (int i = 0; i < mem.length; i++) {
            clSetKernelArg(kernel, i, Sizeof.cl_mem, Pointer.to(mem[i]));
        }

        // Execute the kernel
        clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{settings.sizeX * settings.sizeY}, null, 0, null, null);

        // Read the output data
        clEnqueueReadBuffer(queue, mem[mem.length - 1], CL_TRUE, 0, Sizeof.cl_double * settings.sizeX * settings.sizeY * 3, Pointer.to(results), 0, null, null);

        // Release kernel, program, and memory objects
        for (cl_mem m : mem) {
            clReleaseMemObject(m);
        }

        return results;
    }

    /**
     * Calculate the Buddha for given settings Calculated by finding the top
     * half of the image and mirroring the bottom
     *
     * @param settings The fractal settings we want to render
     * @return double[] of our fractal
     */
    protected double[] createDataJuliaBuddhaMirror(final FractalSettings settings) {

        // Create a blank array to store the output
        double[] results = new double[settings.sizeX * settings.sizeY * 3];
        for (int i = 0; i < results.length; i++) {
            results[i] = 0;
        }

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
            clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * settings.sizeX * settings.sizeY * 3, null, null)
        };

        // Set the arguments for the kernel
        for (int i = 0; i < mem.length; i++) {
            clSetKernelArg(kernel, i, Sizeof.cl_mem, Pointer.to(mem[i]));
        }

        // Execute the kernel
        clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{settings.sizeX * settings.sizeY / 2}, null, 0, null, null);

        // Read the output data
        clEnqueueReadBuffer(queue, mem[mem.length - 1], CL_TRUE, 0, Sizeof.cl_double * settings.sizeX * settings.sizeY * 3, Pointer.to(results), 0, null, null);

        // Release kernel, program, and memory objects
        for (cl_mem m : mem) {
            clReleaseMemObject(m);
        }

        // Mirror the entire result across the x axis
        for (int i = 0; i < settings.sizeX * settings.sizeY; i++) {
            int x = i % settings.sizeX;
            int y = i / settings.sizeX;
            results[i * 3] = results[((settings.sizeY - y - 1) * settings.sizeX + x) * 3] = results[i * 3] + results[((settings.sizeY - y - 1) * settings.sizeX + x) * 3];
            results[i * 3 + 1] = results[((settings.sizeY - y - 1) * settings.sizeX + x) * 3 + 1] = results[i * 3 + 1] + results[((settings.sizeY - y - 1) * settings.sizeX + x) * 3 + 1];
            results[i * 3 + 2] = results[((settings.sizeY - y - 1) * settings.sizeX + x) * 3 + 2] = results[i * 3 + 2] + results[((settings.sizeY - y - 1) * settings.sizeX + x) * 3 + 2];
        }

        return results;
    }

}
