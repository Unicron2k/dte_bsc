//package RSA;
// Koden er hentet fra http://trondal.com/rsakryptering/rsa.html
// Noen modifiseringer i metoder med støtte for BigInteger

import java.io.*;
import java.math.BigInteger;

class Verktoy {

	// gcd finner st?rste felles faktor av u og v
	// ved hjelp av Euklid's algoritme
	static long gcd(long u, long v) {
		long temp;
		while (v != 0) {
			temp = u % v;
			u = v;
			v = temp;
		}
		return u;
	}
	// gcd finner st?rste felles faktor av u og v
	// ved hjelp av Euklid's algoritme
	static BigInteger gcd(BigInteger u, BigInteger v) {
		BigInteger temp;
		while (!v.equals(BigInteger.ZERO)) {
			temp = u.mod(v);
			u = v;
			v = temp;
		}
		return u;
	}
	// Finner e p? grunnlag av phi.
	// e m? v?re innbyrdes primisk med phi
	public static long finnE(long phi){
  	long sff=0, e=2;
    while (sff != 1){
    	e++;
      sff = gcd(e, phi);
    }
    return e;
	}
	// Finner e p? grunnlag av phi.
	// e m? v?re innbyrdes primisk med phi
	public static BigInteger finnE(BigInteger phi){
		BigInteger sff=BigInteger.ZERO;
		BigInteger e=BigInteger.ONE;
		e = e.add(BigInteger.ONE);
	    while (sff.longValue() != 1){
		    e = 	e.add(BigInteger.ONE);
		      sff = gcd(e, phi);
	    }
	    return e;
		}
  // Finner d (den private n?kkelen) p? grunnlag av
  // e og phi ved ? l?se kongruensen e*d=1(mod phi)
  public static long finnD(long e, long phi) {
    long u1=1, u2=0, u3=phi, v1=0, v2=1, v3=e;
    long q, d, t1, t2, t3, uu, vv;
    while (v3 != 0) {
      q = u3/v3;
      t1 = u1-q*v1;
      t2 = u2-q*v2;
      t3 = u3-q*v3;
      u1=v1;  u2=v2;  u3=v3;
      v1=t1;  v2=t2;  v3=t3;
    }
    uu = u1;
    vv = u2;
    if (vv < 0) d = vv+phi;
    else d = vv;
    return d;
  }
  
  // Finner d (den private n?kkelen) p? grunnlag av
  // e og phi ved ? l?se kongruensen e*d=1(mod phi)
  public static BigInteger finnD(BigInteger e, BigInteger phi) {
	  BigInteger u1=BigInteger.valueOf(1), u2=BigInteger.valueOf(0), u3=phi, v1=BigInteger.valueOf(0), v2=BigInteger.valueOf(1), v3=e;
	  BigInteger q, d, t1, t2, t3, uu, vv, q11, q12, q13;
	  while (v3.longValue() != 0) {
	      q = u3.divide(v3);
	      t1 = u1.subtract(q);
	      q11 = q.multiply(v1);
	      q12 = q.multiply(v2);
	      q13 = q.multiply(v3);
	      t1 = u1.subtract(q11);
	      t2 = u2.subtract(q12);
	      t3 = u3.subtract(q13);
	      u1=v1;  u2=v2;  u3=v3;
	      v1=t1;  v2=t2;  v3=t3;
	    }
	    uu = u1;
	    vv = u2;
	    if (vv.longValue() < 0) d = vv.add(phi);
	    else d = vv;
	    return d;
	  }

	// Regner ut a opph?yd i x modulus m
  public static long modPow(long a, long x, long m){
		BigInteger A = new BigInteger(new Long(a).toString());
		BigInteger X = new BigInteger(new Long(x).toString());
		BigInteger M = new BigInteger(new Long(m).toString());
		return A.modPow(X,M).longValue();
	}
  
	// Regner ut a opph?yd i x modulus m
  public static BigInteger modPow(BigInteger a, BigInteger x, BigInteger m){
		/*BigInteger A = new BigInteger(new Long(a).toString());
		BigInteger X = new BigInteger(new Long(x).toString());
		BigInteger M = new BigInteger(new Long(m).toString());
		return A.modPow(X,M).longValue();
		*/
		return a.modPow(x,m);
	}

  // Skriver et tall til en angitt tekstfil
  public static void skriv(long tall, String filnavn){
    try{
      System.out.println("skriver til "+filnavn);
      FileWriter tekstFilSkriver = new FileWriter(filnavn, false);
      PrintWriter tekstSkriver = new PrintWriter(tekstFilSkriver);
      tekstSkriver.print(tall);
      tekstSkriver.close();
    }
    catch (IOException unntak) {
      System.out.print("error: Feil ved skriving til fil! " + unntak);
    }
  }

  // Leser et tall fra en angitt tekstfil
  public static long les(String filnavn){
    String tallStreng;
    tallStreng="-1";
    try{
      System.out.println("leser fra "+filnavn);
      FileReader tekstFilLeser = new FileReader(filnavn);
      BufferedReader tekstLeser = new BufferedReader(tekstFilLeser);
      tallStreng = tekstLeser.readLine();
    }
    catch (IOException unntak) {
      System.out.print("error: Feil ved lesing av fil! " + unntak);
    }
    return new Long(tallStreng).longValue();
  }
}