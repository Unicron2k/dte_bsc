//package RSA;


import java.math.BigInteger;
import java.util.Random;


public class Test {
    public static void main(String[] args) {
        
    	long C, d, M, N, e, Text, C1, Text1;
    	
    	int p =11;
    	int q =13;
    	
    	N = p * q;
    	int t = (p-1)*(q-1);
    	
    	d = 103;
    	e = 7;

    	M =89;
    	
        C = modPow(M, e, N);
        
        System.out.println("krypterer M; C = M^e (mod N) = "+M+"^"+e+" (mod "+N+") = "+C);
    	
    	C1 = (int)(Math.pow(M, e) % N) ;
    	       
        Text = modPow(C, d, N);
        
        System.out.println("dekrypterer C; M = C^d (mod N) = "+C+"^"+d+" (mod "+N+") = "+M);
    	
        System.out.println("Tekst sendt: " + M +" Chiper: " + C + " Tekst mottatt: " + Text);
    
  		/*BigInteger A = new BigInteger(64, new Random());
  		
  		long b = A.longValue();
  		if(b%2==0) b++;
  		long NN = faktoriser(b);
  		*/
    }

	// Regner ut a opph?yd i x modulus m
    public static long modPow(long a, long x, long m){
  		BigInteger A = new BigInteger(new Long(a).toString());
  		BigInteger X = new BigInteger(new Long(x).toString());
  		BigInteger M = new BigInteger(new Long(m).toString());
  		return A.modPow(X,M).longValue();
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
}



