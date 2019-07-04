#define _USE_MATH_DEFINES

double __attribute__ ((overloadable)) abs(double value) {
    return value >= 0 ? value : 0 - value;
}

struct Complex {
    double r;
    double theta;
};

double getRe(struct Complex c) {
    return c.r * cos(c.theta);
}

double getIm(struct Complex c) {
    return c.r * sin(c.theta);
}

double calcTheta(double a, double b) {
    double the = 0;
    if (a == 0 && b == 0) {
        the = 0;
    } else if (a == 0) {
        the = M_PI / 2 * (abs(b) / b);
    } else if (b == 0) {
        if (a < 0) {
            the = M_PI;
        } else {
            the = 0;
        }
    } else if (a > 0) {
        the = atan(b / a);
    } else if (a < 0) {
        if (b > 0) {
            the = atan(b / a) + M_PI;
        } else {
            the = atan(b / a) - M_PI;
        }
    }
    return the;
}

struct Complex newComplex(double r, double theta, bool polar) {
    struct Complex c;
    if (polar) {
        c.r = r;
        c.theta = theta;
        return c;
    } else {
        c.r = sqrt(r * r + theta * theta);
        c.theta = calcTheta(r, theta);
        return c;
    }
}

double getPrinciple(double t) {
    if(t>0){
            //t = t % (2*M_PI);
            t = fmod(t, 2*M_PI);
            if(t<M_PI){
                return t;
            }else{
                return t-2*M_PI;
            }
    }else{
            //t = t % (M_PI*2);
            t = fmod(t, 2*M_PI);
            if(t>-M_PI){
                return t;
            }else{
                return t+2*M_PI;
            }
    }
}

struct Complex addComplex(struct Complex c, struct Complex num) {
    double x = c.r * cos(c.theta) + getRe(num);
    double y = c.r * sin(c.theta) + getIm(num);
    return newComplex(sqrt(x * x + y * y), calcTheta(x, y), true);
}

struct Complex subComplex(struct Complex c, struct Complex num) {
    double x = c.r * cos(c.theta) - getRe(num);
    double y = c.r * sin(c.theta) - getIm(num);
    return newComplex(sqrt(x * x + y * y), calcTheta(x, y), true);
}

struct Complex powComplex(struct Complex c, int numer) {
    double the = getPrinciple(c.theta * numer);
    return newComplex(pow(c.r, 1.0 * numer), the, true);
}

double mod2Complex(struct Complex c) {return getRe(c)*getRe(c) + getIm(c)*getIm(c);}

struct Complex powFracComplex(struct Complex c, int numer, int denom) {
        double the = getPrinciple(c.theta * numer / denom);
        return newComplex(pow(c.r, 1.0 * numer / denom), the, true);
}

struct Complex multiplyScalarComplex(struct Complex c, double num) {
        return newComplex(c.r * num, c.theta, true);
    }

struct Complex multiplyComplex(struct Complex c, struct Complex num) {
        double t = getPrinciple(c.theta + num.theta);
        return newComplex(c.r * num.r, t, true);
    }

    struct Complex minverse(struct Complex c) {
        return newComplex(1 / c.r, -c.theta, true);
    }

    struct Complex ainverse(struct Complex c) {
        double t = getPrinciple(c.theta + M_PI);
        return newComplex(c.r, t, true);
    }

    struct Complex conjugate(struct Complex c) {
        return newComplex(c.r, -c.theta, true);
    }

    struct Complex divideComplex(struct Complex c, struct Complex num) {
        double t = getPrinciple(c.theta - num.theta);
        return newComplex(c.r / num.r, t, true);
    }

    // The principle complex ln function
    struct Complex lnComplex(struct Complex c) {
        return newComplex(log(c.r), c.theta, false);
    }

    struct Complex atanComplex(struct Complex c) {
        double x = getRe(c);
        double y = getIm(c);
        double a = (1 - x * x - y * y) / (x * x + (y + 1) * (y + 1));
        double b = (2 * x) / (x * x + (1 + y) * (1 + y));
        struct Complex z = newComplex(a, b, false);
        z = divideComplex(multiplyScalarComplex(lnComplex(z),0.5), newComplex(1,M_PI/2,true));

        return z;
    }

    struct Complex acotComplex(struct Complex c) {
        double x = getRe(c);
        double y = getIm(c);
        double a = (-1 + x * x + y * y) / (x * x + (y - 1) * (y - 1));
        double b = (2 * x) / (x * x + (y - 1) * (y - 1));
        struct Complex z = newComplex(a, b, false);
        z = multiplyScalarComplex(lnComplex(z),0.5);

        return newComplex(getIm(z), -getRe(z), false);
    }

    struct Complex asinComplex(struct Complex c) {
        struct Complex w = addComplex(ainverse(powComplex(c, 2)), newComplex(1, 0, true));
        w = powFracComplex(w, 1, 2);
        struct Complex y = addComplex(multiplyComplex(c, newComplex(1, M_PI/2, true)), w);
        return multiplyComplex(lnComplex(y), newComplex(1, -M_PI / 2, true));
    }

    struct Complex acosComplex(struct Complex c) {
        struct Complex w = addComplex(ainverse(powComplex(c, 2)), newComplex(1, 0, true));
        w = powFracComplex(w, 1, 2);
        struct Complex y = addComplex(multiplyComplex(w, newComplex(1, M_PI / 2, true)), c);
        return multiplyComplex(lnComplex(y), newComplex(1, -M_PI / 2, true));
    }

    struct Complex expComplex(struct Complex c) {
        double t = getPrinciple(getIm(c));
        return newComplex(exp(getRe(c)), t, true);
    }

    struct Complex tanComplex(struct Complex c) {
        double x = getRe(c);
        double y = getIm(c);
        struct Complex numer = newComplex(tan(x), tanh(y), false);
        struct Complex denom = newComplex(1, -tan(x) * tanh(y), false);
        return divideComplex(numer, denom);
    }

    struct Complex sinComplex(struct Complex c) {
        double x = getRe(c);
        double y = getIm(c);
        return newComplex(sin(x) * cosh(-y), -cos(x) * sinh(-y), false);
    }

    struct Complex cosComplex(struct Complex c) {
        double x = getRe(c);
        double y = getIm(c);
        return newComplex(cos(x) * cosh(-y), sin(x) * sinh(-y), false);
    }
	
	struct Complex operate(struct Complex c, int id) {
        switch (id) {
            case 0:
                return minverse(c);
            case 1:
                return expComplex(c);
            case -1:
                return lnComplex(c);
            case 2:
                return sinComplex(c);
            case -2:
                return asinComplex(c);
            case 3:
                return cosComplex(c);
            case -3:
                return acosComplex(c);
            case 4:
                return tanComplex(c);
            case -4:
                return atanComplex(c);
            default:
                if (id >= 5) {
                    return powComplex(c, id - 3);
                } else {
                    return powFracComplex(c, 1, abs(id) - 3);
                }
        }
    }
	
	