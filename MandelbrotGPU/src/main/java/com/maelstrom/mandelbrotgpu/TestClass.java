    /*
    * To change this license header, choose License Headers in Project Properties.
    * To change this template file, choose Tools | Templates
    * and open the template in the editor.
    */
    package com.maelstrom.mandelbrotgpu;

    import com.maelstrom.mandelbrotgpu.mappers.Mapper;
import com.maelstrom.myMandelbrot.Complex;
    import java.awt.image.BufferedImage;

    /**
    *
    * @author Chris
    */
    public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static double E = 0.0001;
    public static void main(String[] args) {
        Mapper obj = new Mapper("buddha");
        obj = new Mapper("mandelbrot");
        FractalSettings settings = NotableSettings.Scorpion();
        obj.savePNG(obj.createProgramAndImage(settings), "hi.png");
    }
    
    public static Complex f(Complex x){
        //x^16 + 8 x^15 + 28 x^14 + 60 x^13 + 94 x^12 + 116 x^11 + 114 x^10 + 94 x^9 + 69 x^8 + 44 x^7 + 26 x^6 + 14 x^5 + 5 x^4 + 2 x^3 + x^2 +x
        //Complex numer = 
        return null;
    }



    }

