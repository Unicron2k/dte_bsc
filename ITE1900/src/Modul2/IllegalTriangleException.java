package Modul2;

public class IllegalTriangleException extends Exception {

    double side1, side2, side3;

    IllegalTriangleException(String errMessage, double side1, double side2, double side3){
        super(errMessage + " " + side1 + " " + side2 + " " + side3);
        this.side1=side1;
        this.side2=side2;
        this.side3=side3;
    }

    public double getSide1() { return side1; }
    public double getSide2() { return side2; }
    public double getSide3() { return side3; }
    public void setSide1(double side1) { this.side1 = side1; }
    public void setSide2(double side2) { this.side2 = side2; }
    public void setSide3(double side3) { this.side3 = side3; }
}