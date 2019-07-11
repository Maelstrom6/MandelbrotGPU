package com.maelstrom.myMandelbrot;

/**
 *
 * @author Chris
 */
public class Complex {

    private double r, theta;
    public static final Complex ONE = new Complex(1, 0, true);
    public static final Complex I = new Complex(1, Math.PI / 2, true);
    public static final int OPERATOR_INVERSE = 0, OPERATOR_EXP = 1,
            OPERATOR_LN = -1, OPERATOR_SIN = 2,
            OPERATOR_COS = 3, OPERATOR_TAN = 4;

    public static final int OPERATOR_POWER(int n) {
        return n + 3;
    }

    public Complex(double r, double theta, boolean polar) {
        if (polar) {
            this.r = r;
            this.theta = theta;
        } else {
            this.r = Math.sqrt(r * r + theta * theta);
            this.theta = calcTheta(r, theta);
        }

    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getTheta() {
        return theta;
    }

    public double calcTheta(double a, double b) {
        double the = 0;
        if (a == 0 && b == 0) {
            the = 0;
        } else if (a == 0) {
            the = Math.PI / 2 * (Math.abs(b) / b);
        } else if (b == 0) {
            if (a < 0) {
                the = Math.PI;
            } else {
                the = 0;
            }
        } else if (a > 0) {
            the = Math.atan(b / a);
        } else if (a < 0) {
            if (b > 0) {
                the = Math.atan(b / a) + Math.PI;
            } else {
                the = Math.atan(b / a) - Math.PI;
            }
        }
        return the;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getRe() {
        return r * Math.cos(theta);
    }

    public double getIm() {
        return r * Math.sin(theta);
    }
    
    public double getPrinciple(double t){
        if(t>0){
            t = t%(Math.PI*2);
            if(t<Math.PI){
                return t;
            }else{
                return t-2*Math.PI;
            }
        }else{
            t = t%(Math.PI*2);
            if(t>-Math.PI){
                return t;
            }else{
                return t+2*Math.PI;
            }
        }
    }

    public Complex operator(int id) {
        switch (id) {
            case OPERATOR_INVERSE:
                return minverse();
            case OPERATOR_EXP:
                return exp();
            case OPERATOR_LN:
                return ln();
            case OPERATOR_SIN:
                return sin();
            case -OPERATOR_SIN:
                return asin();
            case OPERATOR_COS:
                return cos();
            case -OPERATOR_COS:
                return acos();
            case OPERATOR_TAN:
                return tan();
            case -OPERATOR_TAN:
                return atan();
            default:
                if (id >= OPERATOR_POWER(2)) {
                    return power(id - OPERATOR_POWER(0));
                } else {
                    return power(1, Math.abs(id) - OPERATOR_POWER(0));
                }
        }
    }

    public Complex add(double x2, double y2) {
        double x = r * Math.cos(theta) + x2;
        double y = r * Math.sin(theta) + y2;
        return new Complex(Math.sqrt(x * x + y * y), calcTheta(x, y), true);
    }

    public Complex add(Complex num) {
        double x = r * Math.cos(theta) + num.getRe();
        double y = r * Math.sin(theta) + num.getIm();
        return new Complex(Math.sqrt(x * x + y * y), calcTheta(x, y), true);
    }

    public Complex power(int numer, int denom) {
        double the = getPrinciple(theta * numer / denom);//+(4*Math.PI*numer/denom);
        return new Complex(Math.pow(r, 1.0 * numer / denom), the, true);
    }

    public Complex power(int numer) {
        return power(numer, 1);
    }

    public Complex multiply(double num) {
        return new Complex(r * num, theta, true);
    }

    public Complex multiply(Complex num) {
        double t = getPrinciple(theta + num.getTheta());
        return new Complex(r * num.getR(), t, true);
    }

    public Complex minverse() {
        return new Complex(1 / r, -theta, true);
    }

    public Complex ainverse() {
        double t = getPrinciple(theta + Math.PI);
        return new Complex(r, t, true);
    }

    public Complex conjugate() {
        return new Complex(r, -theta, true);
    }

    public Complex divide(Complex num) {
        double t = getPrinciple(theta - num.getTheta());
        return new Complex(r / num.getR(), t, true);
    }

    public String toString() {
        return "Polar: (" + r + ", " + theta + ")\tRectangular: (" + (r * Math.cos(theta)) + ", " + (r * Math.sin(theta)) + ")";
    }

    // The principle complex ln function
    public Complex ln() {
        return new Complex(Math.log(getR()), getTheta(), false);
        //return new Complex(Math.log(x*x + y*y)/2, Math.asin(y/Math.sqrt(x*x + y*y)), false);
    }

    public Complex atan() {
        double x = getRe();
        double y = getIm();
        double a = (1 - x * x - y * y) / (x * x + (y + 1) * (y + 1));
        double b = (2 * x) / (x * x + (1 + y) * (1 + y));
        Complex z = new Complex(a, b, false);
        z = z.ln().multiply(0.5).divide(Complex.I);

        return z;
    }

    public Complex acot() {
        double x = getRe();
        double y = getIm();
        double a = (-1 + x * x + y * y) / (x * x + (y - 1) * (y - 1));
        double b = (2 * x) / (x * x + (y - 1) * (y - 1));
        Complex z = new Complex(a, b, false);
        z = z.ln().multiply(0.5);

        return new Complex(z.getIm(), -z.getRe(), false);
    }

    public Complex asin() {
        Complex w = power(2).ainverse().add(1, 0);
        w = w.power(1, 2);
        Complex y = w.add(multiply(I));
        return y.ln().multiply(new Complex(1, -Math.PI / 2, true));
    }

    public Complex acos() {
        Complex w = power(2).ainverse().add(1, 0);
        w = w.power(1, 2);
        Complex y = w.multiply(new Complex(1, Math.PI / 2, true)).add(getRe(), getIm());
        return y.ln().multiply(new Complex(1, -Math.PI / 2, true));
    }

    public Complex exp() {
        double t = getPrinciple(getIm());
        return new Complex(Math.exp(getRe()), t, true);
    }

    public Complex tan() {
        double x = getRe();
        double y = getIm();
        Complex numer = new Complex(Math.tan(x), Math.tanh(y), false);
        Complex denom = new Complex(1, -Math.tan(x) * Math.tanh(y), false);
        return numer.divide(denom);
    }

    public Complex sin() {
        double x = getRe();
        double y = getIm();
        return new Complex(Math.sin(x) * Math.cosh(-y), -Math.cos(x) * Math.sinh(-y), false);
    }

    public Complex cos() {
        double x = getRe();
        double y = getIm();
        return new Complex(Math.cos(x) * Math.cosh(-y), Math.sin(x) * Math.sinh(-y), false);
    }

}
