/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maelstrom.mandelbrotgpu;

import com.maelstrom.mandelbrotgpu.mappers.Mapper;
import java.awt.image.BufferedImage;

/**
 *
 * @author Chris
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Mapper obj = new Mapper("buddha");
        //String fileDir = System.getProperty("user.dir") + "\\";
        String fileDir = "C:\\Users\\Chris\\Pictures\\backgrounds\\Throne\\";
        String fileName = "-3 4 5 better uncroppped.png";
        
        BufferedImage img = obj.readPNG(fileDir + fileName);
        img = obj.removeMiddlePixels(img);
        //img = obj.removeMiddlePixelsHorizontal(img);
        obj.savePNG(img, fileDir + fileName);
        // TODO code application logic here
    }
    
}
