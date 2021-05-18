package Modul2;

import java.util.Scanner;

public class TestArrayBounds {
    public static void main(String[] args){
        int[] arr = new int[100];
        Scanner keyboard = new Scanner(System.in);

        for (int i=0; i<100; i++){
            arr[i]=(int)(Math.random()*10000);
        }
        System.out.print("Enter an index: ");

        try{
            System.out.printf("The element is: %d", arr[keyboard.nextInt()]);
        }
        catch(ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: Index out of bounds!");
        }
    }
}