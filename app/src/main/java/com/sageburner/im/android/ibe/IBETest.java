package com.sageburner.im.android.ibe;

public class IBETest {
	public static void main(String[] args) {

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
}
