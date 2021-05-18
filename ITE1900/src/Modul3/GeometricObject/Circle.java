package Modul3.GeometricObject;

public class Circle extends GeometricObject {

    private double radius;

    public Circle(){this(1);}
    public Circle(double radius){this(radius, "white", false);}

    public Circle(double radius, String color, boolean filled){
        super(color, filled);
        this.radius=radius;
    }

    public void setRadius(double radius){this.radius=radius;}
    public double getRadius(){return radius;}
    public double getArea(){return Math.PI*radius*radius; }
    public double getPerimeter(){return Math.PI*2*radius;}

    @Override
    public String toString(){
        return String.format("Circle%nArea: %.2f%nPerimeter: %.2f%n%nRadius: %.2f%n%s",this.getArea(), this.getPerimeter(), this.getRadius(), super.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Circle) {
            return(
                (this.radius == ((Circle)obj).radius) &&
                (this.getColor().equals(((Circle)obj).getColor())) &&
                (this.isFilled() == ((Circle)obj).isFilled())
            );
        }
        else return false;
    }
}