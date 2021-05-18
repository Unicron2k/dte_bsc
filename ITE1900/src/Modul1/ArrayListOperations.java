package Modul1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class ArrayListOperations {
    public static void main(String[] args) {
        ArrayListWrapper arr = new ArrayListWrapper();

        System.out.println("Contents of list:");
        arr.printList();
        System.out.println();

        System.out.println("Adding number...");
        arr.addNum(42);
        System.out.println("Removing number");
        arr.removeNum(3);

        System.out.printf("%nDoes list contain 42? %s%n", arr.containsNum(42)?"Yes.":"No.");
        System.out.printf("Does list contain 3? %s%n", arr.containsNum(3)?"Yes.":"No.");

        System.out.printf("%nNew contents of list:%n");
        arr.printList();

        System.out.printf("%nShuffled list:%n");
        arr.shuffleList();
        arr.printList();

        System.out.printf("%nSorted list, descending:%n");
        arr.sortListDescending();
        arr.printList();
    }
}

class ArrayListWrapper{

    private final int arrLength = 10;
    private Integer[] arr = new Integer[arrLength];
    private ArrayList<Integer> arrList;

    ArrayListWrapper() {
        for (int i = 0; i < arrLength; i++) {
            arr[i] = i + 1;
        }
        arrList = new ArrayList<>(Arrays.asList(arr));
    }

    public void printList() {
        for (int value : arrList) {
            System.out.printf("%d ", value);
        }
        System.out.println();
    }

    public void addNum(int number) {
        arrList.add(number);
    }

    public void removeNum(int number){
        //tving remove() til å fjerne et "objekt" med verdien til randomInt
        //mulignes litt dirty, men funker...
        arrList.remove((Integer) number);
        //alternativt:
        //int removed = arrList.get(randomInt);
        //arrList.remove(randomInt);
    }

    public boolean containsNum(int number) {
        return arrList.contains(number);
    }

    public void sortListDescending() {
         Collections.sort(arrList);
         Collections.reverse(arrList);
     }

    public void shuffleList() {
        Integer temp;
        int randomPos;
        for (int i = 0; i < arrList.size(); i++) {
            randomPos = (int) (Math.random() * 10);
            temp = arrList.get(randomPos);
            arrList.set(randomPos, arrList.get(i));
            arrList.set(i, temp);
        }
    }
}