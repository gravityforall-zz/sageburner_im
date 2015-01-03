package com.sageburner.im.android.jpbc;

public class IBETest {
	public static void main(String[] args) {

		// Init the generator... (setup() called implicitly via the IBE constructor)
		IBE ibe = new IBE();

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
