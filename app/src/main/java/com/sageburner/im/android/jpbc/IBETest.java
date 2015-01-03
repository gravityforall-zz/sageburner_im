package com.sageburner.im.android.jpbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
