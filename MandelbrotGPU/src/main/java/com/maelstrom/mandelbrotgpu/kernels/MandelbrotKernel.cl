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
		struct Complex znMinOne = newComplex(*start + (*stop - *start) * x / *width, *top + (*bottom - *top) * y / *height, false);
		znMinOne = transform(znMinOne);

		struct Complex c = znMinOne;

		results[id] = 0;

		const double threshold = 2;
		results[id] = -1;
                struct Complex zn;
		for(int i = 1; i < *iterations; i++) {
			zn = fn(znMinOne, c, i);

			if(zn.r > threshold) {
				double k = (threshold - znMinOne.r) / abs(znMinOne.r - zn.r);
				if(k < 0) k = 0.0;
					results[id] = i + k - 1;
					break;
			}

			znMinOne = zn;
		}
	}
};