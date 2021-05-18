package primes;
import java.util.LinkedList;
import java.util.BitSet;
import java.util.Random;
import java.util.Collections;
import java.math.BigInteger;
import java.lang.Exception;
import java.lang.Boolean;

public class testprimes {

	public static void main (String[] args)
	{
		System.out.println(sieve(100));
		System.out.println(primes(100));
		System.out.println(primes(1000000).size());
		System.out.println(tdPrime(new BigInteger("600851475143")));
		System.out.println(tdFactors(new BigInteger("600851475143")));
		System.out.println(isPrime(new BigInteger("600851475143")));
		System.out.println(isPrime(new BigInteger("2305843009213693951")));
		System.out.println(rhoFactors(new BigInteger("3948415853060422661")));
		LinkedList factors = rhoFactors(new BigInteger("3948415853060422661"));

		BigInteger largest = (BigInteger) factors.get(factors.size()-1);
		System.out.println(largest);
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
	
	public static LinkedList tdFactors(BigInteger n)
	{
	BigInteger two = BigInteger.valueOf(2);
	LinkedList fs = new LinkedList();
	if (n.compareTo(two) < 0)
	{
	throw new IllegalArgumentException("must be greater than one");
	}
	while (n.mod(two).equals(BigInteger.ZERO))
	{
	fs.add(two);
	n = n.divide(two);
	}
	if (n.compareTo(BigInteger.ONE) > 0)
	{
	BigInteger f = BigInteger.valueOf(3);
	while (f.multiply(f).compareTo(n) <= 0)
	{
	if (n.mod(f).equals(BigInteger.ZERO))
	{
	fs.add(f);
	n = n.divide(f);
	}
	else
	{
	f = f.add(two);
	}
	}
	fs.add(n);
	}
	return fs;
	}
	
	public static Boolean tdPrime(BigInteger n)
	{
	BigInteger two = BigInteger.valueOf(2);
	if (n.compareTo(two) < 0)
	{
	throw new IllegalArgumentException("must be greater than one");
	}
	if (n.mod(two).equals(BigInteger.ZERO))
	{
	return n.equals(two);
	}
	BigInteger d = BigInteger.valueOf(3);
	while (d.multiply(d).compareTo(n) <= 0)
	{
	if (n.mod(d).equals(BigInteger.ZERO))
	{
	return false;
	}
	d = d.add(two);
	}
	return true;
	}

	public static LinkedList primes(int n)
	{
	if (n < 2)
	{
	throw new IllegalArgumentException("must be greater than one");
	}
	int m = (n-1) / 2;
	BitSet b = new BitSet(m);
	b.set(0, m);
	int i = 0;
	int p = 3;
	LinkedList ps = new LinkedList();
	ps.add(2);
	while (p * p < n)
	{
	if (b.get(i))
	{
	ps.add(p);
	int j = 2*i*i + 6*i + 3;
	while (j < m)
	{
	b.clear(j);
	j = j + 2*i + 3;
	}
	}
	i += 1; p += 2;
	}
	while (i < m)
	{
	if (b.get(i))
	{
	ps.add(p);
	}
	i += 1; p += 2;
	}
	return ps;
	}

	public static LinkedList sieve(int n)
	{
	BitSet b = new BitSet(n);
	LinkedList ps = new LinkedList();
	b.set(0,n);
	for (int p=2; p<n; p++)
	{
	if (b.get(p))
	{
	ps.add(p);
	for (int i=p+p; i<n; i+=p)
	{
	b.clear(i);
	}
	}
	}
	return ps;
	}
	
}
