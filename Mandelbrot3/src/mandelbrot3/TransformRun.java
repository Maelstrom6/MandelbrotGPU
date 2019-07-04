/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot3;

/**
 *
 * @author Chris
 */
public class TransformRun {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Get image, centre of image is centre
        //define coordinate limits
        //define transform
        //set each new pixel to the transformed pixel
        
        TransformImage obj=new TransformImage(500,500);
        
        obj.read(System.getProperty("user.dir")+"/firstbuddha.png");
        obj.savePNG(obj.doThings(), System.getProperty("user.dir")+"/PNGTransform.png");
        // TODO code application logic here
    }
    
}
