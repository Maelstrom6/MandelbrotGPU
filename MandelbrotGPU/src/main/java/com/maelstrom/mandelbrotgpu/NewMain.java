/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maelstrom.mandelbrotgpu;

/**
 *
 * @author Chris
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String complexSRC = "#define _USE_MATH_DEFINES\n"
            + "\n"
            + "double __attribute__ ((overloadable)) abs(double value) {\n"
            + "    return value >= 0 ? value : 0 - value;\n"
            + "}\n"
            + "\n"
            + "struct Complex {\n"
            + "    double r;\n"
            + "    double theta;\n"
            + "};\n"
            + "\n"
            + "double getRe(struct Complex c) {\n"
            + "    return c.r * cos(c.theta);\n"
            + "}\n"
            + "\n"
            + "double getIm(struct Complex c) {\n"
            + "    return c.r * sin(c.theta);\n"
            + "}\n"
            + "\n"
            + "double calcTheta(double a, double b) {\n"
            + "    double the = 0;\n"
            + "    if (a == 0 && b == 0) {\n"
            + "        the = 0;\n"
            + "    } else if (a == 0) {\n"
            + "        the = M_PI / 2 * (abs(b) / b);\n"
            + "    } else if (b == 0) {\n"
            + "        if (a < 0) {\n"
            + "            the = M_PI;\n"
            + "        } else {\n"
            + "            the = 0;\n"
            + "        }\n"
            + "    } else if (a > 0) {\n"
            + "        the = atan(b / a);\n"
            + "    } else if (a < 0) {\n"
            + "        if (b > 0) {\n"
            + "            the = atan(b / a) + M_PI;\n"
            + "        } else {\n"
            + "            the = atan(b / a) - M_PI;\n"
            + "        }\n"
            + "    }\n"
            + "    return the;\n"
            + "}\n"
            + "\n"
            + "struct Complex newComplex(double r, double theta, bool polar) {\n"
            + "    struct Complex c;\n"
            + "    if (polar) {\n"
            + "        c.r = r;\n"
            + "        c.theta = theta;\n"
            + "        return c;\n"
            + "    } else {\n"
            + "        c.r = sqrt(r * r + theta * theta);\n"
            + "        c.theta = calcTheta(r, theta);\n"
            + "        return c;\n"
            + "    }\n"
            + "}\n"
            + "\n"
            + "double getPrinciple(double t) {\n"
            + "    while (t >= M_PI) {\n"
            + "        t = t - 2 * M_PI;\n"
            + "    }\n"
            + "    while (t <= -M_PI) {\n"
            + "        t = t + 2 * M_PI;\n"
            + "    }\n"
            + "    return t;\n"
            + "}\n"
            + "\n"
            + "struct Complex addComplex(struct Complex c, struct Complex num) {\n"
            + "    double x = c.r * cos(c.theta) + getRe(num);\n"
            + "    double y = c.r * sin(c.theta) + getIm(num);\n"
            + "    return newComplex(sqrt(x * x + y * y), calcTheta(x, y), true);\n"
            + "}\n"
            + "\n"
            + "struct Complex subComplex(struct Complex c, struct Complex num) {\n"
            + "    double x = c.r * cos(c.theta) - getRe(num);\n"
            + "    double y = c.r * sin(c.theta) - getIm(num);\n"
            + "    return newComplex(sqrt(x * x + y * y), calcTheta(x, y), true);\n"
            + "}\n"
            + "\n"
            + "struct Complex powComplex(struct Complex c, int numer) {\n"
            + "    double the = getPrinciple(c.theta * numer);\n"
            + "    return newComplex(pow(c.r, 1.0 * numer), the, true);\n"
            + "}\n"
            + "\n"
            + "double mod2Complex(struct Complex c) {return getRe(c)*getRe(c) + getIm(c)*getIm(c);}";
        System.out.println(complexSRC);
        // TODO code application logic here
    }
    
}
