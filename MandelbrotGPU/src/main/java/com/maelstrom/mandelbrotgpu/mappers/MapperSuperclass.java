package com.maelstrom.mandelbrotgpu.mappers;

import com.maelstrom.mandelbrotgpu.FractalManager;
import com.maelstrom.mandelbrotgpu.FractalSettings;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.jocl.CL.clReleaseCommandQueue;
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clReleaseProgram;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_kernel;
import org.jocl.cl_program;

/**
 *
 * @author Chris
 */
public class MapperSuperclass {

    protected cl_context context;
    protected cl_command_queue queue;
    protected cl_program program;
    protected cl_kernel kernel;
    protected static double[] fractalData;

    /**
     * Gets the openCL source code for the Complex operations and struct.
     *
     * @param fn The function to be iterated. Written in the language of C99.
     * This can be a function of zn, c and n and can use the methods in
     * ComplexFunctions.cl file
     * @param transformOperators The ArrayList of operator ID's to transform
     * @return The source code of the Complex operations
     */
    protected String getComplexSRC(final String fn,
            final ArrayList<Integer> transformOperators) {
        String t = getStringTransform(transformOperators);
        String it = getStringInverseTransform(transformOperators);

        String complexFileName = System.getProperty("user.dir")
                + "\\src\\main\\java\\com\\maelstrom\\mandelbrotgpu\\kernels"
                + "\\ComplexFunctions.cl";
        String complexSRC = readFile(complexFileName);
        complexSRC += "struct Complex fn(struct Complex zn, struct Complex c, int n){"
                + "\n" + "return " + fn + ";\n}\n\n";
        complexSRC += "struct Complex transform(struct Complex z){\n"
                + "		return " + t + ";\n"
                + "	}\n\n";
        complexSRC += "struct Complex inverseTransform(struct Complex z){\n"
                + "		return " + it + ";\n"
                + "	}\n\n";
        return complexSRC;
    }

    /**
     * Creates the kernel code for the function Transform
     *
     * @param operatorString The ArrayList of operator ID's
     * @return The resultant kernel code
     */
    protected String getStringTransform(final ArrayList<Integer> operatorString) {
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
    protected String getStringInverseTransform(final ArrayList<Integer> operatorString) {
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

    protected String readFile(final String fileName) {
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
