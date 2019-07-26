package com.moptra.go4wealth.prs.common.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import com.moptra.go4wealth.prs.common.constant.GoForWealthPRSConstants;


/*
 * PBKDF2 salted password hashing.
 * Author: havoc AT defuse.ca
 * www: http://crackstation.net/hashing-security.htm
 */

@Component
public class EncryptUserDetail {
	
	private final String PBKDF2_ALGORITHM = GoForWealthPRSConstants.PBKDF2_ALGORITHM;
	private final String PRIVATE_kEY = GoForWealthPRSConstants.PRIVATE_KEY;

    // The following constants may be changed without breaking existing hashes.
    public static final int SALT_BYTES = GoForWealthPRSConstants.SALT_BYTES;
    public static final int HASH_BYTES = GoForWealthPRSConstants.HASH_BYTES;
    public static final int PBKDF2_ITERATIONS = GoForWealthPRSConstants.PBKDF2_ITERATIONS;
    public static final int ITERATION_INDEX = GoForWealthPRSConstants.ITERATION_INDEX;
    public static final int SALT_INDEX = GoForWealthPRSConstants.SALT_INDEX;
    public static final int PBKDF2_INDEX = GoForWealthPRSConstants.PBKDF2_INDEX;

    
    /**
     * 
     * @param word
     * @return
     * @throws Exception
     */
    public String encrypt(String word) throws Exception {
    	byte[] ivBytes;
        String privateKey = PRIVATE_kEY;
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        byte[] saltBytes = bytes;
        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        PBEKeySpec spec = new PBEKeySpec(privateKey.toCharArray(),saltBytes,65556,128);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        //encrypting the word
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        ivBytes =   params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedTextBytes = cipher.doFinal(word.getBytes("UTF-8"));
        //prepend salt and vi
        byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
        System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
        System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
        System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);
        String encryptedValue = new Base64().encodeToString(buffer);
        return encryptedValue;
    }
    
    /**
     * 
     * @param encryptedText
     * @return
     * @throws Exception
     */
    public String decrypt(String encryptedText) throws Exception {	
    	String privateKey = PRIVATE_kEY;
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	//strip off the salt and iv
    	ByteBuffer buffer = ByteBuffer.wrap(new Base64().decode(encryptedText));
    	byte[] saltBytes = new byte[20];
    	buffer.get(saltBytes, 0, saltBytes.length);
    	byte[] ivBytes1 = new byte[cipher.getBlockSize()];
    	buffer.get(ivBytes1, 0, ivBytes1.length);
    	byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes1.length];
    	buffer.get(encryptedTextBytes);
    	// Deriving the key
    	SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
    	PBEKeySpec spec = new PBEKeySpec(privateKey.toCharArray(), saltBytes, 65556,128);
    	SecretKey secretKey = factory.generateSecret(spec);
    	SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
    	cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes1));
    	byte[] decryptedTextBytes = null;
    	try {
    		decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
    	} catch (IllegalBlockSizeException e) {
    		e.printStackTrace();
    	} catch (BadPaddingException e) {
    		e.printStackTrace();
    	}
    	return new String(decryptedTextBytes);
    }
    
    
    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param   password    the password to hash
     * @return              a salted PBKDF2 hash of the password
     */
    
    public  String createHash(String password)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return createHash(password.toCharArray());
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param   password    the password to hash
     * @return              a salted PBKDF2 hash of the password
     */
    public  String createHash(char[] password)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);

        // Hash the password
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES);
        // format iterations:salt:hash
        return toHex(salt) + ":" +  toHex(hash);
    }

    /**
     * Validates a password using a hash.
     *
     * @param   password    the password to check
     * @param   goodHash    the hash of the valid password
     * @return              true if the password is correct, false if not
     */
    public  boolean validatePassword(String password, String salt1, String orgPass)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return validatePassword(password.toCharArray(), salt1, orgPass );
    }

    /**
     * Validates a password using a hash.
     *
     * @param   password    the password to check
     * @param   goodHash    the hash of the valid password
     * @return              true if the password is correct, false if not
     */
    public  boolean validatePassword(char[] password, String salt1, String orgPass)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Decode the hash into its parameters
    	
    	byte[] salt = fromHex(salt1);
        byte[] oldPass = fromHex(orgPass);
        
        // Compute the hash of the provided password, using the same salt, 
        // iteration count, and hash length
        
        byte[] currentPass = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES);
        
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        
        return slowEquals(oldPass, currentPass);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line 
     * system using a timing attack and then attacked off-line.
     * 
     * @param   a       the first byte array
     * @param   b       the second byte array 
     * @return          true if both byte arrays are the same, false if not
     */
    private  boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    /**
     *  Computes the PBKDF2 hash of a password.
     *
     * @param   password    the password to hash.
     * @param   salt        the salt
     * @param   iterations  the iteration count (slowness factor)
     * @param   bytes       the length of the hash to compute in bytes
     * @return              the PBDKF2 hash of the password
     */
    private  byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param   hex         the hex string
     * @return              the hex string decoded into a byte array
     */
    private  byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param   array       the byte array to convert
     * @return              a length*2 character string encoding the byte array
     */
    private  String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) 
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

}