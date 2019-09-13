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
//        Mapper obj = new Mapper("buddha");
//        //String fileDir = System.getProperty("user.dir") + "\\";
//        String fileDir = "C:\\Users\\Chris\\Pictures\\backgrounds\\Throne\\";
//        String fileName = "-3 4 5 better uncroppped.png";
//
//        BufferedImage img = obj.readPNG(fileDir + fileName);
//        img = obj.removeMiddlePixels(img);
//        //img = obj.removeMiddlePixelsHorizontal(img);
//        obj.savePNG(img, fileDir + fileName);
        
        // Use Newtons method to find solutions to equation
        String str = "x^16 + 8 x^15 + 28 x^14 + 60 x^13 + 94 x^12 + 116 x^11 + 114 x^10 + 94 x^9 + 69 x^8 + 44 x^7 + 26 x^6 + 14 x^5 + 5 x^4 + 2 x^3 + x^2 +x";
        
        str = "16 x^15 + 120 x^14 + 392 x^13 + 780 x^12 + 1128 x^11 + 1276 x^10 + 1140 x^9 + 846 x^8 + 552 x^7 + 308 x^6 + 156 x^5 + 70 x^4 + 20 x^3 + 6 x^2 + 2 x + 1";
        str = "x^32 + 16 x^31 + 120 x^30 + 568 x^29 + 1932 x^28 + 5096 x^27 + 10948 x^26 + 19788 x^25 + 30782 x^24 + 41944 x^23 + 50788 x^22 + 55308 x^21 + 54746 x^20 + 49700 x^19 + 41658 x^18 + 32398 x^17 + 23461 x^16 + 15864 x^15 + 10068 x^14 + 6036 x^13 + 3434 x^12 + 1860 x^11 + 958 x^10 + 470 x^9 + 221 x^8 + 100 x^7 + 42 x^6 + 14 x^5 + 5 x^4 + 2 x^3 +x^2 +x";
        System.out.println(str.replace("^", "**").replace(" x", "*x"));
        // TODO code application logic here
    }
    
    public static Complex f(Complex x){
        //x^16 + 8 x^15 + 28 x^14 + 60 x^13 + 94 x^12 + 116 x^11 + 114 x^10 + 94 x^9 + 69 x^8 + 44 x^7 + 26 x^6 + 14 x^5 + 5 x^4 + 2 x^3 + x^2 +x
        //Complex numer = 
        return null;
    }



    }

