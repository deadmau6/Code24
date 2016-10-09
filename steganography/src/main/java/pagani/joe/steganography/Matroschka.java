package pagani.joe.steganography;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by User on 10/9/2016.
 */

public class Matroschka {
    public byte[] encrypt(String data, String key) throws Exception
    {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");

        byte[] saltEncrypt = new byte[20];
        rand.nextBytes(saltEncrypt);
        byte[] derivedKey = getDerivedKey(key, saltEncrypt, 100000, 128);

        SecretKeySpec secretkeyspecs = new SecretKeySpec( derivedKey, "AES");
        Cipher cypher = Cipher.getInstance("AES/CTR/NoPadding");
        cypher.init(Cipher.ENCRYPT_MODE, secretkeyspecs, new IvParameterSpec(new byte[16]));
        byte[] cypherText = cypher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        byte[] HmacSalt = new byte[20];
        rand.nextBytes(HmacSalt);
        byte[] derivedHmacKey = getDerivedKey(key, saltEncrypt, 100000, 128);

        SecretKeySpec HmacSecretkeyspecs = new SecretKeySpec(derivedHmacKey, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(HmacSecretkeyspecs);
        byte[] hmac = mac.doFinal(cypherText);

        byte[] done = new byte[40 + cypherText.length + 32];
        System.arraycopy(saltEncrypt, 0, done, 0, 20);
        System.arraycopy(HmacSalt, 0, done, 20, 20);
        System.arraycopy(cypherText, 0, done, 40, cypherText.length);
        System.arraycopy(hmac, 0, done, 40+ cypherText.length, 32);

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
        //System.out.println("MESSAGE size = " + message.length);

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



    public Bitmap hideMessage(Bitmap image, byte[] encryptedMessage)
    {

        image = image.copy(Bitmap.Config.ARGB_8888,true);
        int x = 0;
        int y = 0;
        for(int b = 0; b < encryptedMessage.length; b = b + 3)
        {

            int firstColor;
            int secondColor;
            int thirdColor;

            if(encryptedMessage[b] < 0)
            {
                firstColor = encryptedMessage[b] + 256;
            }
            else if(encryptedMessage[b] > 0)
            {
                firstColor = encryptedMessage[b];
            }
            else
            {
                firstColor = 0;
            }


            if(b+1 < encryptedMessage.length)
            {
                if(encryptedMessage[b+1] < 0)
                {
                    secondColor = encryptedMessage[b+1] + 256;
                }
                else if(encryptedMessage[b+1] > 0)
                {
                    secondColor = encryptedMessage[b+1];
                }
                else
                {
                    secondColor = 0;
                }
            }
            else
            {
                secondColor = 0;
            }

            if(b+2 < encryptedMessage.length)
            {
                if(encryptedMessage[b+2] < 0)
                {
                    thirdColor = encryptedMessage[b+2] + 256;
                }
                else if(encryptedMessage[b+2] > 0)
                {
                    thirdColor = encryptedMessage[b+2];
                }
                else
                {
                    thirdColor = 0;
                }
            }
            else
            {
                thirdColor = 0;
            }

            Color rgb = new Color();

            System.out.println("Red = " + firstColor + " Green = " + secondColor + " Blue = " + thirdColor);
            image.setPixel(x, y, rgb.rgb(firstColor, secondColor, thirdColor));

            x++;

            if(x >= image.getWidth())
            {
                y++;
                x=0;
            }
        }

        x = image.getWidth()-1;
        y = image.getHeight()-1;

        String s = Integer.toString(encryptedMessage.length);
        byte[] sizeAsBytes = s.getBytes();
        for(byte b: sizeAsBytes)
        {
            if(b < 0)
            {
                //System.out.println("set image neg " + encryptedMessage[b]);
                Color rgb = new Color();

                image.setPixel(x, y, rgb.rgb(b + 256, 0, 0));
            }
            else
            {
                //System.out.println("set image pos " + encryptedMessage[b]);
                Color rgb = new Color();
                image.setPixel(x, y, rgb.rgb(b, 0, 0));
            }

            x--;
        }

        Color rgb = new Color();
        image.setPixel(x, y, rgb.rgb(0, 1, 1));
        return image;
    }



    public byte[] getMessageFromImage(Bitmap image)
    {
        int x = image.getWidth()-1;
        int y = image.getHeight()-1;
        int byteSize = 0;
        Color c = new Color();
        int currentColor = image.getPixel(x,y);
        while(c.green(currentColor)== 0 && c.blue(currentColor)== 0)
        {
            byteSize++;
            x--;
            currentColor = image.getPixel(x,y);
        }
        x = image.getWidth()-1;
        byte[] size = new byte[byteSize];

        for(int u = 0; u < size.length; u++)
        {
            currentColor = image.getPixel(x - u,y);
            int val = c.red(currentColor);
            if(val > 127)
            {
                val = val-256;
            }
            size[u] = Byte.parseByte(Integer.toString(val));
        }
        int numBytes = Integer.parseInt(new String(size));

        x = 0;
        y = 0;
        byte[] encryptedMessage = new byte[numBytes];
        for(int i = 0; i < encryptedMessage.length; i++)
        {
            currentColor = image.getPixel(x,y);
            int byteVal;
            switch(i % 3)
            {
                case 0:
                    byteVal = c.red(currentColor);
                    if(byteVal > 127)
                    {
                        byteVal = byteVal-256;
                    }
                    encryptedMessage[i] = Byte.parseByte(Integer.toString(byteVal));
                    break;
                case 1:
                    byteVal = c.green(currentColor);
                    if(byteVal > 127)
                    {
                        byteVal = byteVal-256;
                    }
                    encryptedMessage[i] = Byte.parseByte(Integer.toString(byteVal));
                    break;
                case 2:
                    byteVal = c.blue(currentColor);
                    if(byteVal > 127)
                    {
                        byteVal = byteVal-256;
                    }
                    encryptedMessage[i] = Byte.parseByte(Integer.toString(byteVal));
                    x++;
                    break;
            }

            if(x >= image.getWidth())
            {
                x = 0;
                y ++;
            }
        }

        return encryptedMessage;
    }
}
