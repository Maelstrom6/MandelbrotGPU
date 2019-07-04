# MandelbrotGPU
Netbeans GPU based mandelbrot and buddhabrot creator.

NOTE: This project is very "work-in-progress" and there are LOTS of unused functions and lots of spagetti.
I'm still bringing over code from Mandelbrot3 to MandelbrotCPU.

Under the Mandelbrot3 project there are folders of images.
Folders called Mandelbrots contain mandelbrots while folders called Buddhas contain corresponding Buddhabrots.

Mandelbrots 1 and Buddhas 1 contains 13 images each.
Mandelbrots 2 and Buddhas 2 contains 169 images each.
Mandelbrots 3 and Buddhas 3 contains 1872 300x300 images of mandelbrots and buddhabrots respectivaley.

The bottom left corner is -4-4i and the top corner is 4+4i for each image.
The names of each image have a naming convention: "MyNewTesta b c" where a,b,c are integers.
These integers refer to the operation ID of each transformation.
The operations ID's are as follows for a given complex number z:
0 - 1/z
1 - exp(z)
2 - sin(z)
3 - cos(z)
4 - tan(z)
5 - z^2
6 - z^3
...
-1 - ln(z)
-2 - asin(z)
-3 - acos(z)
-4 - atan(z)
-5 - sqrt(z) (only the first solution)
-6 - cbrt(z) (only the first solution)
...

An important note is that all these operations have an inverse and the inverse operation of a is -a.
The order of a,b,c follows Reverse Polish Notation. 
For example, if we have a file called "MyNewTest-2 3 6", then we know we have sqrt(z), cos(z) and z^3 involved. 
They combine as follows: (cos(sqrt(z)))^3.
So the first operation is applied first, then the second, then the third and so on.
