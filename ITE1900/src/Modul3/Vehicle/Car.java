package Modul3.Vehicle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Car extends Vehicle{
    private int power;
    private Calendar productionDate;

    Car() {
    }

    Car(String name, String colour, int price, int modelYear, String serialNumber, int power, int direction) {
        super(name, colour, price, modelYear, serialNumber, direction);
        this.power=power;
        productionDate = new GregorianCalendar();
    }

    @Override
    public void setAllFields(){
        super.setAllFields();
        System.out.print("Power: ");
        power=input.nextInt();
        setProductionDate(new GregorianCalendar());
    }

    @Override
    public void turnLeft(int degrees){
        //turning left subtracts from the angle
        if(degrees>=0 && degrees<=360) {
            int dir= this.getDirection()-degrees;

            if(dir<0) {
                dir += 360;
            }
            super.setDirection(dir);
        }
    }

    @Override
    public void turnRight(int degrees){
        //turning right adds to the angle
        if(degrees>=0 && degrees<=360) {
            int dir= this.getDirection()+degrees;

            if(dir>360) {
                dir -= 360;
            }
            super.setDirection(dir);
        }
    }

    public int getPower(){return this.power;}
    public Calendar getProductionDate(){return this.productionDate;}

    public void setPower(int power){this.power=power;}
    public void setProductionDate(Calendar productionDate){this.productionDate=productionDate;}

    @Override
    protected Object clone() throws CloneNotSupportedException{
        Car c = (Car)super.clone();
        c.setPower(this.getPower());
        c.setProductionDate((Calendar)getProductionDate().clone());
        return c;
    }

    @Override
    public String toString(){
        return String.format("%s, Power: %d, Productiondate: %s", super.toString(), getPower(), getProductionDate().getTime());
    }

    @Override
    public void accelerate(int speedfactor){
        if(getSpeed()==0){
            setSpeed(0.5*speedfactor);
        } else {
            setSpeed(getSpeed()*speedfactor);
            if(getSpeed()>MAX_SPEED_CAR){
                setSpeed(MAX_SPEED_CAR);
            }
        }
    }

    @Override
    public void brakes(int speedfactor){
        setSpeed(getSpeed()/speedfactor);
    }

    @Override
    public void writeData(PrintWriter out) throws IOException {
        out.printf("%s,%s,%s,%d,%d,%s,%d,%.2f,%d,%d,%d,%d,%d,%d,%d%n",
                getClass().getName(), getName(), getColour(), getPrice(), getModelYear(), getSerialNumber(), getDirection(), getSpeed(),
                getBuyingDate().get(Calendar.YEAR), getBuyingDate().get(Calendar.MONTH),getBuyingDate().get(Calendar.DAY_OF_MONTH), getPower(),
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
        this.setPower(in.nextInt());
        this.setProductionDate(new GregorianCalendar(in.nextInt(), in.nextInt(), in.nextInt()));


    }

}