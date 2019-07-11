package mandelbrot3;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class Sequence {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList nums=new ArrayList<Integer>();
        nums.add(0);
        nums.add(1);
        int tot=0;
        for(int i=2;i<20;i++){
            tot=0;
            for(int a=1;a<i;a++){
                tot=tot+Integer.parseInt(nums.get(a).toString())*Integer.parseInt(nums.get(i-a).toString());
            }
            nums.add(tot);
        }
        
        System.out.println(nums.toString());
        // TODO code application logic here
    }
    
}
