package Modul1.PersonStudentEmployeeFacultyStaff;

class Student extends Person{
    private int course, year;

    public static final int MECHANIC = 1;
    public static final int COMPUTER_IT = 2;
    public static final int NURSING_SCHOOL = 3;
    public static final int MEDICAL_SCHOOL = 4;

    Student(){
        this.course=MECHANIC;
        this.year=1;
    }
    Student(int course, int year, String name, String address, int housenumber, String email, double phonenumber) {
        super(name, address, housenumber, email, phonenumber);
        this.course =course;
        this.year=year;
    }

    String getCourseAsString(int course){
        switch (course){
            case MECHANIC:
                return "Mechanic";
            case COMPUTER_IT:
                return "Computer\\IT";
            case NURSING_SCHOOL:
                return "Nursing School";
            case MEDICAL_SCHOOL:
                return "Medical School";
            default:
                return "Not a valid course!";
        }
    }
    public String toString(){
        return "Year: " + year + "\nCourse: "+ getCourseAsString(this.course) +"\n"+ super.toString();
    }
}