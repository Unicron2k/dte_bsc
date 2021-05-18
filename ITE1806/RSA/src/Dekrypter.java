//package RSA;
class Dekrypter {
  public static void main(String[] args){
    long C, d, N, M;

    C = Verktoy.les("C.txt");
    System.out.println("C = "+C);

    d = Verktoy.les("privat.txt");
    System.out.println("d = "+d);

    N = Verktoy.les("offentlig-N.txt");
    System.out.println("N = "+N);

    M = Verktoy.modPow(C, d, N);
    System.out.println("dekrypterer C; M = C^d (mod N) = "+C+"^"+d+" (mod "+N+") = "+M);
    Verktoy.skriv(M, "M1.txt");
  }
}