package itsp3;

import java.io.*;
import java.util.Arrays;

public class Mac {

    // Returns the contents of the file in a byte array.
    public static byte[] getBytesFromFile(File file, byte[] prefix, int blockSize) throws IOException {
        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new IOException("File is too large!");
        }

        // Create the byte array to hold the data

        long size = length + prefix.length;
        long paddingBytes = size % blockSize;
        size += paddingBytes;

        byte[] bytes = new byte[(int)size];

        // adding prefix
        for (int n = 0; n < prefix.length; n++) {
            bytes[n] = prefix[n];
        }

        // Read in the bytes
        int offset = prefix.length;
        int numRead = 0;

        InputStream is = new FileInputStream(file);
        try {
            while ((offset) < (bytes.length - paddingBytes)
                    && (numRead=is.read(bytes, offset, (int) (bytes.length-offset-paddingBytes))) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }

        Arrays.fill(bytes, (int)bytes.length - (int)paddingBytes, bytes.length, (byte)0);

        // Ensure all the bytes have been read in
        if (offset + paddingBytes < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
        return bytes;
    }

	public static void main(String[] args) {
    /*
		p(secretPreMac("00000000000000000000000000000000",
				"/home/oldvox/Dropbox/Code/itsp/text.txt"));
		p(secretPreMac("00000000000000000000000000000000",
				"/home/oldvox/Dropbox/Code/itsp/text.txt"));
		p(secretPreMac("00000000000000000000000000000000",
				"/home/oldvox/Dropbox/Code/itsp/text.txt"));
    */
	}

    // blocksize = 16
	public static String secretPreMac(String hexkey, String filename, int blocksize) {
		StringBuffer intext = new StringBuffer();
        byte[] intextArray = null;
		try {
            byte[] prefix = hexStringToByteArray(hexkey);
            intextArray = getBytesFromFile(new File(filename), prefix, blocksize);
			/*BufferedReader in = new BufferedReader(new FileReader(filename));
			String str;
			while ((str = in.readLine()) != null) {
				intext.append(str);
			}
			in.close();*/
		} catch (IOException e) {
			e.printStackTrace();
		}

		//String initVector = "0000000000000000";
		int macRounds = intextArray.length / blocksize;
		p("Rounds: " + macRounds);

		//byte[] intextArray = intext.toString().getBytes();
		byte[] initVectorArray = new byte[blocksize]; //initVector.getBytes();
        Arrays.fill(initVectorArray, (byte)0);
		p("Lange "+initVectorArray.length);
		byte[] tmp = new byte[blocksize];

		for (int j = 0; j < blocksize; j++) {
			tmp[j] = (byte) (intextArray[j] ^ initVectorArray[j]);
		}

        //p("0. tmp = " + ByteArrayToHexString(tmp));
		for (int i = 1; i < macRounds; i++) {
			for (int k = 0; k < blocksize; k++) {
				tmp[k] = (byte) (tmp[k] ^ intextArray[(i * blocksize) + k]);
			}
            //p((i+1) + ". tmp = " + ByteArrayToHexString(tmp));
		}

		// finanisieren?

        return ByteArrayToHexString(tmp);
	}

	public static void macBruteForce(int leadingZeros, String filename,
			String macValue) {
		// Ausgabe
		// Anzahl möglicher Schlüssel
		// Anzahl getesteter Schlüssel
		// Geheimer Schlüssel in Hexadezimalformat
		// Laufzeit

	}

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

	// Max ist 7F (127).
	public static byte[] toByteArray(String hexString) {
		int numberChars = hexString.length();
		byte[] bytes = new byte[numberChars / 2];
		for (int i = 0; i < numberChars-1; i += 2) {
			bytes[i / 2] = Byte.parseByte(hexString.substring(i, i+2), 16);
		}
		return bytes;
	}

    public static String ByteArrayToHexString(byte byteArray[]) {
        String strArray = new String();
        strArray = "";

        for (int x=0; x < byteArray.length; x++) {
            int b = ((int)byteArray[x] & 0x000000ff);
            if (b < 16) {
                strArray = strArray + "0" + Integer.toHexString(b).toUpperCase();
            }
            else  {
                strArray = strArray + Integer.toHexString(b).toUpperCase();
            }
        }
        return strArray;
    }

	public static void p(String s) {
		System.err.println(s);
	}
}
