package Modul3.GeometricObject;

public class Triangle extends GeometricObject {

    private double side1, side2, side3;

    public Triangle() /*throws IllegalTriangleException*/{
        this(1);
    }
    public Triangle(double side) /*throws IllegalTriangleException*/{
        this(side,side,side);
    }
    public Triangle(double side1, double side2, double side3) /*throws IllegalTriangleException*/{
        this(side1, side2, side3, "white", false);
    }
    public Triangle(double side1, double side2, double side3, String color, boolean filled) /*throws IllegalTriangleException*/{
        super(color, filled);
        this.side1=side1;
        this.side2=side2;
        this.side3=side3;

        /*
        if(side1<=0 || side2<=0 || side3<=0){
            throw new IllegalTriangleException("Side less than 0:", getSide1(), getSide2(), getSide3());
        }
        if(side1>=side2+side3 || side2>=side1+side3 || side3>=side2+side1){
            throw new IllegalTriangleException("Invalid length of side:", getSide1(), getSide2(), getSide3());
        }
        */
    }

    public void setSide1(double side){side1=side;}
    public void setSide2(double side){side2=side;}
    public void setSide3(double side){side3=side;}

    public double getSide1(){return side1;}
    public double getSide2(){return side2;}
    public double getSide3(){return side3;}

    public double getArea(){
        double s =(side1+side2+side3)/2.0;
        return Math.sqrt(s*(s-side1)*(s-side2)*(s-side3));
    }
    public double getPerimeter(){return side1+side2+side3;}

    @Override
    public String toString(){
        return String.format("Triangle%nArea: %.2f%nPerimeter: %.2f%n%nSides: %.2f, %.2f, %.2f%n%s",this.getArea(), this.getPerimeter(), this.getSide1(), this.getSide2(), this.getSide3(), super.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Triangle) {
            return(
                (this.side1 == ((Triangle)obj).side1) &&
                (this.side2 == ((Triangle)obj).side2) &&
                (this.side3 == ((Triangle)obj).side3) &&
                (this.getColor().equals(((Triangle)obj).getColor())) &&
                (this.isFilled() == ((Triangle)obj).isFilled())
            );
        }
        else return false;
    }
}