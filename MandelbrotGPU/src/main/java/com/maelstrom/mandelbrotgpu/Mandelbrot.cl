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
    while (t >= M_PI) {
        t = t - 2 * M_PI;
    }
    while (t <= -M_PI) {
        t = t + 2 * M_PI;
    }
    return t;
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


kernel void fractalKernel(
	global const double *start,
	global const double *stop,
	global const double *top,
	global const double *bottom,

	global const int    *width,
	global const int    *height,
	global const int    *iterations,

	global double *results
) {
	/* For explaination, refer to JavaCalculator.java */

	/* Find out where we are */
	const int id = get_global_id(0);
	const int x = id % *width;
	const int y = id / *width;

	/* Check that we should calculate for this pixel */
	if(y < *height) {
		struct Complex past = newComplex(*start + (*stop - *start) * x / *width, *top + (*bottom - *top) * y / *height, false);

		/* Using DBL_MAX to indicate that there is no seed */
		struct Complex base = past;

		results[id] = 0;
                if(false){

		}else{
			const double threshold = 4;
                        const int order = 2;
			results[id] = -1;
                        for(int i = 1; i < *iterations; i++) {
                            past = addComplex(powComplex(past, order), base);

                            if(mod2Complex(past) > threshold) {
                                results[id] = i - 1;
				break;
                            }
			}
					
				
		}
	}
};