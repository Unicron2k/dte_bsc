public class Password {
    //[test kun denne metoden]
    static boolean checkPassword(String password){
        if (!(password==null))
            return checkPasswordLength(password) && checkPasswordForAlphanumerics(password) && checkPasswordForDigitCount(password);
        else
            return false;
    }

    //[hjelpemetoden for checkPassword]
    static boolean checkPasswordLength(String password){
        return password.length()>10;
    }

    //[hjelpemetoden for checkPassword]
    static boolean checkPasswordForAlphanumerics(String password){
        return password.matches("^[a-zA-Z0-9]*$");
    }

    //[hjelpemetoden for checkPassword]
    static boolean checkPasswordForDigitCount(String password){
        return password.matches("(?:\\D*\\d){3,}.*");
    }
}
