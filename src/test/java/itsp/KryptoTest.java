package itsp;

import static net.java.quickcheck.generator.CombinedGeneratorsIterables.someLists;
import static net.java.quickcheck.generator.PrimitiveGenerators.integers;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.java.quickcheck.generator.PrimitiveGenerators;
import net.java.quickcheck.generator.iterable.Iterables;
import org.testng.annotations.Test;

public class KryptoTest {

    @Test
    public void CaesarTestVector() {
        String cleartext = "IJKLMN";
        String expectedCyphertext = "LMNOPQ";

        assertEquals(expectedCyphertext, Krypto.caesar(cleartext, 3, true));
        assertEquals(cleartext, Krypto.caesar(expectedCyphertext, 3, false));
    }


    @Test
    public void CaesarTest() {
        for (Integer key : Iterables.toIterable(PrimitiveGenerators.integers(0, 255))) {
            for (String cleartext : Iterables.toIterable(PrimitiveGenerators.strings())) {
                // encrypt
                String cyphertext = Krypto.caesar(cleartext, key, true);

                // decrypt
                String result = Krypto.caesar(cyphertext, key, false);
                assertEquals(cleartext, result);
            }
        }
    }

    @Test
    public void SkytaleTestVectors() {
        String cleartext = "TROOPSHEADINGWESTNEEDMORESUPPLIESSENDGENERALDUBOISMENTOAID";
        assertEquals(Krypto.skytale(cleartext, 1, true), cleartext);

        cleartext = "HALLO";
        assertEquals(Krypto.skytale(cleartext, 2, true).trim(), "HLAOL ".trim());
    }

    @Test
    public void SkytaleTest() {
        for (Integer key : Iterables.toIterable(PrimitiveGenerators.integers(1, 100))) {
            for (String cleartext : Iterables.toIterable(PrimitiveGenerators.strings())) {
                // encrypt
                String cyphertext = Krypto.skytale(cleartext, key, true);

                // decrypt
                String result = Krypto.skytale(cyphertext, key, false);
                assertEquals(cleartext.trim(), result.trim());
            }
        }
    }
}
