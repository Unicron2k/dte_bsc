package Modul3.Vehicle;

/*
 * TestVehicles oppretter Bicycle og Car objekter, legger disse i et ArrayList
 * Lar bruker manipulere data i arrayet på forskjellige måter
 */

import java.util.*;
import java.io.*;

public class VehicleTest {
    public static void main(String[] args) {

        VehicleTest vtest = new VehicleTest();
        try {
            vtest.menuLoop();
        } catch(IOException e) {
            System.out.println("IO Exception!");
            System.exit(1);
        } catch(CloneNotSupportedException e) {
            System.out.println("CloneNotSupportedException");
            System.exit(1);
        }
    }

    private void menuLoop() throws IOException, CloneNotSupportedException {
        Scanner scan = new Scanner(System.in);
        ArrayList<Vehicle> arr=new ArrayList<Vehicle>();
        Vehicle vehicle;//?
        String searchString, direction;
        File file = new File("vehicles.csv");

        try (Scanner in = new Scanner(file).useLocale(Locale.US)){
            in.useDelimiter(",|\n");
            String vClass;

            while(in.hasNext()){
                vClass = in.next();
                Class veh1 = Class.forName(vClass);

                //deprecated, should apparently use getConstructor().newInstance()
                Vehicle v = (Vehicle)veh1.newInstance();
                v.readData(in);
                arr.add(v);
                System.out.printf("Vehicle read from file: %s%n", v);
            }
        }
        catch(FileNotFoundException ex){System.out.printf("Could not read from file: %s%n", ex.getMessage());}
        catch(ClassNotFoundException ex){System.out.printf("Could not find class: %s%n", ex.getMessage());}
        catch(IllegalAccessException ex){System.out.printf("Could not access element: %s%n", ex.getMessage());}
        catch(InstantiationException ex){System.out.printf("Could instantiate object: %s%n", ex.getMessage());}


        arr.add(new Car("Volvo","Black",85000,2010,"1010-11",163,0));
        arr.add(new Bicycle("Diamant","yellow",4000,1993,"BC100",10,0));
        arr.add(new Car("Ferrari Testarossa","red",1200000,1996,"A112",350,0));
        arr.add(new Bicycle("DBS","pink",5000,1994,"42",10,0));

        while(true) {
            System.out.println("1...................................New car");
            System.out.println("2...............................New bicycle");
            System.out.println("3......................Find vehicle by name");
            System.out.println("4..............Show data about all vehicles");
            System.out.println("5.......Change direction of a given vehicle");
            System.out.println("6.........................Test clone method");
            System.out.println("7..................Test driveable interface");
            System.out.println("8..............................Exit program");
            System.out.print(".............................Your choice? ");
            int choice = scan.nextInt();
            //Discards leftover carriage return from nextInt(). Otherwise, we will read the leftover "%n" and put it into the next scan...
            scan.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    arr.add(new Car());
                    System.out.println("Input car data: ");
                    arr.get(arr.size()-1).setAllFields();
                    break;
                case 2:
                    arr.add(new Bicycle());
                    System.out.println("Input bicycle data:");
                    arr.get(arr.size()-1).setAllFields();
                    break;
                case 3:
                    System.out.print("Name of vehicle: ");
                    searchString = scan.nextLine().toLowerCase();
                    for(Vehicle v : arr){
                        if (searchString.equals(v.getName().toLowerCase())) {
                            System.out.println(v.toString());
                        }
                    }
                    break;
                case 4:
                    for(Vehicle v : arr) {
                            System.out.println(v.toString());
                    }
                    break;
                case 5:
                    System.out.print("Name of vehicle: ");
                    searchString = scan.nextLine().toLowerCase();
                    for(Vehicle v : arr){
                        if (searchString.equals(v.getName().toLowerCase())) {

                            System.out.print("Direction[R/L]: ");
                            direction=scan.nextLine();

                            System.out.print("Degree[0/360]: ");
                            if(direction.equals("L") || direction.equals("l"))
                                v.turnLeft(scan.nextInt());

                            if(direction.equals("R") || direction.equals("r"))
                                v.turnRight(scan.nextInt());
                        }
                    }
                    break;
                case 6:
                    testCloneable();
                    break;
                case 7:
                    testDriveableCar();
                    testDriveableBicycle();
                    break;
                case 8:
                    scan.close();
                    try(PrintWriter out = new PrintWriter(file)){
                        for(Vehicle v : arr){
                            v.writeData(out);
                            System.out.printf("Vehicle written to file: %s%n", v);
                        }
                    }
                    catch(FileNotFoundException ex){
                        System.out.printf("Could not open file: %s%n", ex.getMessage());
                    }
                    System.exit(0);
                    break;
                default:
                    System.out.println("Wrong input!");
            }
            System.out.println();
        }
    }

    public void testCloneable() throws CloneNotSupportedException{
        System.out.println("Date-objects are separate. Deep copy.");

        Car c1 = new Car("Volkswagen", "Grey", 89000, 2019, "WS0010-10",220, 0);
        c1.setProductionDate(new GregorianCalendar(2018, Calendar.NOVEMBER, 27));

        Car c2 = (Car)c1.clone();
        c2.setProductionDate(new GregorianCalendar(2019, Calendar.FEBRUARY, 8));

        System.out.printf("Old production date: %s%nNew production date: %s%n", c1.getProductionDate().getTime(), c2.getProductionDate().getTime());
        //System.out.printf("Old car: %s%nNew car: %s%n", c1.toString(), c2.toString());
    }

    public void testDriveableCar(){
        Car c = new Car("BMW 750CSL", "Black", 149000, 1998, "WBABE2172398CE76297", 482, 0);

        System.out.println("Car:");

        c.accelerate(10);
        System.out.printf("Car accelerates to: %.2f Km/h%n", c.getSpeed());

        c.accelerate(100);
        System.out.printf("Car accelerates to: %.2f Km/h%n", c.getSpeed());

        c.brakes(1000);
        System.out.printf("Car slows down to: %.2f Km/h%n", c.getSpeed());

        c.stop();
    }

    public void testDriveableBicycle(){
        Bicycle b = new Bicycle("Diamant 1420", "White", 4500, 2019, "DD34-DOL-F", 21, 0);

        System.out.println("Bicycle:");

        b.accelerate(10);
        System.out.printf("Bicycle accelerates to: %.2f Km/h%n", b.getSpeed());

        b.accelerate(100);
        System.out.printf("Bicycle accelerates to: %.2f Km/h%n", b.getSpeed());

        b.brakes(1000);
        System.out.printf("Bicycle slows down to: %.2f Km/h%n", b.getSpeed());

        b.stop();
    }
}

