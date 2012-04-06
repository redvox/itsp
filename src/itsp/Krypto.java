package itsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Krypto {

	public static void main(String[] args){
	
//		System.out.println(cesar("Hallo du da", 4, true));
		System.out.println(cesar("wie geht es dir zzwzz a", 4, true));
		System.out.println(cesar(" midkilxdiwdhmvdcc ccde", 4, false));
	}
	
	public static String cesar(String text, int key, boolean verschlüsseln) {
		char[] alphabet = {' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z' };
		List<String> alphabetlist = new ArrayList<String>();
		for(int i=0; i<alphabet.length;i++){
			alphabetlist.add(""+alphabet[i]);
		}
		char[] eingabe = text.toCharArray();
		char[] ausgabe = new char[eingabe.length];

		if (verschlüsseln) {
			for (int i = 0; i < eingabe.length; i++) {
				ausgabe[i] = alphabet[(alphabetlist.indexOf(""+eingabe[i])+key)%alphabet.length];
			}
		} else {
			for (int i = 0; i < eingabe.length; i++) {
				ausgabe[i] = alphabet[(alphabetlist.indexOf(""+eingabe[i])+alphabet.length-key)%alphabet.length];
			}
		}
		
		return new String(ausgabe);
	}
}