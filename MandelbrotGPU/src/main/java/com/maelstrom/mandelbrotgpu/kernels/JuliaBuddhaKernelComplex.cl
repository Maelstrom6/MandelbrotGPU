kernel void fractalKernel(
	global const double *start,
	global const double *stop,
	global const double *top,
	global const double *bottom,
	

	global const int    *width,
	global const int    *height,
	global const int    *iterations,

        global const double *fullstart,
	global const double *fullstop,
	global const double *fulltop,
	global const double *fullbottom,

        global const int *fullWidth,
        global const int *fullHeight,

        global const double *f0Re,
        global const double *f0Im,

        global const double *threshold,

	global double *results
) {

	/* Find out where we are */
	const int id = get_global_id(0);
	const int x = id % *width;
	const int y = id / *width;
        const int iter = *iterations;
	/* Check that we should calculate for this pixel */
	if(y < *fullHeight) {
		struct Complex zn = newComplex(*start + (*stop - *start) * x / *width, *top + (*bottom - *top) * y / *height, false);
		zn = transform(zn);

		struct Complex c = newComplex(*f0Re, *f0Im, false);
                //c = transform(c); // It is unclear whether to transform or not so I just chose not to

                struct Complex visitedCoordinates[INSERT ITERATIONS HERE] = {newComplex(0,0,true)};
                //Need to replace the above 5000 with *iterations


		for(int i = 0; i < *iterations; i++) {
			zn = fn(zn, c, i);

			visitedCoordinates[i] = zn;

			if(zn.r > *threshold) {
				int j=0;
                                while(visitedCoordinates[j].r != 0){
                                        visitedCoordinates[j] = inverseTransform(visitedCoordinates[j]);
                                        double xCoordinate = getRe(visitedCoordinates[j]);
                                        double yCoordinate = getIm(visitedCoordinates[j]);
                                        int xPixel = round(1.0 * (xCoordinate - *fullstart) * *fullWidth / (*fullstop - *fullstart));
                                        int yPixel = round(1.0 * (yCoordinate - *fulltop) * *fullHeight / (*fullbottom - *fulltop));
                                        int index = yPixel * *fullWidth + xPixel;
                                        if(index >= 0 && index < *fullWidth * *fullHeight){//*width * *height
                                        
                                                if(j < 0.01 * *iterations){
                                                    results[(index) * 3 + 2] = results[(index) * 3 + 2] + 1;
                                                }
                                                if(j < 0.1 * *iterations){
                                                    results[(index) * 3 + 1] = results[(index) * 3 + 1] + 1;
                                                }
                                                results[(index) * 3] = results[(index) * 3] + 1;
                                        }
                                        j = j + 1;
                                }
                                break;
			}
		}
		
	}
};