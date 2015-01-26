package com.sageburner.im.android.jpbc;

import com.sageburner.im.android.ibe.IBE;
import com.sageburner.im.android.ibe.IBEParams;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class IBETest {

	@Test
	public void testEncryptDecrypt() {

		IBEParams ibeParams = new IBEParams();
		ibeParams.setParamsString("type a\n" +
				"q 15395144596410194588212526809239258288053698882689112700879123127308216454058624125169848218156001486832831439076222867742441002432159967257568952165990471\n" +
				"r 1461501637330902918201208952637712259106134294527\n" +
				"h 10533785391117241877291560136702432360038107142333604139416314716198370432552752255793622979018968457412536\n" +
				"exp1 91\n" +
				"exp2 160\n" +
				"sign0 -1\n" +
				"sign1 -1");

		ibeParams.setpByteString("ARkDcGf8U6FrIdEYh6jIXcq4GqIImORbifkdNY0eRpY" +
				"FZGa8IItN9XrQlanyPndcf2iQofbKZ2ZaPXEmIv+jp" +
				"c8A7v8MKpoZkKRg8aF8gcjO29NDm8XJ4v4tcm0em/9" +
				"f1F/wHxXdpCm5ZlqC1QDE9kxbtD1Owgj5VHbqv0h922GecA==");

		ibeParams.setsByteString("g9pi2mwJjfOYuHunFObMgFwFVbk=");

		// Init the generator... (setup() called implicitly via the IBE constructor)
		IBE ibe = new IBE(ibeParams.getParamsString(), ibeParams.getpByteString(), ibeParams.getsByteString());

		String message = "weak";
		System.out.println("Message: " + message);

		String userID = "bunPuncher69";
		System.out.println("userID: " + userID);

		// Encryption:
		System.out.println("Encryption.....");
		String encMsg = ibe.getEncFromID(message, userID);

		// Decryption:
		System.out.println("Decryption.....");
		String decMsg = ibe.getDecFromID(encMsg, userID);

		System.out.println("Decoded Message: " + decMsg);
	}

	@Test
	public void testDecrypt() {

		// get USER'S OWN facebook ID
		String facebookID = "";
		String encMsg = "";
		// System.out.println(encMsg);

		// Turn off print statements (turn off the System.out output stream)
		PrintStream out = System.out;
		System.setOut(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		}));
		String decMsg = "weak";

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

			String priv = ibe.getPrivStr(facebookID);

			// Have to call this just to initialize some objects, just ignore
			// result
			ibe.getEncFromID("you are weak", "test");

			decMsg = ibe.getDecFromPriv(encMsg, priv);
		} finally {
			System.setOut(out);
		}
		System.out.println(decMsg);
	}

	@Test
	public void testEncrypt() {

		// get FRIEND'S facebook ID
		String facebookID = "";
		String msg = "";
		// get the plaintext message
//		for (int i = 1; i < args.length; i++) {
//			msg += args[i] + " ";
//		}
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
