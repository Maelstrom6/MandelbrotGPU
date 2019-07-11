package com.maelstrom.mandelbrotgpu;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jocl.CL;
import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_WRITE_ONLY;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContext;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clReleaseCommandQueue;
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clReleaseProgram;
import static org.jocl.CL.clSetKernelArg;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

/**
 *
 * @author Chris
 */
public class FractalManager {

    protected cl_context context;
    protected cl_command_queue queue;
    protected cl_program program;
    protected cl_kernel kernel;
    private static double[] fractalData;

    public FractalManager() {
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
     * @param colorSchemeID The ID of the coloring scheme to be parsed to
     * ColorScheme
     * @return The BufferedImage of our fractal
     */
    public BufferedImage createProgramAndImage(final FractalSettings settings, final int colorSchemeID) {
        LoadProgram(settings.fractalType, settings.fn, settings.transformOperators, settings.maxIterations);
        return createImage(settings, colorSchemeID);
    }

    /**
     * Creates the kernel and program for any implemented fractal given its type
     *
     * @param fractalType The type of fractal to be created
     * @param fn The function to be iterated. Written in the language of C99.
     * This can be a function of zn, c and n and can use the methods in
     * ComplexFunctions.cl file
     * @param transforms The ArrayList of operator ID's
     * @param iterations The maximum number of iterations to be done
     */
    public void LoadProgram(String fractalType, String fn, ArrayList<Integer> transforms, int iterations) {
        if (null == fractalType) {
            LoadProgramMandelbrot(fn, transforms);
        } else switch (fractalType) {
            case "Buddha":
                LoadProgramBuddha(fn, transforms, iterations);
                break;
            case "Buddha complex":
                LoadProgramBuddhaComplex(fn, transforms, iterations);
                break;
            default:
                LoadProgramMandelbrot(fn, transforms);
                break;
        }
    }

    /**
     * Creates the kernel and program for buddha
     *
     * @param fn The function to be iterated. Written in the language of C99.
     * This can be a function of zn, c and n and can use the methods in
     * ComplexFunctions.cl file
     * @param transforms The ArrayList of operator ID's
     * @param iterations The maximum number of iterations to be done
     */
    public void LoadProgramBuddha(final String fn, final ArrayList<Integer> transforms, int iterations) {
        // Load the BuddhaKernel source code
        String buddhaFileName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu" + "\\BuddhaKernel.cl";
        String buddhaSRC = readFile(buddhaFileName);
        // OpenCL does not allow for dynmaic length arrays so we simply change the length in the source code:
        buddhaSRC = buddhaSRC.replace("INSERT ITERATIONS HERE", ""+iterations);
        
        // Load the ComplexFunctions source code
        String complexFileName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu" + "\\ComplexFunctions.cl";
        String complexSRC = readFile(complexFileName);
        complexSRC += "struct Complex fn(struct Complex zn, struct Complex c, int n){"
                + "\n" + "return " + fn + ";\n}\n\n";
        complexSRC += "struct Complex transform(struct Complex z){\n"
                + "		return " + getStringTransform(transforms) + ";\n"
                + "	}\n\n";
        complexSRC += "struct Complex inverseTransform(struct Complex z){\n"
                + "		return " + getStringInverseTransform(transforms) + ";\n"
                + "	}\n\n";

        // Create the kernel
        program = clCreateProgramWithSource(context, 2, new String[]{complexSRC, buddhaSRC}, null, null);
        clBuildProgram(program, 0, null, null, null, null);
        kernel = clCreateKernel(program, "fractalKernel", null);
    }

    /**
     * Creates the kernel and program for buddha
     *
     * @param fn The function to be iterated. Written in the language of C99.
     * This can be a function of zn, c and n and can use the methods in
     * ComplexFunctions.cl file
     * @param transforms The ArrayList of operator ID's
     * @param iterations The maximum number of iterations to be done
     */
    public void LoadProgramBuddhaComplex(final String fn, final ArrayList<Integer> transforms, int iterations) {
        // Load the BuddhaKernelComplex source code
        String buddhaFileName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu" + "\\BuddhaKernelComplex.cl";
        String buddhaSRC = readFile(buddhaFileName);
        // OpenCL does not allow for dynmaic length arrays so we simply change the length in the source code:
        buddhaSRC = buddhaSRC.replace("INSERT ITERATIONS HERE", ""+iterations);
        
        // Load the ComplexFunctions source code
        String complexFileName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu" + "\\ComplexFunctions.cl";
        String complexSRC = readFile(complexFileName);
        complexSRC += "struct Complex fn(struct Complex zn, struct Complex c, int n){"
                + "\n" + "return " + fn + ";\n}\n\n";
        complexSRC += "struct Complex transform(struct Complex z){\n"
                + "		return " + getStringTransform(transforms) + ";\n"
                + "	}\n\n";
        complexSRC += "struct Complex inverseTransform(struct Complex z){\n"
                + "		return " + getStringInverseTransform(transforms) + ";\n"
                + "	}\n\n";

        // Create the kernel
        program = clCreateProgramWithSource(context, 2, new String[]{complexSRC, buddhaSRC}, null, null);
        clBuildProgram(program, 0, null, null, null, null);
        kernel = clCreateKernel(program, "fractalKernel", null);
    }

    /**
     * Creates the kernel and program for mandelbrot
     *
     * @param fn The function to be iterated. Written in the language of C99.
     * This can be a function of zn, c and n and can use the methods in
     * ComplexFunctions.cl file
     * @param transforms The ArrayList of operator ID's
     */
    public void LoadProgramMandelbrot(final String fn, final ArrayList<Integer> transforms) {
        String t = getStringTransform(transforms);
        String it = getStringInverseTransform(transforms);
        String mandFileName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu" + "\\MandelbrotKernel.cl";
        String mandelbrotSRC = readFile(mandFileName);
        String complexFileName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu" + "\\ComplexFunctions.cl";
        String complexSRC = readFile(complexFileName);
        complexSRC += "struct Complex fn(struct Complex zn, struct Complex c, int n){"
                + "\n" + "return " + fn + ";\n}\n\n";
        complexSRC += "struct Complex transform(struct Complex z){\n"
                + "		return " + t + ";\n"
                + "	}\n\n";
        complexSRC += "struct Complex inverseTransform(struct Complex z){\n"
                + "		return " + it + ";\n"
                + "	}\n\n";

        // Create the kernel
        program = clCreateProgramWithSource(context, 2, new String[]{complexSRC, mandelbrotSRC}, null, null);
        clBuildProgram(program, 0, null, null, null, null);
        kernel = clCreateKernel(program, "fractalKernel", null);
    }

    /**
     * Creates the kernel code for the function Transform
     *
     * @param operatorString The ArrayList of operator ID's
     * @return The resultant kernel code
     */
    private String getStringTransform(final ArrayList<Integer> operatorString) {
        String input = "z";
        if (operatorString.size() > 0) {
            for (int i = 0; i < operatorString.size(); i++) {
                input = getStringFunction(operatorString.get(i), input);
            }
        }
        //You can write your custom transformation below:
        //Rember to change the InverseTransform too if you are finding buddha
        return input;
    }

    /**
     * Creates the kernel code for the function InverseTransform
     *
     * @param operatorString The ArrayList of operator ID's
     * @return The resultant kernel code
     */
    private String getStringInverseTransform(final ArrayList<Integer> operatorString) {
        String input = "z";
        if (operatorString.size() > 0) {
            for (int i = 0; i < operatorString.size(); i++) {
                input = getStringFunction(-operatorString.get((operatorString.size() - i - 1)), input);
            }
        }
        //You can write your custom inverse transformation below:
        return input;
    }

    /**
     * Creates the kernel code for a function given its ID
     *
     * @param id The ID of the function
     * @param input The string that the function is being applied to
     * @return The resultant kernel code
     */
    private String getStringFunction(final int id, final String input) {
        switch (id) {
            case 0:
                return "minverse(" + input + ")";
            case 1:
                return "expComplex(" + input + ")";
            case -1:
                return "lnComplex(" + input + ")";
            case 2:
                return "sinComplex(" + input + ")";
            case -2:
                return "asinComplex(" + input + ")";
            case 3:
                return "cosComplex(" + input + ")";
            case -3:
                return "acosComplex(" + input + ")";
            case 4:
                return "tanComplex(" + input + ")";
            case -4:
                return "atanComplex(" + input + ")";
            default:
                if (id >= 5) {
                    return "powComplex(" + input + ", " + (id - 3) + ")";
                } else {
                    return "powFracComplex(" + input + ", 1, " + (Math.abs(id) - 3) + ")";
                }
        }
    }

    /**
     * Calculate the fractal for given settings and color scheme only for
     * mandelbrot. Should be used for large mandelbrot images like 3000x3000 or
     * bigger
     *
     * @param settings The fractal settings we want to render
     * @param colorSchemeID The ID of the coloring scheme to be parsed to
     * ColorScheme
     * @return The BufferedImage of our fractal
     */
    public BufferedImage createDataMandelbrotComplex(final FractalSettings settings, final int colorSchemeID) {
        final long time = System.nanoTime();
        BufferedImage image = new BufferedImage(settings.sizeX, settings.sizeY, BufferedImage.TYPE_INT_RGB);
        ColorScheme scheme = new ColorScheme();
        FractalSettings blockSettings = settings.clone();
        for (int x = 0; x < settings.sizeX; x += 1000) {
            for (int y = 0; y < settings.sizeY; y += 1000) {
                System.out.println("Creating block " + ((x / 1000) * (settings.sizeY / 1000) + y / 1000 + 1) + " of " + (int) (Math.ceil(settings.sizeX / 1000.0) * Math.ceil(settings.sizeY / 1000.0)));
                blockSettings.sizeX = Math.min(1000, settings.sizeX - x);
                blockSettings.sizeY = Math.min(1000, settings.sizeY - y);
                blockSettings.leftest = settings.leftest + (settings.rightest - settings.leftest) * x / settings.sizeX;
                blockSettings.rightest = settings.leftest + (settings.rightest - settings.leftest) * (x + blockSettings.sizeX) / settings.sizeX;
                blockSettings.highest = settings.highest + (settings.lowest - settings.highest) * y / settings.sizeY;
                blockSettings.lowest = settings.highest + (settings.lowest - settings.highest) * (y + blockSettings.sizeY) / settings.sizeY;
                fractalData = createDataMandelbrot(blockSettings);
                image.getRaster().setPixels(x, y, blockSettings.sizeX, blockSettings.sizeY,
                        scheme.iterationsToRGBMandelbrot(colorSchemeID, fractalData, settings.maxIterations));
            }
        }
        System.out.println("Took " + (System.nanoTime() - time) / 1_000_000_000.0);

        return image;
    }

    /**
     * Calculate the fractal for given settings and color scheme
     *
     * @param settings The fractal settings we want to render
     * @param colorSchemeID The ID of the coloring scheme to be parsed to
     * ColorScheme
     * @return The BufferedImage of our fractal
     */
    public BufferedImage createImage(final FractalSettings settings, final int colorSchemeID) {
        final long time = System.nanoTime();
        BufferedImage image = new BufferedImage(settings.sizeX, settings.sizeY, BufferedImage.TYPE_INT_RGB);
        ColorScheme scheme = new ColorScheme();
        
         // Create the image depending on whether it is buddha or mandelbrot and so on
        if (null == settings.fractalType) {
            fractalData = createDataMandelbrot(settings);
                image.getRaster().setPixels(0, 0, settings.sizeX, settings.sizeY,
                        scheme.iterationsToRGBMandelbrot(colorSchemeID, fractalData, settings.maxIterations));
        } else switch (settings.fractalType.toLowerCase()) {
            case "buddha":
                fractalData = createDataBuddhaMirror(settings);
                image.getRaster().setPixels(0, 0, settings.sizeX, settings.sizeY,
                        scheme.iterationsToRGBBuddha(colorSchemeID, fractalData, settings.maxIterations));
                break;
            case "buddha complex":
                fractalData = createDataBuddhaComplex(settings);
                image.getRaster().setPixels(0, 0, settings.sizeX, settings.sizeY,
                        scheme.iterationsToRGBBuddha(colorSchemeID, fractalData, settings.maxIterations));
                break;
            case "mandelbrot complex":
                return createDataMandelbrotComplex(settings, colorSchemeID);
            default:
                fractalData = createDataMandelbrot(settings);
                image.getRaster().setPixels(0, 0, settings.sizeX, settings.sizeY,
                        scheme.iterationsToRGBMandelbrot(colorSchemeID, fractalData, settings.maxIterations));
                break;
        }

        System.out.println("Took " + (System.nanoTime() - time) / 1_000_000_000.0);

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
     * Calculate the buddha in blockSize x blockSize blocks
     *
     * @param settings The fractal settings we want to render
     * @return double[] of our buddha
     */
    protected double[] createDataBuddhaComplex(final FractalSettings settings) {
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
     * Calculate the buddha for given settings
     *
     * @param settings The fractal settings we want to render
     * @return double[] of our buddha
     */
    protected double[] createDataBuddhaSimple(final FractalSettings settings) {

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
     * Calculate the buddha for given settings Calculated by finding the top
     * half of the image and mirroring the bottom
     *
     * @param settings The fractal settings we want to render
     * @return double[] of our fractal
     */
    protected double[] createDataBuddhaMirror(final FractalSettings settings) {

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
            results[i * 3] = results[((settings.sizeX - y - 1) * settings.sizeX + x) * 3] = results[i * 3] + results[((settings.sizeX - y - 1) * settings.sizeX + x) * 3];
            results[i * 3 + 1] = results[((settings.sizeX - y - 1) * settings.sizeX + x) * 3 + 1] = results[i * 3 + 1] + results[((settings.sizeX - y - 1) * settings.sizeX + x) * 3 + 1];
            results[i * 3 + 2] = results[((settings.sizeX - y - 1) * settings.sizeX + x) * 3 + 2] = results[i * 3 + 2] + results[((settings.sizeX - y - 1) * settings.sizeX + x) * 3 + 2];
        }

        return results;
    }

    @Override
    protected void finalize() {
        try {
            // OpenCL requires a cleanup after it runs
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(FractalManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        clReleaseCommandQueue(queue);
        clReleaseContext(context);
        clReleaseKernel(kernel);
        clReleaseProgram(program);

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

    private String readFile(final String fileName) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName)));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

}
