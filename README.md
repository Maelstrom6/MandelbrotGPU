# MandelbrotGPU
Netbeans GPU based Mandelbrot, Julia set, buddhabrot and orbit trap creator.

This repository uses OpenCL for the GPU project (MandelbrotGPU) and the CPU project (Mandelbrot3) requires no imports. Note that if you are running Linux, OpenCL may not work correctly.

NOTE: This project is still "work-in-progress". I still plan to add some new color schemes and possibly remove double precision in favour of arbitrary precision. I might still add fractal flames too.


### Images
Here is the [Google Drive link](https://drive.google.com/drive/folders/10An0V6lK1VaXeYbSASxmnNwJeSDXLgH7?usp=sharing) to most of the images the program has made.

<img src="MandelbrotGPU/Basic%20Mandelbrots/MyNewTest0.png" width="500">

<img src="MandelbrotGPU/Basic%20Mandelbrots/totalTime28s.png" width="500" >

<img src="MandelbrotGPU/Basic%20Mandelbrots/MyNewTest1%202%20-4.png" width="500" >

<img src="MandelbrotGPU/Basic%20Mandelbrots/3%202%200%201000%20iterations.png" width="500">

<img src="MandelbrotGPU/Basic%20Mandelbrots/Orbit%20Trap%20Cross.png" width="500" >

### Usage for MandelbrotGPU UI
The LauncherUI class is the main class with all the things you need for the project.

![Screenshot](https://drive.google.com/uc?export=view&id=1X7EfiRNe0wIVQ3j_IxcvyYbGlHikUARU)

### Usage for MandelbrotGPU "API"
The Launcher class is a class with some code usage examples.
##### Example 1
The following example makes the default image which is a typical 500x500 image of a Mandelbrot with 5000 iterations from -2-2i to 2+2i:
```java
FractalSettings settings = new FractalSettings();
//Adjust the values of settings as you wish.
Mapper obj = new Mapper(settings.fractalType);
BufferedImage image = obj.createProgramAndImage(settings);
obj.savePNG(image, System.getProperty("user.dir") + "\\MyNewTest.png");
```
new FractalSettings() creates a defualt instance of FractalSettings: A typical mandelbrot from -2-2i to 2+2i with 500 iterations.

new Mapper(settings.fractalType) creates a new Mapper instance and only one instance should be made throughout runtime. This is because of OpenCL and its initialization. For some reason multiple calls can be made using the UI but it seems to keep on crashing when creating different image types in large batches.

createProgramAndImage(settings) loads the programs, kernels and so on and then creates the desired image. Multiple calls of this can be made during runtime. An alternative to this is the following:
```java
Mapper obj = new Mapper(settings.fractalType);
obj.loadProgram(settings.fn, settings.transformOperators, settings.maxIterations, settings.calculateComplex);
BufferedImage image = createImage(settings);
```
This alterantive can be used if you are creating multiple images without changing fn, transformOperators, maxIterations or calculateComplex.

The variable calculateComplex should generally be false unless you are creating large mandelbrot images that would typically timeout the kernel. (My personal machine has a timeout of 20 seconds.)

##### Example 2
The following example makes a 1000x1000 image buddhabrot of an inverse transformation mandelbrot from -4-4i to 4+4i with a maximum of 100 iterations.
```java
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

### Usage for Mandelbrot3 "API"
The Mand class is the most important class here.
The MandelbrotStill class has some usage examples.
The main idea is as follows:
```java
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

