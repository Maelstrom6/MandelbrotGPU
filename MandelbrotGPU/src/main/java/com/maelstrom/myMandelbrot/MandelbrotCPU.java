/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maelstrom.myMandelbrot;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Chris
 */
public class MandelbrotCPU {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Integer> operators = new ArrayList();
        MapperMandelbrot obj = new MapperMandelbrot(2, 1, 500, 500, 4, 4, 0, 0, 500, operators);
        obj.savePNG(obj.createImageNoMirror(), System.getProperty("user.dir") + "/MyNewTest.png");
        // TODO code application logic here
    }
    
}
