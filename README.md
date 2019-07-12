# MandelbrotGPU
Netbeans GPU based Mandelbrot and buddhabrot creator.

This repository uses OpenCL for the GPU project (MandelbrotGPU) and the CPU project (Mandelbrot3) requires no imports. Note that if you are running Linux, OpenCL may not work correctly.

NOTE: This project is very "work-in-progress" and there are LOTS of unused functions and lots of spaghetti.
I'm still bringing over code from Mandelbrot3 to MandelbrotCPU. Multiple color schemes are still to be added. It might have a possibility of implementing fractal flames.

### Images Guide
Under the Mandelbrot3 project there are folders of images.
Folders called Mandelbrots contain mandelbrots while folders called Buddhas contain corresponding Buddhabrots.

- Mandelbrots 1 and Buddhas 1 contains 13 images each.
- Mandelbrots 2 and Buddhas 2 contains 169 images each.
- Mandelbrots 3 and Buddhas 3 contains 1872 300x300 images of mandelbrots and buddhabrots respectively.

The bottom left corner is -4-4i and the top corner is 4+4i for each image.
The names of each image have a naming convention: "MyNewTesta b c" where a,b,c are integers.
These integers refer to the operation ID of each transformation.
The operations ID's are as follows for a given complex number z:
- 0 - 1/z
- 1 - exp(z)
- 2 - sin(z)
- 3 - cos(z)
- 4 - tan(z)
- 5 - z^2
- 6 - z^3
- ...
- -1 - ln(z)
- -2 - asin(z)
- -3 - acos(z)
- -4 - atan(z)
- -5 - sqrt(z) (only the first solution)
- -6 - cbrt(z) (only the first solution)
- ...

An important note is that all these operations have an inverse and the inverse operation of a is -a.
The order of a,b,c follows Reverse Polish Notation. 
For example, if we have a file called "MyNewTest-2 3 6", then we know we have sqrt(z), cos(z) and z^3 involved. 
They combine as follows: (cos(sqrt(z)))^3.
So the first operation is applied first, then the second, then the third and so on.


### Usage for Mandelbrot3
The Mand class is the most important class here.
The MandelbrotStill class has some usage examples.
The main idea is as follows:
```
Mand obj = new Mand(numerator, denominator, width, height, limX, limY, centreX, centreY, maximumIterations, operators);
BufferedImage image = obj.mapNoMirror();
obj.savePNG(image, System.getProperty("user.dir") + "Mandelbrot Picture.png");
```
In the first line, we create a new object whose main mandelbrot formula is fn(z)=z^(numerator/denominator)+c.
The image will have dimensions width x height. 
The centre of the image will be the complex coordinate: cenreX + i\*centreY.
The bottom left corner is the point (cenreX - limX) + i\*(cenreY - limY) and similarly the top corner is the complex point (cenreX + limX) + i\*(cenreY + limY).
The calulatoin will go on for maximumIterations iterations.
The operators variable is an ArrayList of operator id's as shown above.

For the second line, all functions that being with "map" will return a buffered image of your desired result. Note: all Julia mappings are actually centred at the point 0 + 0i and centreX, centreY define the value of f0(z).

The third line is simply a generic function that saves buffered images to the desired file.


### Usage for MandelbrotGPU
The Launcher class is a class with some usage examples.
##### Example 1
The following example makes the default image which is a typical 500x500 image of a Mandelbrot with 5000 iterations from -2-2i to 2+2i:
```
FractalSettings settings = new FractalSettings();
//Adjust the values of settings as you wish.
Mapper obj = new Mapper(settings.fractalType);
BufferedImage image = obj.createProgramAndImage(settings);
obj.savePNG(image, System.getProperty("user.dir") + "\\MyNewTest.png");
```
new FractalSettings() creates a defualt instance of FractalSettings: A typical mandelbrot from -2-2i to 2+2i with 500 iterations.

new Mapper(settings.fractalType) creates a new Mapper instance and only one instance should be made throughout runtime. This is because of OpenCL and its initialization.

createProgramAndImage(settings) loads the programs, kernels and so on and then creates the desired image. Multiple calls of this can be made during runtime. An alternative to this is the following:
```
Mapper obj = new Mapper(settings.fractalType);
obj.loadProgram(settings.fn, settings.transformOperators, settings.maxIterations, settings.calculateComplex);
BufferedImage image = createImage(settings);
```
This alterantive can be used if you are creating multiple images without changing fn, transformOperators, maxIterations or calculateComplex.

The variable calculateComplex should generally be false unless you are creating large mandelbrot images that would typically timeout the kernel.

##### Example 2
The following example makes a 1000x1000 image buddhabrot of an inverse transformation mandelbrot from -4-4i to 4+4i with a maximum of 100 iterations.
```
FractalSettings settings = new FractalSettings();
settings.maxIterations = 100;
settings.sizeX = 1000;
settings.sizeY = 1000;
settings.fractalType = "Buddha";
settings.transformOperators.add(0);
settings.leftest = -4;
settings.rightest = 4;
settings.highest = 4;
settings.lowest = -4;
Mapper obj = new Mapper(settings.fractalType);
obj.savePNG(obj.createProgramAndImage(settings), System.getProperty("user.dir") + "\\MyNewTest.png");
```

