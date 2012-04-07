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
    public void CaesarTest() {
        for (Integer key : Iterables.toIterable(PrimitiveGenerators.integers(0, 255))) {
            for (String cleartext : Iterables.toIterable(PrimitiveGenerators.strings())) {
                // encrypt
                String cyphertext = Krypto.cesar(cleartext, key, true);

                // decrypt
                String result = Krypto.cesar(cyphertext, key, false);
                assertEquals(cleartext, result);
            }
        }
    }

    @Test
    public void SkytaleTest() {
        for (Integer key : Iterables.toIterable(PrimitiveGenerators.integers(0, 100))) {
            for (String cleartext : Iterables.toIterable(PrimitiveGenerators.strings())) {
                // encrypt
                String cyphertext = Krypto.skytale(cleartext, key, true);

                // decrypt
                String result = Krypto.skytale(cyphertext, key, false);
                assertEquals(cleartext, result);
            }
        }
    }
}
