public class HashForString {
    public static long hashCodeForString(String s){
        long hash=0, b=31;
        for(int i=0; i<s.length(); i++){
            hash=hash*b+s.charAt(i);
        }
        return hash;
    }
}
