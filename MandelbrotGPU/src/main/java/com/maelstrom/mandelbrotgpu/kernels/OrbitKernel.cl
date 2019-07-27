kernel void fractalKernel(
	global const double *start,
	global const double *stop,
	global const double *top,
	global const double *bottom,
	

	global const int    *width,
	global const int    *height,
	global const int    *iterations,

        global const double *f0Re,
	global const double *f0Im,

        global const double *threshold,

        global const int *orbitID,

	global double *results
) {

	/* Find out where we are */
	const int id = get_global_id(0);
	const int x = id % *width;
	const int y = id / *width;

	/* Check that we should calculate for this pixel */
	//if(y < *height) {
                struct Complex c = newComplex(*start + (*stop - *start) * x / *width, *top + (*bottom - *top) * y / *height, false);
                c = transform(c);
                
		struct Complex znMinOne = newComplex(*f0Re, *f0Im, false);
                struct Complex zn;
		results[id] = 0;
                double distance = 1000000;
                double minAcceptable = 0.4;
                double maxAcceptable = 0.5;
                struct Complex trapPoint = newComplex(-0.1, 0, false);

                switch(*orbitID){
                    case 1:
                        // Orbit trap with a cross
                        for(int i = 1; i < *iterations; i++) {
                                zn = fn(znMinOne, c, i);

                                if(zn.r > *threshold) {
                                        break;
                                }

                                double horiDist = abs(getRe(trapPoint) - getRe(zn));
                                double vertDist = abs(getIm(trapPoint) - getIm(zn));
                                if(vertDist < distance){
                                    distance = vertDist;
                                }
                                if(horiDist < distance){
                                    distance = horiDist;
                                }

                                znMinOne = zn;
                        }
                        break;
                    case 2:
                        // Orbit trap with a ring
                        
                        for(int i = 1; i < *iterations; i++) {
                                zn = fn(znMinOne, c, i);

                                if(zn.r > *threshold) {
                                        break;
                                }

                                double zMinusPointModulus = addComplex(zn, ainverse(trapPoint)).r;
                                if(zMinusPointModulus < distance && minAcceptable < zMinusPointModulus && zMinusPointModulus < maxAcceptable){
                                        distance = zMinusPointModulus;
                                }

                                znMinOne = zn;
                        }
                        break;
                    default:
                        // Orbit trap with a dot
                        for(int i = 1; i < *iterations; i++) {
                                zn = fn(znMinOne, c, i);

                                if(zn.r > *threshold) {
                                        break;
                                }

                                double zMinusPointModulus = addComplex(zn, ainverse(trapPoint)).r;
                                if(zMinusPointModulus < distance){
                                        distance = zMinusPointModulus;
                                }

                                znMinOne = zn;
                        }
                        break;
                }
                results[id] = 100 * (distance );
                
		
	//}
};