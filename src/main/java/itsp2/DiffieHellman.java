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

    public static BigInteger calculateJavaSM(BigInteger base, BigInteger expo, BigInteger mod) {
        return base.modPow(expo, mod);
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

    enum SMMethod {
        NaiiveSM,
        CustomCM,
        NativeCM
    }

    public static void runDHKE(BigInteger p, BigInteger g, BigInteger a, BigInteger b, SMMethod sm) {
        switch(sm) {
            case NaiiveSM:
                System.out.println("Diffie-Hellman Key Exchange");
                break;
            case CustomCM:
                System.out.println("Diffie-Hellman Key Exchange using custom Square-Multiply");
                break;
            case NativeCM:
                System.out.println("Diffie-Hellman Key Exchange using modPow");
                break;
        }
        System.out.println("p: " + p);
        System.out.println("g: " + g);

        System.out.println("Secrets");
        System.out.println("a: " + a);
        System.out.println("b: " + b);

        BigInteger A = null;
        switch(sm) {
            case NaiiveSM:
                A = DiffieHellman.calculate(g, a, p);
                break;
            case CustomCM:
                A = DiffieHellman.calculateSM(g, a, p);
                break;
            case NativeCM:
                A = DiffieHellman.calculateJavaSM(g, a, p);
                break;
        }
        System.out.println("A: " + A);

        BigInteger B = null;
        switch(sm) {
            case NaiiveSM:
                B = DiffieHellman.calculate(g, b, p);
                break;
            case CustomCM:
                B = DiffieHellman.calculateSM(g, b, p);
                break;
            case NativeCM:
                B = DiffieHellman.calculateJavaSM(g, b, p);
                break;
        }
        System.out.println("B: " + B);

        BigInteger SA = null;
        switch(sm) {
            case NaiiveSM:
                SA = DiffieHellman.calculate(B, a, p);
                break;
            case CustomCM:
                SA = DiffieHellman.calculateSM(B, a, p);
                break;
            case NativeCM:
                SA = DiffieHellman.calculateJavaSM(B, a, p);
                break;
        }
        System.out.println("SA: " + SA);

        BigInteger SB = null;
        switch(sm) {
            case NaiiveSM:
                SB = DiffieHellman.calculate(A, b, p);
                break;
            case CustomCM:
                SB = DiffieHellman.calculateSM(A, b, p);
                break;
            case NativeCM:
                SB = DiffieHellman.calculateJavaSM(A, b, p);
                break;
        }
        System.out.println("SB: " + SB);
    }

    public static void compareTest() {
        /*BigInteger p = new BigInteger("125507");
        BigInteger g = new BigInteger("18690");

        BigInteger a = new BigInteger("16561");
        BigInteger b = new BigInteger("65379");
          */
        BigInteger p = new BigInteger("2207");
        BigInteger g = new BigInteger("173");

        BigInteger a = new BigInteger("25");
        BigInteger b = new BigInteger("864");

        long start = System.nanoTime();
        for (int n = 0; n < 1; n++)
            runDHKE(p, g, a, b, SMMethod.NaiiveSM);
        System.out.println("============================");
        System.out.println("Time for DHKE (without SM): " + (System.nanoTime() - start) / 1000+ " µs");

        start = System.nanoTime();
        for (int n = 0; n < 2; n++)
            runDHKE(p, g, a, b, SMMethod.CustomCM);
        System.out.println("============================");
        System.out.println("Time for DHKE (with SM): " + (System.nanoTime() - start) / 1000 + " µs");

        start = System.nanoTime();
        for (int n = 0; n < 2; n++)
            runDHKE(p, g, a, b, SMMethod.NativeCM);
        System.out.println("============================");
        System.out.println("Time for DHKE (with Java SM): " + (System.nanoTime() - start) / 1000 + " µs");
    }
}