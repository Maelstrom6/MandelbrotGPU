package mandelbrot3;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class Test {

    private static int maxI = 20;

    public static void main(String[] args) {
        Myi z = new Myi(-0.1169, 0.75825, false);
        System.out.println(fn(1002, z));
        System.out.println(fMinus(z));
        System.out.println(fPlus(z));
        //-0.9548, 0.01
        //Polar: (0.04858894694029913, -2.9122675954400097)	Rectangular: (-0.04731689048570509, -0.011045254163259237)
        //Polar: (0.9527471358095793, 3.1299993344655745)	Rectangular: (-0.9526831095142948, 0.011045254163259407)
        
        //-0.1169, 0.75825
        //Polar: (0.8918343008704657, 2.433638860196384)	Rectangular: (-0.6775212333728137, 0.5799425821045507)
        //Polar: (0.028200065476293872, -1.3635858635912836)	Rectangular: (0.005801623132925245, -0.027596827025779036)
        ArrayList<Integer> operators = new ArrayList();
        Mand obj = new Mand(2, 1, 500, 500, 2, 2, 0, 0, 500, operators);
        obj.savePNG(obj.mapOrbit(), System.getProperty("user.dir") + "/MyNewTest1.png");
    }

    public static Myi fMinus(Myi z){
        //(1 + sqrt(1 - 4*z)) / 2
        Myi result = z.multiply(4);
        result = result.ainverse();
        result = result.add(1,0);
        result = result.power(1,2);
        result = result.ainverse(); // + -
        result = result.add(1,0);
        result = result.multiply(0.5);
        return result;
    }
    
    public static Myi fPlus(Myi z){
        //(1 + sqrt(1 - 4*z)) / 2
        Myi result = z.multiply(4);
        result = result.ainverse();
        result = result.add(1,0);
        result = result.power(1,2);
        result = result.add(1,0);
        result = result.multiply(0.5);
        return result;
    }
    
    public static Myi fn(int n, Myi z){
        Myi result = z;
        for(int i=0;i<n;i++){
            result = result.power(2).add(z);
            if(result.getR()>2){
                result = new Myi(0,0,true);
                break;
            }
        }
        return result;
    }

}
