/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot3;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chris
 */
public class MandelbrotStill {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Mand2 obj=new Mand2(1920,1080,0.00000001,0.00000001,0.395995455,0.605005292);
        //Mand3 obj=new Mand3(2,1,19200,10800,1.2,1.2,0,0);

        //Mand3 obj=new Mand3(2,1,500,500,Math.pow(10, -700/50.0),Math.pow(10, -700/50.0),0.38943817301526623,0.6080505280576015,5000,4);
        ArrayList<Integer> operators = new ArrayList();
        //operators.add(-1);
        //operators.add(4);
        //operators.add(2);
        //operators.add(1);

        //Mand obj = new Mand(2, 1, 500, 500, 2, 2, 0, 0, 500, operators);
        //obj.savePNG(obj.mapNoMirror(), System.getProperty("user.dir") + "/MyNewTest0.png");
        Mand obj = new Mand(2, 1, 500, 500, 4, 4, 0, 0, 500, operators);
        obj.savePNG(obj.mapNoIterations(), System.getProperty("user.dir") + "/MyNewTest.png");
        
        

        //obj.savePNG(obj.mapJulia(), System.getProperty("user.dir")+"/JialiaSet.png");
        //Mand2 obj=new Mand2(1000,1000,1,1,-0.5,0);
        //obj.savePNG(obj.map(2, 1), System.getProperty("user.dir")+"/ThisTest.png");
        //obj.savePNG(obj.mapBuddhaMirrorNotRand(),System.getProperty("user.dir")+"/MyNewTest1.png");
        /*
        for (int i = -6; i <= 6; i++) {
            operators = new ArrayList();
            operators.add(i);
            obj = new Mand(2, 1, 500, 500, 4, 4, 0, 0, 500, operators);
            obj.savePNG(obj.mapBuddha(), System.getProperty("user.dir") + "\\operators buddha\\MyNewTest" + i + ".png");
        }
         */
 /*
        for (int i = -6; i <= 6; i++) {
            for (int j = -6; j <= 6; j++) {
                operators = new ArrayList();
                operators.add(i);
                operators.add(j);
                obj = new Mand(2, 1, 500, 500, 4, 4, 0, 0, 500, operators);
                obj.savePNG(obj.mapBuddhaMirrorNotRand(), System.getProperty("user.dir") + "\\operators2 buddha\\MyNewTest" + i+" "+j + ".png");
            }
        }
         */
 
        /*for (int i = -6; i <= 5; i++) {
            for (int j = -6; j <= 6; j++) {
                for (int k = -6; k <= 6; k++) {
                    if(i==-j || j==-k){
                        continue;
                    }//3 1 1 is a problem 4 2 1, 5 2 1, 6 1 1
                    operators = new ArrayList();
                    operators.add(i);
                    operators.add(j);
                    operators.add(k);
                    obj = new Mand(2, 1, 300, 300, 4, 4, 0, 0, 100, operators);
                    obj.savePNG(obj.mapBuddhaMirrorNotRand(), System.getProperty("user.dir") + "\\operators3 buddha\\MyNewTest" + i + " " + j + " " + k + ".png");
                    System.out.println("Saved" + i + " " + j + " " + k);
                }
            }
        }*/
    }

}
