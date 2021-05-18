//package RSA;
class Krypter {
  public static void main(String[] args){
    long M, N, e, C;

    M = Verktoy.les("M.txt");
    System.out.println("M = "+M);

    N = Verktoy.les("offentlig-N.txt");
    System.out.println("N = "+N);
    if(N <= M) {
      System.out.println("Feil: For stor M i forhold til N.");
      System.exit(1);
    }

    e = Verktoy.les("offentlig-e.txt");
    System.out.println("e = "+e);

    C = Verktoy.modPow(M, e, N);
    System.out.println("krypterer M; C = M^e (mod N) = "+M+"^"+e+" (mod "+N+") = "+C);
    Verktoy.skriv(C, "C.txt");
  }
}