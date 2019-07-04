/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author Chris
 */
public class MandelbrotGif {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String workingDir = System.getProperty("user.dir");
        Mand obj=new Mand(2,1,100,100, 1.6,1.6);
        int msBetweenFrames = 1000 / 30;//1000/30
        
        try{
            ImageOutputStream output= new FileImageOutputStream(new File("MandelJuliaTest.gif"));
            GifSequenceWriter writer = new GifSequenceWriter(output, 1, msBetweenFrames, true);//1 is firstImage.getType();
            for(int k=-50;k<570;k++){
                /////////////Julia movement//////////////////
                obj=new Mand(2,1,100,100,1.6,1.6,-k/250.0,0);
                
                /////////////ZOOM to whatever//////////////////
                //obj=new Mand3(2,1,100,100,Math.pow(10, -k/50.0),Math.pow(10, -k/50.0),0.38943817299905936,0.6080505280559806,100+k,4);//0.395995455,0.605005292
                
                BufferedImage nextImage = obj.mapJulia();
                //BufferedImage nextImage = obj.map();
                writer.writeToSequence(nextImage);
            }
            for(int i=0;i<60;i++){
                BufferedImage nextImage=obj.getMap();
                writer.writeToSequence(nextImage);
            }
            writer.close();
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(MandelbrotGif.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO code application logic here
        // TODO code application logic here
        // TODO code application logic here
        // TODO code application logic here
    }
    
}
