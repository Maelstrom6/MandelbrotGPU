package com.maelstrom.mandelbrotgpu;

import com.maelstrom.mandelbrotgpu.mappers.Mapper;

/**
 *
 * @author Chris
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        FractalSettings settings = new FractalSettings();
        settings.maxIterations = 100;
        //FractalManager obj;
        settings.sizeX = 500;
        settings.sizeY = 500;
        settings.fractalType = "Buddha";
        //settings.fractalType = "Mandelbrot";
        settings.fractalType = "Julia";
        settings.f0Re = -0.8;
        settings.f0Im = 0.156;
        settings.transformOperators.add(-1);
        //settings.transformOperators.add(6);
        //settings.transformOperators.add(4);
        settings.leftest = -2;
        settings.rightest = 2;
        settings.highest = 2;
        settings.lowest = -2;
        //settings.fn = "addComplex(powComplex(zn, 3), c)";
        //obj = new FractalManager();
        //obj.savePNG(obj.createProgramAndImage(settings, 0), System.getProperty("user.dir") + "\\MyNewTest.png");
        Mapper obj = new Mapper(settings.fractalType);
        obj.savePNG(obj.createProgramAndImage(settings, 0), System.getProperty("user.dir") + "\\MyNewTest.png");
        
        /*for(int i = 0;i<=10;i++){//going from 1/2 to 1/3
            settings.transformOperators.clear();
            //settings.transformOperators.add(-13); // The 1/10th power
            //settings.transformOperators.add(23+i); // The 20+ith power
            settings.transformOperators.add(-2);
            settings.transformOperators.add(4);
            settings.transformOperators.add(-13); // The 1/10th power
            settings.transformOperators.add(23+i); // The 20+ith power
            //settings.transformOperators.add(-(23+i)); // The 20+ith power
            //settings.transformOperators.add(13); // The 1/10th power
            
            
            obj.LoadProgram(settings.fractalType, settings.fn, settings.transformOperators, 0);
            obj.savePNG(obj.createImageSimple(settings, 0), System.getProperty("user.dir") + "\\Transitions3\\MyNewTest" + i + ".png");
        }*/
        
        /*double speed = 0.01;
        for(double i = 0;i<=1;i+=speed){//going from 1/2 to 1/3
            
            settings.transformOperators.clear();
            //settings.transformOperators.add(-13); // The 1/10th power
            //settings.transformOperators.add(23+i); // The 20+ith power
            settings.transformOperators.add(-2);
            settings.transformOperators.add(4);
            settings.transformOperators.add(-power((int)(1/speed))); // The 1/10th power
            settings.transformOperators.add(power((int)(2/speed+i/speed))); // The 20+ith power
            //settings.transformOperators.add(-(23+i)); // The 20+ith power
            //settings.transformOperators.add(13); // The 1/10th power
            obj.savePNG(obj.createProgramAndImage(settings, 0), System.getProperty("user.dir") + "\\Transitions4\\MyNewTest" + Math.round(i*1000) + ".png");
        }*/
        
        /*for (int i = -6; i <= 6; i++) {
            settings.transformOperators.clear();
            settings.transformOperators.add(i);
            obj.LoadProgram(settings.fractalType, settings.fn, settings.transformOperators, 0);
            obj.savePNG(obj.createImageSimple(settings, 0), System.getProperty("user.dir") + "\\Buddhas cubed 1\\MyNewTest" + i + ".png");
        }*/

        /*for (int i = -6; i <= 6; i++) {
            for (int j = -6; j <= 6; j++) {
                for (int k = -6; k <= 6; k++) {
                    if (i == -j || j == -k) {
                        continue;
                    }
                    settings.transformOperators.clear();
                    settings.transformOperators.add(i);
                    settings.transformOperators.add(j);
                    settings.transformOperators.add(k);
                    obj.LoadProgram(settings.fractalType, settings.fn, settings.transformOperators, 0);
                    obj.savePNG(obj.createImageSimple(settings, 0), System.getProperty("user.dir") + "\\Mandelbrots 3\\MyNewTest" + i + " " + j + " " + k + ".png");
                }
            }
        }*/

        
        // TODO code application logic here
    }
    
    public static int power(int i){
        return i+3;
    }

}
