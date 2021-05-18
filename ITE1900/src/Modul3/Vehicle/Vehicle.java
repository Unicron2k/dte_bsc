package Modul3.Vehicle;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public abstract class Vehicle implements Comparable<Vehicle>, Cloneable, Driveable, Fileable{

    private String colour, name, serialNumber;
    private int modelYear, price, direction;
    private double speed;
    private Calendar buyingDate;

    protected java.util.Scanner input = new Scanner(System.in);

    Vehicle(){}


    Vehicle(String name, String colour, int price, int modelYear, String serialNumber, int direction){
        this.colour=colour;
        this.name=name;
        this.serialNumber=serialNumber;
        this.modelYear=modelYear;
        this.price=price;
        this.direction=direction;
        this.speed=0;
        this.buyingDate = new GregorianCalendar();
    }

    public void setAllFields(){
        System.out.print("Name: ");
        this.name=input.nextLine();

        System.out.print("Colour: ");
        this.colour=input.nextLine();

        System.out.print("Price: ");
        this.price=input.nextInt();

        System.out.print("Model: ");
        this.modelYear=input.nextInt();
        //Discards leftover carriage return from nextInt(). Otherwise, we will read the leftover "%n" as the serialnumber
        input.nextLine();

        System.out.print("Serial#: ");
        this.serialNumber=input.nextLine();

        this.direction=0;

    }


    public abstract void turnLeft(int degrees);
    public abstract void turnRight(int degrees);

    public void setColour(String colour) {
        this.colour = colour;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public void setBuyingDate(Calendar buyingDate) {
        this.buyingDate = buyingDate;
    }



    public String getColour() {
        return colour;
    }
    public String getName() {
        return name;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public int getModelYear() {
        return modelYear;
    }
    public int getPrice() {
        return price;
    }
    public int getDirection() {
        return direction;
    }
    public double getSpeed() {
        return speed;
    }
    public Calendar getBuyingDate() {
        return buyingDate;
    }

    @Override
    public int compareTo(Vehicle v){
        if(this.getPrice()<v.getPrice()) {
            return -1;
        } else if(this.getPrice()>v.getPrice()){
            return 1;
        } else return 0;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException{
        Vehicle v = (Vehicle)super.clone();
        v.setColour(this.getColour());
        v.setName(this.getName());
        v.setSerialNumber(this.getSerialNumber());
        v.setModelYear(this.getModelYear());
        v.setPrice(this.getPrice());
        v.setDirection(this.getDirection());
        v.setSpeed(this.getSpeed());
        v.setBuyingDate((Calendar)this.getBuyingDate().clone());
        return v;
    }

    @Override
    public String toString(){
        return String.format("Name: %s, Color: %s, Price: %d, Model: %d, Serialnumber: %s, Direction: %d, Speed: %.2f",
                getName(), getColour(), getPrice(), getModelYear(), getSerialNumber(), getDirection(), getSpeed());
    }

    @Override
    public void stop(){
        this.speed=0;
        System.out.println("Vehicle stops.");
    }

    @Override
    public void writeData(PrintWriter out) throws IOException{
        out.printf("%s,%s,%s,%d,%d,%s,%d,%.2f,%d,%d,%d%n",
             getClass().getName(), getName(), getColour(), getPrice(), getModelYear(), getSerialNumber(), getDirection(), getSpeed(),
             getBuyingDate().get(Calendar.YEAR), getBuyingDate().get(Calendar.MONTH),getBuyingDate().get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void readData(Scanner in) throws IOException{
        in.useDelimiter(",");
        this.setName(in.next());
        this.setColour(in.next());
        this.setPrice(in.nextInt());
        this.setModelYear(in.nextInt());
        this.setSerialNumber(in.next());
        this.setDirection(in.nextInt());
        this.setSpeed(in.nextDouble());
        this.setBuyingDate(new GregorianCalendar(in.nextInt(), in.nextInt(), in.nextInt()));

    }
}