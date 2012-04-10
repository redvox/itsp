package itsp;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.*;

public class Krypto {

    public static Charset charset = Charset.forName("ISO-8859-1");
    public static CharsetEncoder encoder = charset.newEncoder();
    public static CharsetDecoder decoder = charset.newDecoder();

    public static String fileToString(String path) {
        try {
            FileInputStream stream = new FileInputStream(new File(path));
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            stream.close();
            return new String(decoder.decode(bb).array());
        }
        catch (Exception e) {
            return "";
        }
    }

    public static void stringToFile(String path, String content) {
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(path), "ISO-8859-1");
            BufferedWriter out = new BufferedWriter(w);
            out.write(content.toCharArray());
            out.close();

        } catch (Exception e) {
            System.out.println("Failed to write contents to file.");
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
        if (args[0].equals("caesar")) {
            stringToFile(args[4], caesar(fileToString((args[1])), Integer.parseInt(args[3]), Boolean.parseBoolean(args[2])));
        }
        else if (args[0].equals("skytale")) {
            stringToFile(args[4], skytale(fileToString((args[1])), Integer.parseInt(args[3]), Boolean.parseBoolean(args[2])));
        }
        else if (args[0].equals("analyse")){
            analyseFile(args[1]);
        }
        else {
            System.out.println("BEEP");
        }
	}

	public static String caesar(String text, int key, boolean encrypt) {
		char[] eingabe = text.toCharArray();
		char[] ausgabe = new char[eingabe.length];

		if (encrypt) {
			for (int i = 0; i < eingabe.length; i++) {
				ausgabe[i] = (char) ((eingabe[i] + key) % 256);
			}
		} else {
			for (int i = 0; i < eingabe.length; i++) {
                ausgabe[i] = (char) ((256 + eingabe[i] - key) % 256);
			}
		}

		return new String(ausgabe);
	}

	public static String skytale(String inputStr, int key, boolean encrypt) {
        if (inputStr.isEmpty()) return new String();

        char[] input = inputStr.toCharArray();
        int outputSize = (int) (Math.ceil((double)input.length / (double)key)) * key;
        char[] output = new char[outputSize];

        if (encrypt) {
            for (int i = 0; i < input.length; i++) {
                int posT = (i * key + (int)Math.abs(i * (double)key / (double)outputSize)) % outputSize;
                output[posT] = input[i];
            }
        } else {
            int width = (int) Math.ceil(input.length / key);
            for (int i = 0; i < input.length; i++) {
                int posT = (i % key) * width + (int)Math.abs(i / (double)key);
                output[posT] = input[i];
            }
        }
        return new String(output);
	}

	public static void analyse(String text) {
		char[] eingabe = text.toCharArray();
		Map<String, Integer> haufigkeit = new HashMap<String, Integer>();
        int charAmount = 0;

		for (int i = 0; i < eingabe.length; i++) {
			if (haufigkeit.containsKey("" + eingabe[i])) {
				haufigkeit.put("" + eingabe[i], haufigkeit.get("" + eingabe[i]) + 1);
			} else {
				haufigkeit.put("" + eingabe[i], 1);
			}
            charAmount++;
		}

        List <Map.Entry<String, Integer>> results = new ArrayList<Map.Entry<String, Integer>>();

		for (Map.Entry<String, Integer> e : haufigkeit.entrySet()) {
            results.add(e);
		}

        Collections.sort(results, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> stringIntegerEntry1) {
                return -stringIntegerEntry.getValue().compareTo(stringIntegerEntry1.getValue());
            }
        });

        for (Map.Entry<String, Integer> e : results) {
            System.out.printf("[ %s ] -> %d ( %.2f )\n", e.getKey(), e.getValue(), (e.getValue()/(double)charAmount * 100));
        }
	}

	public static void analyseFile(String file) {

		BufferedReader reader;
		StringBuffer content = new StringBuffer();;
		try {
			reader = new BufferedReader(new FileReader(file));
			String s = null;

			while ((s = reader.readLine()) != null) {
				content.append(s).append(System.getProperty("line.separator"));
			}			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		analyse(""+content);
	}
}