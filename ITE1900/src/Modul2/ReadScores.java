package Modul2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadScores {
    public static void main(String[] args){

        double score=0, numScores=0;

        System.out.print("Enter file name: ");
        try(Scanner keyboard = new Scanner(System.in); Scanner input = new Scanner(new File(keyboard.nextLine()))){
            while(input.hasNext()) {
                score += Double.parseDouble(input.next());
                numScores++;
            }
        }
        catch(FileNotFoundException ex){
            System.out.printf("Unable to open file: %s%n", ex.getMessage());
            System.exit(1);
        }
        catch(NumberFormatException ex){
            System.out.printf("%s: Not a valid score!%n", ex.getMessage());
            System.exit(2);
        }
        System.out.printf("Total is: %.2f%nAverage is: %.2f%n", score, score/numScores);
    }
}
