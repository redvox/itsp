package itsp2;

import static net.java.quickcheck.generator.CombinedGeneratorsIterables.someLists;
import static net.java.quickcheck.generator.PrimitiveGenerators.integers;
import static org.testng.Assert.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.java.quickcheck.generator.PrimitiveGenerators;
import net.java.quickcheck.generator.iterable.Iterables;
import org.testng.annotations.Test;

import itsp2.*;

public class DiffieHellmanTest {

    @Test
    public void DHTestVector() {
        BigInteger p = new BigInteger("467");
        BigInteger g = new BigInteger("57");

        BigInteger a = new BigInteger("32");
        BigInteger b = new BigInteger("27");

        BigInteger A = DiffieHellman.calculate(g, a, p);
        assertEquals(A, new BigInteger("371"));

        BigInteger B = DiffieHellman.calculate(g, b, p);
        assertEquals(B, new BigInteger("350"));

        BigInteger SA = DiffieHellman.calculate(B, a, p);
        assertEquals(SA, new BigInteger("81"));

        BigInteger SB = DiffieHellman.calculate(A, b, p);
        assertEquals(SB, new BigInteger("81"));
    }

    @Test
    public void DHTestVectorSM() {
        BigInteger p = new BigInteger("467");
        BigInteger g = new BigInteger("57");

        BigInteger a = new BigInteger("32");
        BigInteger b = new BigInteger("27");

        BigInteger A = DiffieHellman.calculateSM(g, a, p);
        assertEquals(A, new BigInteger("371"));

        BigInteger B = DiffieHellman.calculateSM(g, b, p);
        assertEquals(B, new BigInteger("350"));

        BigInteger SA = DiffieHellman.calculateSM(B, a, p);
        assertEquals(SA, new BigInteger("81"));

        BigInteger SB = DiffieHellman.calculateSM(A, b, p);
        assertEquals(SB, new BigInteger("81"));
    }

    @Test
    public void powerIterSMTest() {
        BigInteger base = new BigInteger("4");
        BigInteger exp = new BigInteger("13");

        BigInteger result = DiffieHellman.powerIterSM(base, exp);
        assertEquals(result, new BigInteger("67108864"));
    }


}
