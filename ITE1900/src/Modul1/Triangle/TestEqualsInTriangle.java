package Modul1.Triangle;

public class TestEqualsInTriangle {
    public static void main(String[] args) {
        Triangle t1 = new Triangle(1);
        Triangle t2 = new Triangle(2);
        Triangle t3 = new Triangle(1,1,1);
        System.out.println("t1 == t1 gir " + (t1 == t1)); // 1
        System.out.println("t1 == t2 gir " + (t1 == t2)); // 2
        System.out.println("t1 == t3 gir " + (t1 == t3)); // 3

        System.out.println("t1.equals(t1) gir " + t1.equals(t1)); // 4
        System.out.println("t1.equals(t2) gir " + t1.equals(t2)); // 5
        System.out.println("t1.equals(t3) gir " + t1.equals(t3)); // 6
    }
}