package itsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Krypto {

	public static void main(String[] args) {

		// System.out.println(caesar("Hallo du da", 4, true));
		System.out.println(caesar("wie geht es dir zzwzz a", 4, true));
		System.out.println(caesar(" midkilxdiwdhmvdcc ccde", 4, false));

		System.out.println(skytale("hallo wie geht es dir", 4, true));
		System.out.println(skytale("hwhdaitile rl e ogs  e  ", 4, false));

		analyse("Hallo wie geht es dir eigentlich so");
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

	public static String skytale(String text, int key, boolean encrypt) {
		char[] eingabe = text.toCharArray();
		int mod = eingabe.length;

		StringBuffer ausgabe = new StringBuffer();
		int start = 0;

		int b = 0;
		if (encrypt) {
			while (ausgabe.length() != eingabe.length) {
				ausgabe.append(eingabe[b]);
				b = (b + key) % mod;
			}
		} else {
			while (ausgabe.length() != eingabe.length) {
				ausgabe.append(eingabe[b]);
				b = (b + key) % mod;
			}
		}

		return "" + ausgabe;
	}

	public static void analyse(String text) {
		char[] eingabe = text.toCharArray();
		Map<String, Integer> haufigkeit = new HashMap<String, Integer>();

		for (int i = 0; i < eingabe.length; i++) {
			if (haufigkeit.containsKey("" + eingabe[i])) {
				haufigkeit.put("" + eingabe[i], haufigkeit.get("" + eingabe[i]) + 1);
			} else {
				haufigkeit.put("" + eingabe[i], 1);
			}
		}

		for (String s : haufigkeit.keySet()) {
			System.out.println(s + " -> " + haufigkeit.get(s));
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