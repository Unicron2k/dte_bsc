package Modul1.PersonStudentEmployeeFacultyStaff;

public class TestPersons {
    public static void main(String[] args)   {

        Student s = new Student(Student.MEDICAL_SCHOOL, 2, "Bob Kåre", "Heimdalveien", 42, "b.kåre@tussi.no", 95257593);
        Faculty f = new Faculty(Faculty.DEAN, "D3340", "26.12-2018", 600000, "Frank Egil", "Jokkmokkgata", 12, "fegil@gmail.com", 23130987);
        Staff s2= new Staff("Cleaner", "23.04-2017", 450000, "Nessie Ness", "Bjørnebærhaugen", 43, "nessie_da_monstah@yahoo.com", 95857565);

        System.out.printf("Student:%n%s%n%n", s.toString());
        System.out.printf("Faculty:%n%s%n%n", f.toString());
        System.out.printf("Staff:%n%s%n%n", s2.toString());
    }
}