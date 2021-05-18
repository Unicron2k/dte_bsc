package Modul1.Triangle;

import java.util.Scanner;

public class TestTriangle {
    public static void main(String[] args){

        Scanner keyboard = new Scanner(System.in);

        double side1, side2, side3;

        System.out.print("Side1: ");
        side1=keyboard.nextDouble();

        System.out.print("Side2: ");
        side2=keyboard.nextDouble();

        System.out.print("Side3: ");
        side3=keyboard.nextDouble();

        Triangle tris = new Triangle(side1, side2, side3, "Orange", true);

        System.out.printf("%n%s", tris.toString());
    }
}