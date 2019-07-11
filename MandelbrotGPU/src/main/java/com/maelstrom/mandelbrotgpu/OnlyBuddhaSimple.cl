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

	/* Find out where we are */
	const int id = get_global_id(0);
	const int x = id % *width;
	const int y = id / *width;
	/* Check that we should calculate for this pixel */
	if(y < *height) {
		struct Complex zn = newComplex(*start + (*stop - *start) * x / *width, *top + (*bottom - *top) * y / *height, false);
		zn = transform(zn);

		struct Complex c = zn;

		const double threshold = 2;
                struct Complex visitedCoordinates[INSERT ITERATIONS HERE] = {newComplex(0,0,true)};


		for(int i = 0; i < *iterations; i++) {
			zn = fn(zn, c, i);

			visitedCoordinates[i] = zn;

			if(zn.r > threshold) {
				int j=0;
                                while(visitedCoordinates[j].r != 0){
                                        visitedCoordinates[j] = inverseTransform(visitedCoordinates[j]);
                                        double xCoordinate = getRe(visitedCoordinates[j]);
                                        double yCoordinate = getIm(visitedCoordinates[j]);
                                        int xPixel = round(1.0 * (xCoordinate - *start) * *width / (*stop - *start));
                                        int yPixel = round(1.0 * (yCoordinate - *top) * *height / (*bottom - *top));
                                        if(yPixel * *width + xPixel >= 0 && yPixel * *width + xPixel < *width * *height){//*width * *height
                                                if(j < 0.01 * *iterations){
                                                    results[(yPixel * *width + xPixel) * 3 + 2] = results[(yPixel * *width + xPixel) * 3 + 2] + 1;
                                                }
                                                if(j < 0.1 * *iterations){
                                                    results[(yPixel * *width + xPixel) * 3 + 1] = results[(yPixel * *width + xPixel) * 3 + 1] + 1;
                                                }
                                                results[(yPixel * *width + xPixel) * 3] = results[(yPixel * *width + xPixel) * 3] + 1;
                                        }
                                        j = j + 1;
                                }
                                break;
			}
		}
		
	}
};