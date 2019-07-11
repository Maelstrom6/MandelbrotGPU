package mandelbrot3;

/**
 *
 * @author Chris
 */
public class Mandelbrot3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MandQuar obj=new MandQuar(2,1,500,500,2,2,0,0);
        
        
        //obj.savePNG(obj.mapJulia(), System.getProperty("user.dir")+"/JialiaSet.png");
        //Mand2 obj=new Mand2(1000,1000,1,1,-0.5,0);
        //obj.savePNG(obj.map(2, 1), System.getProperty("user.dir")+"/ThisTest.png");
        obj.savePNG(obj.mapNoMirror(), System.getProperty("user.dir")+"/MyNewTestQuar.png");
        // TODO code application logic here
    }
    
}
