import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

//package RSA;

class Knekk {
  public static void main(String[] args) {
    long N = Verktoy.les("offentlig-N.txt");
    System.out.println("N = "+N);
    long e = Verktoy.les("offentlig-e.txt");
    System.out.println("e = "+e);
    System.out.print("faktoriserer N...");
	LinkedList factors = rhoFactors(BigInteger.valueOf(N));
	BigInteger largest = (BigInteger) factors.get(0);
    //long p = faktoriser(N);
	long p = largest.longValue();
    long q = N/p;
    System.out.println("ferdig! N = " + p + " * " + q);
    long phi = (p-1)*(q-1);
    System.out.println("phi = "+phi);
    long d = Verktoy.finnD(e, phi);
    System.out.println("d = "+d);
    Verktoy.skriv(d, "knekk-privat.txt");
    //LinkedList fs = tdFactors( BigInteger.valueOf(N));
	//LinkedList factors = rhoFactors(BigInteger.valueOf(N));

	//BigInteger largest = (BigInteger) factors.get(0);
	System.out.println("P: "+ p + " q: " + q);
	System.out.println(largest);
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
