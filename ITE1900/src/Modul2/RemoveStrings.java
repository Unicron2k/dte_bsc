package Modul2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class RemoveStrings {
    public static void main(String[] args){

        if(args.length!=2){
            System.out.printf("Invalid number of arguments!%nUsage: java RemoveStrings \'stringToBeRemoved\' sourceFile");
            System.exit(1);
        }

        File path = new File(args[1]);

        //Probably not a good method for handling large files...
        ArrayList<String> output = new ArrayList<>();
        Scanner fileIn = null;
        PrintWriter fileOut = null;

        try{
            //Open, read and process data
            fileIn = new Scanner(path);
            System.out.println("Reading and removing string from file...");
            while (fileIn.hasNext()){
                output.add(fileIn.nextLine().replace(args[0], ""));
            }
            System.out.println("Done!");

            //Put the data back...
            fileOut = new PrintWriter(path);
            System.out.println("Writing back to file...");
            for(String list : output){
                fileOut.println(list);
            }
            System.out.println("Done!");
        }
        catch (FileNotFoundException ex){
            System.out.printf("Unable to open file: %s%n", ex.getMessage());
            System.exit(2);
        }
        finally{
            if(fileIn!=null){
                fileIn.close();
            }
            if(fileOut!=null) {
                fileOut.close();
            }
        }
    }
}