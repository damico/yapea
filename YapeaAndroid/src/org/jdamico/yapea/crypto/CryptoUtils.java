package org.jdamico.yapea.crypto;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.jdamico.yapea.commons.AppMessages;
import org.jdamico.yapea.commons.Constants;
import org.jdamico.yapea.commons.Utils;
import org.jdamico.yapea.commons.YapeaException;

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
	
}
