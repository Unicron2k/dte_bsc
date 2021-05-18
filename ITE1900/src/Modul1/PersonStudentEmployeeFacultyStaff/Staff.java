package Modul1.PersonStudentEmployeeFacultyStaff;

class Staff extends Employee{
    private String title;

    Staff(){this.title="Janitor";}
    Staff(String title, String date, double salary, String name, String address, int housenumber, String email, double phonenumber){
        super(date, salary, name, address, housenumber, email, phonenumber);
        this.title = title;}

    public String toString(){
        return "Title: " + title +"\n"+super.toString();
    }
}
