//package RSA;
import java.math.BigInteger;

public class Generator {
  public static void main(String[] args) {
    long p=0, q=0, N, phi, e, d;
    int bits=4, pbits, qbits;

    try {
      //bits = new Integer(args[0]).intValue();
      bits = new Integer(62);
      System.out.println("Maks integer verdi: " + Integer.MAX_VALUE);
      if(bits<5 || bits>62) throw (new Exception());
    } catch (Exception ex) {
      feil();
    }

    pbits = bits/2;
    qbits = bits-pbits;

    java.util.Random r = new java.util.Random();

    while (p<3) p = BigInteger.probablePrime(pbits, r).longValue();
    System.out.print("lager "+pbits+" bits p = "+p+"\n");

    while (q<3) q = BigInteger.probablePrime(qbits, r).longValue();
    System.out.print("lager "+qbits+" bits q = "+q+"\n");

    N = p*q;
    System.out.print("N = p * q = "+p+" * "+q+" = "+N+"\n");
    Verktoy.skriv(N, "offentlig-N.txt");

    phi = (p-1)*(q-1);
    System.out.print("phi = (p-1)*(q-1) = "+(p-1)+" * "+(q-1)+" = "+phi+"\n");

    e = Verktoy.finnE(phi);
    System.out.print("velger en e (innbyrdisk primisk med phi) = "+e+"\n");
    Verktoy.skriv(e, "offentlig-e.txt");

    d = Verktoy.finnD(e, phi);
    System.out.print("finner d ved ? l?se kongruensen e*d=1(mod phi); d = "+d+"\n");
    Verktoy.skriv(d, "privat.txt");
  }

  private static void feil() {
    System.out.println("java Generator <antall_bits_i_N>");
    System.out.println("Antall bits i n?kkel m? v?re minst 5, maks 63");
    System.exit(1);
  }
}