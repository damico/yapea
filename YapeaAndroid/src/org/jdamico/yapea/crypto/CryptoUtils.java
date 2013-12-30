package org.jdamico.yapea.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.jdamico.yapea.commons.AppMessages;
import org.jdamico.yapea.commons.Constants;
import org.jdamico.yapea.commons.Utils;
import org.jdamico.yapea.commons.YapeaException;
import org.jdamico.yapea.dataobjects.CryptoAlgoObj;

import android.content.Context;

public class CryptoUtils {

	private static CryptoUtils INSTANCE = null;

	private CryptoUtils(){}

	public static CryptoUtils getInstance(){
		if(null == INSTANCE) INSTANCE = new CryptoUtils();
		return INSTANCE;
	}


	public byte[] pbkdf2(char[] password, byte[] salt, int iterationCount, int keyLength) throws YapeaException {

		byte[] ret = null;

		try {
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
			try {
				SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
				ret =  secretKey.getEncoded();
			} catch (InvalidKeySpecException ikse) {
				throw new YapeaException(ikse);
			}
		} catch (NoSuchAlgorithmException nsae) {
			throw new YapeaException(nsae);
		}

		return ret;
	}

	public byte[] getKeyHash(Context context, char[] passwd) throws YapeaException{
		byte[] salt = getSalt(context);
		return pbkdf2(passwd, salt, salt.length, Constants.PBKDF2_KEY_LENGTH);
	}

	public byte[] getSalt(Context context){
		String imei = Utils.getInstance().getIMEI(context);
		if(imei != null) return imei.getBytes();
		else return Utils.getInstance().getDeviceData().getBytes();

	}

	public byte[] enc(Context context, String password, byte[] plainContent, String algo) throws YapeaException{

		SecretKeyFactory factory = null;
		byte[] cipherContent = null;
		byte[] salt = getSalt(context);
		CryptoAlgoObj cryptoObj = getCryptoAlgoObjByAlgo(algo);
		
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, salt.length, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), algo); //AES
			Cipher cipher = Cipher.getInstance(cryptoObj.getAlgoInstance()); //"AES/CBC/PKCS5Padding"
			byte[] iv = normalizeIvByteArray(Utils.getInstance().getDeviceData().getBytes(), cryptoObj.getIvLength()); 
			cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));
			cipherContent = cipher.doFinal(plainContent);
		} catch (NoSuchAlgorithmException e) {
			throw new YapeaException(e);
		} catch (InvalidKeySpecException e) {
			throw new YapeaException(e);
		} catch (NoSuchPaddingException e) {
			throw new YapeaException(e);
		} catch (InvalidKeyException e) {
			throw new YapeaException(e);
		} catch (IllegalBlockSizeException e) {
			throw new YapeaException(e);
		} catch (BadPaddingException e) {
			throw new YapeaException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new YapeaException(e);
		}

		return cipherContent;


	}

	public byte[] dec(Context context, String password, byte[] cipherContent, String algo) throws YapeaException {

		SecretKeyFactory factory = null;
		byte[] plainContent = null;
		byte[] salt = getSalt(context);
		
		CryptoAlgoObj cryptoObj = getCryptoAlgoObjByAlgo(algo);
		
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, salt.length, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), algo); //AES
			Cipher cipher = Cipher.getInstance(cryptoObj.getAlgoInstance()); //"AES/CBC/PKCS5Padding"
			byte[] iv = normalizeIvByteArray(Utils.getInstance().getDeviceData().getBytes(), cryptoObj.getIvLength()); 
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
			plainContent = cipher.doFinal(cipherContent);

		} catch (NoSuchAlgorithmException e) {
			throw new YapeaException(e);
		} catch (InvalidKeySpecException e) {
			throw new YapeaException(e);
		} catch (NoSuchPaddingException e) {
			throw new YapeaException(e);
		} catch (InvalidKeyException e) {
			throw new YapeaException(e);
		} catch (IllegalBlockSizeException e) {
			throw new YapeaException(e);
		} catch (BadPaddingException e) {
			throw new YapeaException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new YapeaException(e);
		}

		return plainContent;
	}

	

	
	
	private byte[] normalizeIvByteArray(byte[] notNormalized, int length) throws YapeaException{
		byte[] iv = new byte[length];

		if(notNormalized != null){

			if(notNormalized.length > length){
				for (int i = 0; i < iv.length; i++) {
					iv[i] = notNormalized[i];
				}
			}else if(notNormalized.length < length){

				for (int i = 0; i < iv.length; i++) {
					if(notNormalized.length != i+1) iv[i] = notNormalized[i];
					else{
						Random generator = new Random(); 
						int r = generator.nextInt(127) + 1;
						if(i % 2 != 0) r = r * -1;
						iv[i] = (byte) r;
					}
				}

			}else iv = notNormalized;
		} else throw new YapeaException(AppMessages.getInstance().getMessage("CryptoUtils.normalizeIvByteArray.nullSource"));
		return iv;
	}
	
	private CryptoAlgoObj getCryptoAlgoObjByAlgo(String algo){
		CryptoAlgoObj cryptoAlgo = null;
		if(!algo.equalsIgnoreCase("AES")) cryptoAlgo = new CryptoAlgoObj(algo, "Blowfish/CFB/NoPadding", 8);
		else cryptoAlgo = new CryptoAlgoObj(algo, "AES/CBC/PKCS5Padding", 16);
		return cryptoAlgo;
	}
	
}
