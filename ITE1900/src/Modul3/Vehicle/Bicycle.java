package Modul3.Vehicle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Bicycle extends Vehicle {
    private int gears;
    private Calendar productionDate;

    Bicycle() {
    }

    Bicycle(String name, String colour, int price, int modelYear, String serialNumber, int gears, int direction) {
        super(name, colour, price, modelYear, serialNumber, direction);
        this.gears=gears;
        productionDate = new GregorianCalendar();
    }

    @Override
    public void setAllFields(){
        super.setAllFields();
        System.out.print("Gears: ");
        gears =input.nextInt();
        setProductionDate(new GregorianCalendar());
    }

    @Override
    public void turnRight(int degrees){
        System.out.println("bicycle has turned right by " + degrees + " degrees");
    }

    @Override
    public void turnLeft(int degrees){
        System.out.println("bicycle has turned left by " + degrees + " degrees");
    }

    public int getGears(){return this.gears;}
    public Calendar getProductionDate(){return this.productionDate;}

    public void setGears(int gears){this.gears = gears;}
    public void setProductionDate(Calendar productionDate){this.productionDate=productionDate;}

    @Override
    protected Object clone() throws CloneNotSupportedException{
        Bicycle b = (Bicycle)super.clone();
        b.setGears(this.getGears());
        b.setProductionDate((Calendar)getProductionDate().clone());
        return b;
    }

    @Override
    public String toString(){
        return String.format("%s, Gears: %d, Productiondate: %s", super.toString(), getGears(), getProductionDate().getTime());
    }

    @Override
    public void accelerate(int speedfactor){
        if(getSpeed()==0){
            setSpeed(0.3*speedfactor);
        } else {
            setSpeed(getSpeed()*0.5*speedfactor);
            if(getSpeed()>MAX_SPEED_BIKE){
                setSpeed(MAX_SPEED_BIKE);
            }
        }
    }

    @Override
    public void brakes(int speedfactor){
        setSpeed(getSpeed()/(speedfactor*0.5));
    }

    @Override
    public void writeData(PrintWriter out) throws IOException {
        out.printf("%s,%s,%s,%d,%d,%s,%d,%.2f,%d,%d,%d,%d,%d,%d,%d%n",
                getClass().getName(), getName(), getColour(), getPrice(), getModelYear(), getSerialNumber(), getDirection(), getSpeed(),
                getBuyingDate().get(Calendar.YEAR), getBuyingDate().get(Calendar.MONTH),getBuyingDate().get(Calendar.DAY_OF_MONTH), getGears(),
                getProductionDate().get(Calendar.YEAR), getProductionDate().get(Calendar.MONTH),getProductionDate().get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void readData(Scanner in) throws IOException{
        in.useDelimiter(",|\n");
        this.setName(in.next());
        this.setColour(in.next());
        this.setPrice(in.nextInt());
        this.setModelYear(in.nextInt());
        this.setSerialNumber(in.next());
        this.setDirection(in.nextInt());
        this.setSpeed(in.nextDouble());
        this.setBuyingDate(new GregorianCalendar(in.nextInt(), in.nextInt(), in.nextInt()));
        this.setGears(in.nextInt());
        this.setProductionDate(new GregorianCalendar(in.nextInt(), in.nextInt(), in.nextInt()));
    }
}
