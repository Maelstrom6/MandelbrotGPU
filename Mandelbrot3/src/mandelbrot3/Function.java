package mandelbrot3;

import java.math.BigInteger;

/**
 *
 * @author Chris
 */
public class Function {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Must return coefficients of fn(z)
        int n = 6;
        int maxSize = (int)(Math.pow(2, n+1))+1;
        double coef[] = new double[maxSize];
        coef[0] = 0;
        coef[1] = 1;
        coef[2] = 1;

        double count;
        double newCoef[] = new double[maxSize];
        for (int a = 0; a < n; a++) {
            newCoef = new double[maxSize];
            for (int b = 0; b < maxSize; b++) {
                count = 0;
                for (int c = 1; c < b; c++) {
                    count = count + coef[c] * coef[b - c];
                }
                newCoef[b] = count;
            }
            coef = newCoef;
            coef[1]=1;
        }
        
        for(int i=0;i<maxSize;i++){
            System.out.print(coef[i]+" ");
        }
        System.out.println("");
        
        // TODO code application logic here
    }

    static BigInteger choose(final int N, final int K) {
        BigInteger ret = BigInteger.ONE;
        for (int k = 0; k < K; k++) {
            ret = ret.multiply(BigInteger.valueOf(N - k))
                    .divide(BigInteger.valueOf(k + 1));
        }
        return ret;
    }

}
