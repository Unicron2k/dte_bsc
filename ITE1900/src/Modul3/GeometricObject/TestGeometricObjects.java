package Modul3.GeometricObject;

import java.util.ArrayList;
import java.util.Collections;

public class TestGeometricObjects {
    public static void main(String[] args){

        ArrayList<GeometricObject> geoObj = new ArrayList<>();

        GeometricObject tri1 = new Triangle(2,3,4);
        GeometricObject tri2 = new Triangle(4,5,6);

        GeometricObject cir1 = new Circle(3);
        GeometricObject cir2 = new Circle(4);

        GeometricObject rec1 = new Rectangle(3, 5);
        GeometricObject rec2 = new Rectangle(4, 8);

        geoObj.add(GeometricObject.max(tri1, tri2));
        geoObj.add(GeometricObject.max(cir1, cir2));
        geoObj.add(GeometricObject.max(rec1, rec2));



        System.out.printf("The biggest Triangle:%n%s%n%n", geoObj.get(0).toString());
        System.out.printf("The biggest Circle:%n%s%n%n", geoObj.get(1).toString());
        System.out.printf("The biggest Rectangle:%n%s%n%n", geoObj.get(2).toString());

        Collections.sort(geoObj);

        System.out.printf("The biggest geometric object is: %s%n", geoObj.get(geoObj.size()-1).toString());
    }
}