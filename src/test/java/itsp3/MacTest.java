package test.java.itsp3;

import itsp3.Mac;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MacTest {

    @Test
    public void hexStringToBytesTest() {
        byte[] expected = new byte[4];
        expected[0] = 123;
        expected[1] = (byte)231;
        expected[2] = 23;
        expected[3] = 97;

        String test = "7BE71761";

        byte[] actual = Mac.hexStringToByteArray(test);
        assertEquals(actual, expected);
    }

    @Test
    public void secretPrefixTest() {
        String prefix = "0B";

        String expected = "78";
        String actual = Mac.secretPreMac(prefix, "/Users/tobias/dev/rep/itsp/testfile.bin", 1);
        assertEquals(actual, expected);
    }
}
