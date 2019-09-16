package mandelbrot3;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class Test {

    private static int maxI = 20;

    public static void main(String[] args) {
//        Myi z = new Myi(-0.1169, 0.75825, false);
//        System.out.println(fn(1000, z));
//        System.out.println(fMinus(z));
//        System.out.println(fPlus(z));
//        
//        System.out.println(partOfNoIterations(z));
//        for(double x=-2;x<2;x+=0.5){
//            for(double y=-2;y<2;y+=0.5){
//                z = new Myi(x, y, false);
//                System.out.println("");
//                System.out.println(z.getR()<2);
//                System.out.println(z=fPlus(z).multiply(fMinus(z)));
//                System.out.println(fn(1000, z));
//            }
//        }

        Myi i = new Myi(0.0148954947042096, -0.84814878, false);
        System.out.println((i.atan()));
        
        
        //-0.9548, 0.01
        //Polar: (0.04858894694029913, -2.9122675954400097)	Rectangular: (-0.04731689048570509, -0.011045254163259237)
        //Polar: (0.9527471358095793, 3.1299993344655745)	Rectangular: (-0.9526831095142948, 0.011045254163259407)
        
        //-0.1169, 0.75825
        //Polar: (0.8918343008704657, 2.433638860196384)	Rectangular: (-0.6775212333728137, 0.5799425821045507)
        //Polar: (0.028200065476293872, -1.3635858635912836)	Rectangular: (0.005801623132925245, -0.027596827025779036)
        
        //ArrayList<Integer> operators = new ArrayList();
        //Mand obj = new Mand(2, 1, 500, 500, 2, 2, 0, 0, 500, operators);
        //obj.savePNG(obj.mapOrbit(), System.getProperty("user.dir") + "/MyNewTest1.png");
    }

    public static Myi fPlus(Myi z){
        // returns (1 + sqrt(1-4z))/2
         z = z.multiply(4);
         z = z.ainverse();
         z = z.add(Myi.ONE);
         z = z.power(1, 2);
         z = z.add(Myi.ONE);
         z = z.multiply(0.5);
         return z;
    }
    
    public static Myi fMinus(Myi z){
        // returns (1 - sqrt(1-4z))/2
         z = z.multiply(4);
         z = z.ainverse();
         z = z.add(Myi.ONE);
         z = z.power(1, 2);
         
         z = z.ainverse();
         z = z.add(Myi.ONE);
         z = z.multiply(0.5);
         return z;
    }
    
    public static Myi fMinus2(Myi z){
        double x = z.getRe();
        double y = z.getIm();
        
        x = -4*x+1;
        y = -4*y;
        
        
        //double the = calcTheta(x, y);
        double the = Math.atan(y/x);
        
        double r = Math.sqrt(x*x+y*y);
        the = the/2+Math.PI; // This is the different part
        r = Math.sqrt(r);
        
        x = (r*Math.cos(the)+1)/2;
        y = r*Math.sin(the)/2;
        
        return new Myi(x, y, false);
    }
    
    public static Myi partOfNoIterations(Myi z){
        Myi fp = fPlus(z);
        Myi fm = fMinus(z);
        //Now check if fp is a valid point
        // Check if the point satisfies mand escape condition
        /**if (fm.getR() < 2){
            return maxI;
        }else{
            return 1;
        }
        */
        // Find the average Im(z) is always zero
        Myi f = fp.add(fm).multiply(0.5);
        //Myi f = fp.multiply(fm);
        if (f.getR() < 2){
            return f;
        }else{
            return new Myi(0,0,true);
        }
        // Check if the converted point is part of the mandelbrot set
        //return PartOfMand(fm.getRe(), fm.getIm());
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
    
    public static double calcTheta(double a, double b){
        double the=0;
        if(a==0 && b==0){
            the=0;
        }else if(a==0){
            the=Math.PI/2*(Math.abs(b)/b);
        }else if(b==0){
            if(a<0){
                the=Math.PI;
            }else{
                the=0;
            }
        }else if(a>0){
            the=Math.atan(b/a);
        }else if(a<0){
            if(b>0){
                the=Math.atan(b/a)+Math.PI;
            }else{
                the=Math.atan(b/a)-Math.PI;
            }
        }
        return the;
    }

}
