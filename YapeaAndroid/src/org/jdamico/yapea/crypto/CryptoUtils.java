package org.jdamico.yapea.crypto;

/*
 * This file is part of YAPEA.
 * 
 *    YAPEA is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License (version 2) 
 *    as published by the Free Software Foundation.
 *
 *    YAPEA is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with YAPEA.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
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
import org.jdamico.yapea.commons.StaticObj;
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

	private SecretKey genSecretKey(Context context, String password, String algo) throws NoSuchAlgorithmException, InvalidKeySpecException{

		byte[] salt = getSalt(context);
		SecretKeyFactory	factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, salt.length, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), algo); //AES

		return secret;
	}

	public byte[] enc(Context context, String password, byte[] plainContent, String algo) throws YapeaException{

		byte[] cipherContent = null;
		CryptoAlgoObj cryptoObj = getCryptoAlgoObjByAlgo(algo);

		try {
			Cipher cipher = Cipher.getInstance(cryptoObj.getAlgoInstance()); //"AES/CBC/PKCS5Padding"
			byte[] iv = normalizeIvByteArray(Utils.getInstance().getDeviceData().getBytes(), cryptoObj.getIvLength()); 
			cipher.init(Cipher.ENCRYPT_MODE, genSecretKey(context, password, algo), new IvParameterSpec(iv));
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

		byte[] plainContent = null;
		CryptoAlgoObj cryptoObj = getCryptoAlgoObjByAlgo(algo);

		try {
			Cipher cipher = Cipher.getInstance(cryptoObj.getAlgoInstance()); //"AES/CBC/PKCS5Padding"
			byte[] iv = normalizeIvByteArray(Utils.getInstance().getDeviceData().getBytes(), cryptoObj.getIvLength()); 
			cipher.init(Cipher.DECRYPT_MODE, genSecretKey(context, password, algo), new IvParameterSpec(iv));
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

	public byte[] genMd5(byte[] source) throws YapeaException{

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new YapeaException(e);
		}
		return md.digest(source);
	}

	public void storeKeyInCache(String key, Context context) throws YapeaException{

		String encHexKey = null;
		try {
			byte[] encKey = enc(context, genKeyInCachePassword(context), key.getBytes("UTF-8"), "AES");
			encHexKey = Utils.getInstance().byteArrayToHexString(encKey);
		} catch (UnsupportedEncodingException e) {
			throw new YapeaException(e);
		}
		StaticObj.KEY = encHexKey;
	}


	public String genKeyInCachePassword(Context context) throws YapeaException{
		byte[] salt = getSalt(context);
		byte[] saltHash = genMd5(salt);
		String hexStr = null;
		try {
			hexStr = Utils.getInstance().byteArrayToHexString(saltHash);
		} catch (UnsupportedEncodingException e) {
			throw new YapeaException(e);
		}
		return hexStr;
	}


	public String retrieveKeyFromCache(Context context) throws YapeaException{

		byte[] decKey = null;
		String decStrKey = null;
		if(StaticObj.KEY != null){
			decKey = dec(context,  genKeyInCachePassword(context), Utils.getInstance().hexStringToByteArray(StaticObj.KEY), "AES");
			decStrKey = new String(decKey);
		}

		return decStrKey;
	} 

}
