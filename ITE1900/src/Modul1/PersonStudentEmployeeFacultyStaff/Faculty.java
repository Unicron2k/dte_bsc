package Modul1.PersonStudentEmployeeFacultyStaff;

class Faculty extends Employee{
    private String officeNo;
    private int rank;

    public static final int PRINCIPAL =1;
    public static final int DEAN =2;
    public static final int PROFESSOR =3;
    public static final int LECTURER =4;

    Faculty(){
        this.rank=LECTURER;
        this.officeNo="A0000";
    }
    Faculty(int rank, String officeNo, String date, double salary, String name, String address, int housenumber, String email, double phonenumber){
        super(date, salary, name, address, housenumber, email, phonenumber);
        this.rank=rank;
        this.officeNo=officeNo;
    }

    private String getRankAsString(int rank){
        switch (rank){
            case PRINCIPAL:
                return "Principal";
            case DEAN:
                return "Dean";
            case PROFESSOR:
                return "Professor";
            case LECTURER:
                return "Lecturer";
            default:
                return "Not a valid rank!";
        }
    }

    public String toString(){
        return "Office: " + officeNo +"\nRank: " + getRankAsString(rank)+"\n"+ super.toString();
    }
}
