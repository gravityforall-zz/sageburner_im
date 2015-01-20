package com.sageburner.im.android.ibe;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Takes a Facebook ID and a plaintext message and prints out the encrypted
 * message
 * 
 * @author John
 * 
 */

public class IBEencrypt {
	public static void main(String[] args) {

		// get FRIEND'S facebook ID
		String facebookID = args[0];
		String msg = "";
		// get the plaintext message
		for (int i = 1; i < args.length; i++) {
			msg += args[i] + " ";
		}
		msg = msg.substring(0, msg.length() - 1);
		// System.out.println()

		// Turn off print statements (turn off the System.out output stream)
		PrintStream out = System.out;
		System.setOut(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		}));
		String encMsg = "weak";

		String paramsString = "type a\n" +
				"q 15395144596410194588212526809239258288053698882689112700879123127308216454058624125169848218156001486832831439076222867742441002432159967257568952165990471\n" +
				"r 1461501637330902918201208952637712259106134294527\n" +
				"h 10533785391117241877291560136702432360038107142333604139416314716198370432552752255793622979018968457412536\n" +
				"exp1 91\n" +
				"exp2 160\n" +
				"sign0 -1\n" +
				"sign1 -1";

		String pByteString = "ARkDcGf8U6FrIdEYh6jIXcq4GqIImORbifkdNY0eRpY" +
				"FZGa8IItN9XrQlanyPndcf2iQofbKZ2ZaPXEmIv+jp" +
				"c8A7v8MKpoZkKRg8aF8gcjO29NDm8XJ4v4tcm0em/9" +
				"f1F/wHxXdpCm5ZlqC1QDE9kxbtD1Owgj5VHbqv0h922GecA==";

		String sByteString = "g9pi2mwJjfOYuHunFObMgFwFVbk=";

		try {
			// encrypt the message with the friend's Facebook ID
			IBE ibe = new IBE(paramsString, pByteString, sByteString);

			String pub = ibe.getPubStr(facebookID);
			encMsg = ibe.getEncFromPub(msg, pub);

		} finally {
			System.setOut(out);
		}
		System.out.println(encMsg);
	}
}
