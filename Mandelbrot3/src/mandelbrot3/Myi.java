package mandelbrot3;

/**
 *
 * @author Chris
 */
public class Myi {
    private double r,theta;
    public static Myi ONE=new Myi(1,0,true);
    public static Myi I = new Myi(1,Math.PI/2,true);
    
    
    public Myi(double r, double theta, boolean polar) {
        if(polar){
            this.r = r;
            this.theta=theta;
        }else{
            this.r=Math.sqrt(r*r+theta*theta);
            this.theta=calcTheta(r,theta);
        }
        
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getTheta() {
        return theta;
    }
    
    public double calcTheta(double a, double b){
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
    
    public void setTheta(double theta) {
        this.theta = theta;
    }
    
    public double getRe(){
        return r*Math.cos(theta);
    }
    
    public double getIm(){
        return r*Math.sin(theta);
    }
    
    public double getPrinciple(double t){
        if(t>0){
            t = t%(Math.PI*2);
            if(t<Math.PI){
                return t;
            }else{
                return t-2*Math.PI;
            }
        }else{
            t = t%(Math.PI*2);
            if(t>-Math.PI){
                return t;
            }else{
                return t+2*Math.PI;
            }
        }
    }
    
    public double getPrincipleOld(double t){
        while(t>Math.PI){
            t=t-2*Math.PI;
        }
        while(t<=-Math.PI){
            t=t+2*Math.PI;
        }
        return t;
    }
    
    public Myi operator(int id){
        if(id == 0){
            return minverse();
        }else if(id == 1){
            return exp();
        }else if(id == -1){
            return ln();
        }else if(id == 2){
            return sin();
        }else if(id == -2){
            return asin();
        }else if(id == 3){
            return cos();
        }else if(id == -3){
            return acos();
        }else if(id == 4){
            return tan();
        }else if(id == -4){
            return atan();
        }else{
            if(id>=5){
                return power(id-3);
            }else{
                return power(1, Math.abs(id)-3);
            }
        }
    }
    
    public Myi add(double x2, double y2){
        double x=r*Math.cos(theta)+x2;
        double y=r*Math.sin(theta)+y2;
        return new Myi(Math.sqrt(x*x+y*y), calcTheta(x,y), true);
    }
    
    public Myi add(Myi num){
        double x=r*Math.cos(theta)+num.getRe();
        double y=r*Math.sin(theta)+num.getIm();
        return new Myi(Math.sqrt(x*x+y*y), calcTheta(x,y), true);
    }
    
    public Myi power(int numer, int denom){
        double the=getPrinciple(theta*numer/denom);//+(4*Math.PI*numer/denom);
        return new Myi(Math.pow(r, 1.0*numer/denom), the, true);
    }
    
    public Myi power(int numer){
        return power(numer, 1);
    }
    
    public Myi multiply(double num){
        return new Myi(r*num,theta,true);
    }
    
    public Myi multiply(Myi num){
        double t = getPrinciple(theta+num.getTheta());
        return new Myi(r*num.getR(),t, true);
    }
    
    public Myi minverse(){
        return new Myi(1/r, -theta, true);
    }
    
    public Myi ainverse(){
        double t = theta+Math.PI;
        while(t>=Math.PI){
            t=t-2*Math.PI;
        }
        while(t<=-Math.PI){
            t=t+2*Math.PI;
        }
        return new Myi(r, t, true);
    }
    
    public Myi conjugate(){
        return new Myi(r,-theta,true);
    }
    
    public Myi divide(Myi num){
        double t = getPrinciple(theta-num.getTheta());
        return new Myi(r/num.getR(), t, true);
    }
    
    public String toString(){
        return "Polar: ("+r+", "+theta+")\tRectangular: ("+(r*Math.cos(theta))+", "+(r*Math.sin(theta))+")";
    }
    
    // The principle complex ln function
    public Myi ln(){
        double x = getRe();
        double y = getIm();
        return new Myi(Math.log(getR()), getTheta(), false);
        //return new Myi(Math.log(x*x + y*y)/2, Math.asin(y/Math.sqrt(x*x + y*y)), false);
    }
    
    public Myi atanOff(){
        double x = getRe();
        double y = getIm();
        double a = (1 - x*x - y*y)/(y*y + (1-x)*(1-x));
        double b = (2*y)/(y*y + (1-x)*(1-x));
        Myi z = new Myi(a, b, false);
        z.divide(Myi.I);
        z = z.ln().multiply(0.5).multiply(Myi.I);
        
        return z;
    }
    
    public Myi atan(){
        double x = getRe();
        double y = getIm();
        double a = (1 - x*x - y*y)/(x*x + (y+1)*(y+1));
        double b = (2*x)/(x*x + (1+y)*(1+y));
        Myi z = new Myi(a, b, false);
        z = z.ln().multiply(0.5).divide(Myi.I);
        
        return z;
    }
    
    public Myi acot(){
        double x = getRe();
        double y = getIm();
        double a = (-1 + x*x + y*y)/(x*x + (y-1)*(y-1));
        double b = (2*x)/(x*x + (y-1)*(y-1));
        Myi z = new Myi(a, b, false);
        z = z.ln().multiply(0.5);
        
        return new Myi(z.getIm(), -z.getRe(), false);
    }
    
    public Myi asin(){
        Myi w = power(2).ainverse().add(1, 0);
        w = w.power(1,2);
        Myi y = w.add(multiply(I));
        return y.ln().multiply(new Myi(1, -Math.PI/2, true));
    }
    
    public Myi acos(){
        Myi w = power(2).ainverse().add(1, 0);
        w = w.power(1,2);
        Myi y = w.multiply(new Myi(1, Math.PI/2, true)).add(getRe(), getIm());
        return y.ln().multiply(new Myi(1, -Math.PI/2, true));
    }
    
    public Myi exp(){
        double t = getPrinciple(getIm());
        return new Myi(Math.exp(getRe()), t, true);
    }
    
    public Myi tan(){
        double x = getRe();
        double y = getIm();
        Myi numer = new Myi(Math.tan(x),Math.tanh(y),false);
        Myi denom = new Myi(1, -Math.tan(x)*Math.tanh(y),false);
        return numer.divide(denom);
    }
    
    public Myi sin(){
        double x = getRe();
        double y = getIm();
        return new Myi(Math.sin(x)*Math.cosh(-y), -Math.cos(x)*Math.sinh(-y),false);
    }
    
    public Myi cos(){
        double x = getRe();
        double y = getIm();
        return new Myi(Math.cos(x)*Math.cosh(-y), Math.sin(x)*Math.sinh(-y),false);
    }
    
}
