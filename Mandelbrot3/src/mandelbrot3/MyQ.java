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
public class MyQ {
    private double r,i,j,k;

    public MyQ(double r, double i, double j, double k) {
        this.r = r;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }

    public double getJ() {
        return j;
    }

    public void setJ(double j) {
        this.j = j;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }
    
    public MyQ add(MyQ num){
        double r,i,j,k;
        r = this.r+num.getR();
        i = this.i+num.getI();
        j = this.j+num.getJ();
        k = this.k+num.getK();
        return new MyQ(r, i, j, k);
    }
    
    // multiples the num from the right. ie. this*num
//    public MyQ multiply(MyQ num){
//        double r,i,j,k;
//        r = this.r*num.getR() - this.i*num.getI() - this.j*num.getJ() - this.k*num.getK();
//        i = this.r*num.getI() + this.i*num.getR() + this.j*
//        return new MyQ(r, i, j, k);
//    }
//    
    public MyQ square(){
        //return new MyQ(r*r-i*i-j*j-k*k, 2*r*i, 2*r*j, 2*r*k);
        return new MyQ(r*r-i*i-j*j-k*k, 2*j*k*r, 2*k*i*r, 2*i*j*r);
    }
    
    public double norm(){
        return Math.sqrt(this.r*this.r + this.i*this.i + this.j*this.j + this.k*this.k);
    }
}
