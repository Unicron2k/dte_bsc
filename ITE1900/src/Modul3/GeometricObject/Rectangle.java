package Modul3.GeometricObject;

public class Rectangle extends GeometricObject {

    private double width, height;

    public Rectangle(){
        this(1);
    }
    public Rectangle(double sides){
        this(sides,sides);
    }
    public Rectangle(double width, double height){
        this(width, height, "white", false);
    }
    public Rectangle(double width, double height, String color, boolean filled){
        super(color, filled);
        this.width=width;
        this.height=height;
    }

    public void setWidth(double width){this.width=width;}
    public void setHeight(double height){this.height=height;}

    public double getWidth(){return width;}
    public double getHeight(){return height;}

    public double getArea(){return width*height;}
    public double getPerimeter(){return (width+height)*2;}

    @Override
    public String toString(){
        return String.format("Rectangle%nArea: %.2f%nPerimeter: %.2f%n%nWidth: %.2f%nHeight: %.2f%n%s",this.getArea(), this.getPerimeter(), this.getWidth(), this.getHeight(), super.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            return(
                (this.width == ((Rectangle)obj).width) &&
                (this.height == ((Rectangle)obj).height) &&
                (this.getColor().equals(((Rectangle)obj).getColor())) &&
                (this.isFilled() == ((Rectangle)obj).isFilled())
            );
        }
        else return false;
    }
}