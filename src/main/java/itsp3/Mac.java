package main.java.itsp3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Mac {

	public static void main(String[] args) {

		p(secretPreMac("0000000000000000", "/home/oldvox/Dropbox/Code/itsp/text.txt"));
		p(secretPreMac("0101111100000000", "/home/oldvox/Dropbox/Code/itsp/text.txt"));
		p(secretPreMac("1111111111111111", "/home/oldvox/Dropbox/Code/itsp/text.txt"));
	}

	public static String secretPreMac(String hexkey, String filename) {	
		StringBuffer intext = new StringBuffer();
		
		try {
		    BufferedReader in = new BufferedReader(new FileReader(filename));
		    String str;
		    while ((str = in.readLine()) != null) {
		        intext.append(str);
		    }
		    in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int padding = intext.length() % 16;
		p("Padding: "+padding);
		while(padding > 0){
			intext.append("0");
			padding--;
		}
		
		// Secret Präfix? Der Inizialvekor könnte dann der Schlüssel sein.
//		String initVector = "0000000000000000";
		String initVector = hexkey;
		int macRounds = intext.length() / 16;
		p("Rounds: "+macRounds);
		
		byte[] intextArray = intext.toString().getBytes();
		byte[] initVectorArray = initVector.getBytes(); 
		byte[] tmp = new byte[16];
		
		for(int j=0; j < 16; j++){
			tmp[j] = (byte) (intextArray[j] ^ initVectorArray[j]);
		}		
		
		for(int i=0; i <= macRounds; i++){
			for(int k=0; k < 16; k++){
				tmp[k] = (byte) (tmp[k] ^ intextArray[(i*16)+k]);
			}
		}
		
		// finanisieren?
		
//		return new String(tmp);
		
		StringBuffer mac = new StringBuffer();
		for(int j=0; j < 16; j++){
			mac.append(String.valueOf(tmp[j]));
		}
		return mac.toString();
	}
	
	public static void p(String s){
		System.out.println(s);
	}
}
