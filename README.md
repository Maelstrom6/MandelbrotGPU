# MandelbrotGPU
Netbeans GPU based mandelbrot and buddhabrot creator.

NOTE: This project is very "work-in-progress" and there are LOTS of unused functions and lots of spagetti.
I'm still bringing over code from Mandelbrot3 to MandelbrotCPU. Multiple color schemes are still to be added.

### Images Guide
Under the Mandelbrot3 project there are folders of images.
Folders called Mandelbrots contain mandelbrots while folders called Buddhas contain corresponding Buddhabrots.

- Mandelbrots 1 and Buddhas 1 contains 13 images each.
- Mandelbrots 2 and Buddhas 2 contains 169 images each.
- Mandelbrots 3 and Buddhas 3 contains 1872 300x300 images of mandelbrots and buddhabrots respectivaley.

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
The image will have dimesnions width x height. 
The centre of the image will be the complex coordinate: cenreX + i\*centreY.
The bottom left corner is the point (cenreX - limX) + i\*(cenreY - limY) and similarly the top corner is the complex point (cenreX + limX) + i\*(cenreY + limY).
The calulatoin will go on for maximumIterations iterations.
The operators variable is an ArrayList of operator id's as shown above.

For the second line, all functions that being with "map" will return a buffered image of your desired result. Note: all Julia mappings are actually centred at the point 0 + 0i and centreX, centreY define the value of f0(z).

The third line is simply a generic function that saves buffered images to the desired file.


### Usage for MandelbrotGPU
The Launcher class is a class with some usage examples. The main usage is as follows:
```
FractalSettings settings = new FractalSettings();
//Adjust the values of settings as you wish.
obj = new FractalManager();
obj.LoadProgram(settings.fractalType, settings.fn, settings.transformOperators, 0);
BufferedImage image = obj.createImageSimple(settings, 0);
obj.savePNG(image, System.getProperty("user.dir") + "\\MyNewTest.png");
```
new FractalSettings() creates a defualt instance of FractalSettings: A typical mandelbrot from -2-2i to 2+2i with with 500 iterations.

new FractalManager() creates a new FractalManager instance and only one instance should be made throughout runtime.

LoadProgram loads the programs, kernels and so on. Multiple calls of this can be made during runtime. Ideally, the LoadProgram method should not need any parameters but in order to create the kernel, some settings are required.

createImageSimple creates the image and performs the same function as mapNoMirror as in the Mandelbrot 3 project. createImage should not be used unless you are creating large mandelbrot images that would typically timeout under createImage.
