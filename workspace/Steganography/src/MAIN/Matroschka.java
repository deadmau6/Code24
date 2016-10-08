package MAIN;

import java.awt.Image;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Matroschka {
	public byte[] encrypt(String data, String key) throws Exception
	{
		SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
		
		byte[] saltEncrypt = new byte[20];
		rand.nextBytes(saltEncrypt);
		byte[] derivedKey = getDerivedKey(key, saltEncrypt, 100000, 128);
		
		System.out.println("salt done");
		
		SecretKeySpec secretkeyspecs = new SecretKeySpec( derivedKey, "AES");
		Cipher cypher = Cipher.getInstance("AES/CTR/NoPadding");
		cypher.init(Cipher.ENCRYPT_MODE, secretkeyspecs, new IvParameterSpec(new byte[16]));
		byte[] cypherText = cypher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		
		System.out.println("encrpyted");
		
		byte[] HmacSalt = new byte[20];
		rand.nextBytes(HmacSalt);
		byte[] derivedHmacKey = getDerivedKey(key, saltEncrypt, 100000, 128);
		
		System.out.println("hmac salt");
		
		SecretKeySpec HmacSecretkeyspecs = new SecretKeySpec(derivedHmacKey, "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(HmacSecretkeyspecs);
		byte[] hmac = mac.doFinal(cypherText);
		
		System.out.println("hmacced");
		
		byte[] done = new byte[40 + cypherText.length + 32];
		System.arraycopy(saltEncrypt, 0, done, 0, 20);
		System.arraycopy(HmacSalt, 0, done, 20, 20);
		System.arraycopy(cypherText, 0, done, 40, cypherText.length);
		System.arraycopy(hmac, 0, done, 40+ cypherText.length, 32);
		
		System.out.println("size = " + done.length);
		
		return done;
	}
	
	public byte[] getDerivedKey(String key, byte[] salt, int size, int bits) throws Exception
	{
		PBEKeySpec k = new PBEKeySpec(key.toCharArray(), salt, size, bits);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		return factory.generateSecret(k).getEncoded();
	}
	
	public String decrypt(byte[] message, String key) throws Exception
	{
		System.out.println("MESSAGE size = " + message.length);
		
		byte[] saltEncrypt = Arrays.copyOfRange(message, 0, 20);
		byte[] HmacSalt = Arrays.copyOfRange(message, 20, 20);
		byte[] cypherText = Arrays.copyOfRange(message, 40, message.length-32);
		byte[] hmac = Arrays.copyOfRange(message, message.length - 32, message.length);
		
		byte[] derivedHmacKey = getDerivedKey(key, hmac, 100000, 160);
		SecretKeySpec HmacSecretkeyspecs = new SecretKeySpec(derivedHmacKey, "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(HmacSecretkeyspecs);
		byte[] unMaced = mac.doFinal(cypherText);
		
		byte[] derivedKey = getDerivedKey(key, saltEncrypt, 100000, 128);
		SecretKeySpec secretkeyspecs = new SecretKeySpec( derivedKey, "AES");
		Cipher cypher = Cipher.getInstance("AES/CTR/NoPadding");
		cypher.init(Cipher.DECRYPT_MODE, secretkeyspecs, new IvParameterSpec(new byte[16]));
		byte[] unEncrypted = cypher.doFinal(cypherText);
		
		String finalMessage = new String(unEncrypted, StandardCharsets.UTF_8);
		
		return finalMessage;
	}
	
	
	public void hideMessage(Image image, byte[] encryptedMessage)
	{
		
	}
	/*
	public byte[] getMessageFromImage(Image image)
	{
		return new byte[] happiness;
	}
	*/
	
}
