package Modul1.PersonStudentEmployeeFacultyStaff;

class Employee extends Person{
    private String startDate;
    private double salary;
    Employee(){
        this.startDate="1.Jan.1970";
        this.salary=0;
    }

    Employee(String date, double salary, String name, String address, int housenumber, String email, double phonenumber) {
        super(name, address, housenumber, email, phonenumber);
        this.startDate=date;
        this.salary=salary;
    }
    public String toString(){
        return "Started: " + startDate + "\nSalary: " + salary+"\n"+super.toString();
    }
}