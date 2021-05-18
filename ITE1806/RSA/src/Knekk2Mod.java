import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

//package RSA;

class Knekk2Mod {
  public static void main(String[] args) {


	    BigInteger p=BigInteger.ZERO, q=BigInteger.ZERO, N, phi, e, d;
	    int bits=1, pbits, qbits;
	    long testNumber = 1;
        long startTime, endTime;


        /*
        // ask user for number of bits in N
        System.out.println("\nPlease enter number of bits in key: ");
        Scanner myScanner = new Scanner(System.in);
        if (myScanner.hasNextInt()) {
            bits = myScanner.nextInt();
        }

	    pbits = bits/2;
	    qbits = bits-pbits;

	    Random r = new Random();
        System.out.println("\nFinding prime numbers....");
	    while (p.longValue()<3) p = BigInteger.probablePrime(pbits, r);
	    System.out.print("lager "+pbits+" bits p = "+p+"\n");
	    //q = p.nextProbablePrime();
	    while (q.longValue()<3) q = BigInteger.probablePrime(qbits, r);
	    System.out.print("lager "+qbits+" bits q = "+q+"\n");



	    N = p.multiply(q);
	    System.out.println("N = "+N);
	    phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
	    System.out.print("phi = (p-1)*(q-1) = "+  phi +"\n");
	    
	    e = Verktoy.finnE(phi);
	    System.out.print("velger en e (innbyrdisk primisk med phi) = "+e+"\n");

	    d = Verktoy.finnD(e, phi);
	    System.out.println("d = "+d);

        // ask user for number to be encrypted
        System.out.println("\nPlease enter number to be encrypted: ");

        // construct scanner to put continuous int-interpretable input in
        // variable testNumber
        myScanner = new Scanner(System.in);
        if (myScanner.hasNextLong()) {
            testNumber = myScanner.nextLong();
        }
        // cast testNumber as a long variable and set it to BigInteger M
        BigInteger M = BigInteger.valueOf(testNumber);

	    System.out.println("Krypterer tallet = " + M);
	    BigInteger C = Verktoy.modPow(M, e, N);
	    System.out.println("Krypterert verdi = " + C);

	    */


        N = new BigInteger("643878978000907256009472011");
	  	BigInteger C = new BigInteger("19414739223217152237878131");



	    // Faktoriserer N, finner hemmelig nøkkel og dekrypterer C
	    System.out.println("Finne hemmelig nøkkel ut fra offentlig, dekrypterer " + C +" skal gi verdien " );
	    System.out.println("faktoriserer N...");
	    startTime = System.currentTimeMillis();
		LinkedList factors = rhoFactors(N);
        // stop timing algorithm here
        endTime = System.currentTimeMillis();
        System.out.println("\nTotal run time for factoring " + N + " was "
                + (endTime - startTime) + " milliseconds.\n");
		BigInteger p_new = (BigInteger) factors.get(0);
		BigInteger q_new = N.divide(p_new);
	    System.out.println("ferdig! N = " + p_new + " * " + q_new);
	    
	    BigInteger phi_new  = (p_new.subtract(BigInteger.valueOf(1))).multiply(q_new.subtract(BigInteger.valueOf(1)));
	    System.out.println("phi_new = "+phi_new);
	    BigInteger e_new = Verktoy.finnE(phi_new);
	    BigInteger d_new = Verktoy.finnD(e_new, phi_new);
	    System.out.println("d_new = "+d_new);
	    
	    BigInteger M1 = Verktoy.modPow(C, d_new, N);
	    System.out.println("dekrypterer C; M = C^d (mod N) = "+C+"^"+d_new+" (mod "+N+") = "+M1);
	    
  }

  // Returnerer den st?rste faktoren i N
  // mindre eller lik kvadratroten av N.
  // Algoritmen er hentet fra Donald Knuth,
  // The Art of Computer Programming, vol.2.
  static long faktoriser(long N) {
	  long sqrtN, x, y, r;

	  if (N<3 || N%2 == 0)
		  throw new ArithmeticException("N<2 eller ikke oddetall");

	  sqrtN = (long)Math.sqrt(N);
	  x = 2*sqrtN + 1;
	  y = 1;
	  r = sqrtN*sqrtN - N;

	  while (r!=0) {
		  r += x;
		  x += 2;
		  while (r>0) {
			  r -= y;
			  y += 2;
		  }
	  }
	  return (x-y)/2;
	  // N = ((x-y)/2)*((x+y-2)/2),
	  // og (x-y)/2 er den st?rste faktoren
	  // av N mindre eller lik sqrt(N)
  }
  
  // Metodene nedenfor er hentet fra https://programmingpraxis.files.wordpress.com/2012/09/primenumbers.pdf
  public static LinkedList rhoFactors(BigInteger n)
  {
	  BigInteger f;
	  BigInteger two = BigInteger.valueOf(2);
	  LinkedList fs = new LinkedList();
	  if (n.compareTo(two) < 0)
	  {
		  return fs;
	  }
	  while (n.mod(two).equals(BigInteger.ZERO))
	  {
		  n = n.divide(two);
		  fs.add(two);
	  }
	  if (n.equals(BigInteger.ONE))
	  {
		  return fs;
	  }
	  while (! n.isProbablePrime(25))
	  {
		  f = rhoFactor(n, BigInteger.ONE);
		  n = n.divide(f);
		  fs.add(f);
	  }
	  fs.add(n);
	  Collections.sort(fs);
	  return fs;
  }

  private static BigInteger rhoFactor(BigInteger n, BigInteger c)
  {
	  BigInteger t = BigInteger.valueOf(2);
	  BigInteger h = BigInteger.valueOf(2);
	  BigInteger d = BigInteger.ONE;
	  while (d.equals(BigInteger.ONE))
	  {
		  t = t.multiply(t).add(c).mod(n);
		  h = h.multiply(h).add(c).mod(n);
		  h = h.multiply(h).add(c).mod(n);
		  d = t.subtract(h).gcd(n);
	  }
	  if (d.equals(n)) /* cycle */
	  {
		  return rhoFactor(n, c.add(BigInteger.ONE));
	  }
	  else if (isPrime(d)) /* success */
	  {
		  return d;
	  }
	  else /* found composite factor */
	  {
		  return rhoFactor(d, c.add(BigInteger.ONE));
	  }
  }

  public static Boolean isPrime(BigInteger n)
  {
	  Random r = new Random();
	  BigInteger two = BigInteger.valueOf(2);
	  BigInteger n3 = n.subtract(BigInteger.valueOf(3));
	  BigInteger a;
	  int k = 25;
	  if (n.compareTo(two) < 0)
	  {
		  return false;
	  }
	  if (n.mod(two).equals(BigInteger.ZERO))
	  {
		  return n.equals(two);
	  }
	  while (k > 0)
	  {
		  a = new BigInteger(n.bitLength(), r).add(two);
		  while (a.compareTo(n) >= 0)
		  {
			  a = new BigInteger(n.bitLength(), r).add(two);
		  }
		  if (! isSpsp(n, a))
		  {
			  return false;
		  }
		  k -= 1;
	  }
	  return true;
  }

  private static Boolean isSpsp(BigInteger n, BigInteger a)
  {
	  BigInteger two = BigInteger.valueOf(2);
	  BigInteger n1 = n.subtract(BigInteger.ONE);
	  BigInteger d = n1;
	  int s = 0;
	  while (d.mod(two).equals(BigInteger.ZERO))
	  {
		  d = d.divide(two);
		  s += 1;
	  }
	  BigInteger t = a.modPow(d, n);
	  if (t.equals(BigInteger.ONE) || t.equals(n1))
	  {
		  return true;
	  }
	  while (--s > 0)
	  {
		  t = t.multiply(t).mod(n);
		  if (t.equals(n1))
		  {
			  return true;
		  }
	  }
	  return false;
  }
}
