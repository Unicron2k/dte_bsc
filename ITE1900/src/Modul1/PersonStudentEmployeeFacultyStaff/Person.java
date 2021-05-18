package Modul1.PersonStudentEmployeeFacultyStaff;

class Person{
    private String name, address, email;
    private int housenumber;
    private double phonenumber;

    Person(){this("Jens Jensen", "Holmlia", 42, "jens.jensen@jensemann.jens", 42425555);}
    Person(String name, String address, int housenumber, String email, double phonenumber){
        this.name=name;
        this.address=address;
        this.email=email;
        this.housenumber=housenumber;
        this.phonenumber=phonenumber;
    }

    public String toString(){
        return "Name: " + name + "\nAddress: " + address + " " + housenumber + "\nPhonenumber: " + phonenumber + "\nEmail: " +  email; //don't need super here...
    }
}