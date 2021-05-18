package Modul2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WriteEmployeeList {
    public static void main(String[] args){

        System.out.println("Writing 'employee_list.txt'");
        try(PrintWriter output = new PrintWriter("employee_list.txt")){
            output.println(String.format("%-20s%-19s%-13s%10s", "First Name:", "Last Name:", "Rank:", "Salary:"));
            String rank="None";
            double sal=0;
            for(int i=1; i<=1001; i++){

                switch ((int)(Math.random()*3)){
                    case 0:
                        sal=50000+Math.random()*30000;
                        rank="Assistant";
                        break;
                    case 1:
                        sal=60000+Math.random()*40000;
                        rank="Associate";
                        break;
                    case 2:
                        sal=75000+Math.random()*55000;
                        rank="Assistant";
                        break;
                }
                output.println(String.format("%9s%04d%5s %9s%04d%5s %9s%2s %12.2f", "FirstName", i, " ", "LastName", i, " ", rank, " ", sal));
            }
            System.out.println("Done!");
        }
        catch(FileNotFoundException ex){
            System.out.printf("Unable to open file: %s", ex.getMessage());
        }


    }
}


/*
Du skal skrive en program som oppretter en fil med 1000 linjer, den skal representere en ansattslisten for en stor firma.

Programmet skal:

    Opprette en fil kalt EmployeeList.txt og skrive til den ved hjelp av PrintWriter (bruk try-with-resources).
    Generer info stringer for 1000 ansatte og skriv dem til fil.
    Hver string skal ha:
        Fornavnet på form: FirstName0001, FirstName0002, ...
        Etternavnet på form: LastName0001, LastName0002, ...
        Stilling som skal være tilfeldig. Det er tre stillinger: Assistant, Associate og Manager.
        Lønn som skal være tilfeldig, men:
            Assistant skal tjene mellom 50 og 80 tusen dollar.
            Associate skal tjene mellom 60 og 100 tusen dollar.
            Manager skal tjene mellom 75 og 130 tusen dollar.
 */