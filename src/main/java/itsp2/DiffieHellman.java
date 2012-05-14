package itsp2;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

public class DiffieHellman {

	static BigInteger alice_p;
	static BigInteger alice_a;
	static BigInteger alice_A;
	static BigInteger alice_g;
	static BigInteger alice_K;
	static BigInteger bob_b;
	static BigInteger bob_B;
	static BigInteger bob_K;

	static Date start;
	static Date end;

	public static void main(String[] args) {
		p("DiffieHellman");
		//wikitest();
        compareTest();
	}

	public static void wikitest() {
		start = new Date();
		p("Wikpedia Test");
		p("p = 13, g = 2");
		p("a = 5, b = 7");

		alice_p = BigInteger.valueOf(13);
		alice_g = BigInteger.valueOf(2);

		alice_a = BigInteger.valueOf(5);
		bob_b = BigInteger.valueOf(7);
		p("Alice berechnet A = 2^5 mod 13 = 6 und sendet dieses Ergebnis an Bob.");
		alice_A = calculate(alice_g, alice_a, alice_p);
		p("Test: " + alice_A);

		p("Bob berechnet B = 2^7 mod 13 = 11 und sendet dieses Ergebnis an Alice.");
		bob_B = calculate(alice_g, bob_b, alice_p);
		p("Test: " + bob_B);

		p("Alice berechnet K = 11^5 mod 13 = 7.");
		alice_K = calculate(bob_B, alice_a, alice_p);
		p("Test: " + alice_K);

		p("Bob berechnet K = 6^7 mod 13 = 7.");
		bob_K = calculate(alice_A, bob_b, alice_p);
		p("Test: " + bob_K);

		p("Beide haben den selben Schlüssel: Alice(" + alice_K + ") und Bob("
				+ bob_K + ")");

		end = new Date();

		long time = end.getTime() - start.getTime();
		p("Zeit: " + time + "ms");
	}

	public static BigInteger getRandomNumber(BigInteger p) {
		SecureRandom rnd = new SecureRandom();
		BigInteger r;
		do {
			r = new BigInteger(p.bitLength(), rnd);
		} while (r.compareTo(p.subtract(BigInteger.valueOf(2))) < 0 && r.compareTo(BigInteger.ZERO) > 0);
		return r;
	}

	public static BigInteger getPrime() {
		SecureRandom rnd = new SecureRandom();
		BigInteger prime;
		boolean isPrime;
		do {
			prime = BigInteger.probablePrime(256, rnd);

			int certainty = 1;
			isPrime = prime.isProbablePrime(certainty);
		} while (!isPrime);

		return prime;
	}
	
	public static BigInteger getGenerator(BigInteger p){
		SecureRandom rnd = new SecureRandom();
		BigInteger g;
		do {
			g = new BigInteger(p.bitLength(), rnd);
		} while (g.compareTo(p.subtract(BigInteger.valueOf(2))) < 0 && g.compareTo(BigInteger.ONE) > 0 && g.mod(p) != BigInteger.ZERO);
		return g;
	}

	public static BigInteger calculate(BigInteger base, BigInteger expo,
			BigInteger mod) {
		BigInteger tmp = powerIter(base, expo);
		return tmp.mod(mod);
	}

    public static BigInteger calculateSM(BigInteger base, BigInteger expo,
                                       BigInteger mod) {
        BigInteger tmp = powerIterSM(base, expo);
        return tmp.mod(mod);
    }

	public static void p(String s) {
		System.out.println(s);
	}

	public static BigInteger power(BigInteger a, BigInteger exponent) {
		if (exponent.equals(BigInteger.ONE))
			return a;
		else if (exponent.equals(BigInteger.ZERO))
			return BigInteger.ONE;
		else
			return a.multiply(power(a, exponent.subtract(BigInteger.ONE)));
	}

    public static BigInteger powerIter(BigInteger a, BigInteger exponent) {
        BigInteger result = a;
        if (exponent.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        }
        while(!exponent.equals(BigInteger.ONE)) {
            result = result.multiply(a);
            exponent = exponent.subtract(BigInteger.ONE);
        }
        return result;
    }

    public static BigInteger powerIterSM(BigInteger a, BigInteger exponent) {
        int bits = exponent.bitLength();
        BigInteger result = new BigInteger("1");

        for(int n = bits - 1; n >= 0; n--) {
            // SQ
            result = result.multiply(result);

            if (exponent.testBit(n)) {
                // MP
                result = result.multiply(a);
            }
        }
        return result;
    }

    public static void runDHKE(BigInteger p, BigInteger g, BigInteger a, BigInteger b, boolean useSM) {
        System.out.println("Diffie-Hellman Key Exchange " + (useSM ? " using Square-Multiply" : ""));
        System.out.println("p: " + p);
        System.out.println("g: " + g);

        System.out.println("Secrets");
        System.out.println("a: " + a);
        System.out.println("b: " + b);

        BigInteger A = useSM ? DiffieHellman.calculateSM(g, a, p) : DiffieHellman.calculate(g, a, p);
        System.out.println("A: " + A);

        BigInteger B = useSM ? DiffieHellman.calculateSM(g, b, p) : DiffieHellman.calculate(g, b, p);
        System.out.println("B: " + B);

        BigInteger SA = useSM ? DiffieHellman.calculateSM(B, a, p) : DiffieHellman.calculate(B, a, p);
        System.out.println("SA: " + SA);

        BigInteger SB = useSM ? DiffieHellman.calculateSM(A, b, p) : DiffieHellman.calculate(A, b, p);
        System.out.println("SB: " + SB);
    }

    public static void compareTest() {
        BigInteger p = new BigInteger("125507");
        BigInteger g = new BigInteger("18690");

        BigInteger a = new BigInteger("16561");
        BigInteger b = new BigInteger("65379");

        long start = System.nanoTime();
        for (int n = 0; n < 1; n++)
            runDHKE(p, g, a, b, false);
        System.out.println("Time for DHKE (without SM): " + (System.nanoTime() - start) / 1000 + " µs");

        start = System.nanoTime();
        for (int n = 0; n < 1; n++)
            runDHKE(p, g, a, b, true);
        System.out.println("Time for DHKE (with SM): " + (System.nanoTime() - start) / 1000 + " µs");
    }
}