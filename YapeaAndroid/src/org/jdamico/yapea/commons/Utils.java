package org.jdamico.yapea.commons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdamico.yapea.crypto.CryptoUtils;
import org.jdamico.yapea.dataobjects.ConfigObj;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

public class Utils {

	private static Utils INSTANCE = null;

	private Utils(){}

	public static Utils getInstance(){
		if(null == INSTANCE) INSTANCE = new Utils();
		return INSTANCE;
	}

	static final byte[] HEX_CHAR_TABLE = {
		(byte)'0', (byte)'1', (byte)'2', (byte)'3',
		(byte)'4', (byte)'5', (byte)'6', (byte)'7',
		(byte)'8', (byte)'9', (byte)'a', (byte)'b',
		(byte)'c', (byte)'d', (byte)'e', (byte)'f'
	};   

	public ConfigObj getConfigFile(Context context) throws YapeaException{

		ConfigObj ret = null;
		File file = new File(context.getFilesDir(), Constants.CONFIG_FILE);

		if(file.exists()){
			ret = configFileToConfigObj(file);
		}

		return ret;

	}

	public ConfigObj configFileToConfigObj(File fXmlFile) throws YapeaException {

		ConfigObj configObj = null;
		DocumentBuilderFactory dbFactory = null;
		DocumentBuilder dBuilder = null;
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			String hash = (String) getDomTagAttribute(doc, Constants.XML_CONFIG_KEY_TAG, Constants.XML_CONFIG_KEY_HASH_ATTRIB);
			String algo = (String) getDomTagAttribute(doc, Constants.XML_CONFIG_ALGO_TAG, Constants.XML_CONFIG_ALGO_TYPE_ATTRIB);

			String panicPassword = (String) getDomTagAttribute(doc, Constants.XML_CONFIG_KEY_TAG, Constants.XML_CONFIG_KEY_PANICPASSWD_ATTRIB);
			int panicNumber = Integer.parseInt( (String) getDomTagAttribute(doc, Constants.XML_CONFIG_KEY_TAG, Constants.XML_CONFIG_KEY_PANICNUMBER_ATTRIB) );


			configObj = new ConfigObj(algo, hash, false, panicPassword, panicNumber);

		} catch (NumberFormatException e) {
			throw new YapeaException(e);
		} catch (ParserConfigurationException e) {
			throw new YapeaException(e);
		} catch (SAXException e) {
			throw new YapeaException(e);
		} catch (IOException e) {
			throw new YapeaException(e);
		}

		return configObj;
	}

	public void configObjToConfigFile(ConfigObj configObj, File file) throws YapeaException{

		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<"+Constants.XML_CONFIG_ROOT_TAG+">\n");
		sb.append("<"+Constants.XML_CONFIG_KEY_TAG+" "+Constants.XML_CONFIG_KEY_HASH_ATTRIB+"=\""+configObj.getHashedKey()+"\" "+Constants.XML_CONFIG_KEY_PANICPASSWD_ATTRIB+"=\""+configObj.getPanicPassword()+"\" "+Constants.XML_CONFIG_KEY_PANICNUMBER_ATTRIB+"=\""+configObj.getPanicNumber()+"\"/>\n");
		sb.append("<"+Constants.XML_CONFIG_ALGO_TAG+" "+Constants.XML_CONFIG_ALGO_TYPE_ATTRIB+"=\""+configObj.getEncAlgo()+"\"/>\n");
		sb.append("</"+Constants.XML_CONFIG_ROOT_TAG+">\n");
		writeTextToFile(file, sb.toString());

	}


	public Object getDomTagAttribute(Document doc, String tag, String attribute) {
		Object ret = null;

		NodeList nList = doc.getElementsByTagName(tag);


		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				ret = eElement.getAttribute(attribute);
			}
		}

		return ret;
	}


	public boolean isAuthenticated(Context context, String passwd) {

		boolean isAuthenticated = false;
		String typedHash = null;
		String storedHash = null;
		if(context!=null){ 
			File file = new File(context.getFilesDir(), Constants.CONFIG_FILE);
			try {
				typedHash = byteArrayToHexString(CryptoUtils.getInstance().getKeyHash(context, passwd.toCharArray()));

				storedHash = configFileToConfigObj(file).getHashedKey();

			} catch (Exception e) {e.printStackTrace();}

			if(typedHash!=null && storedHash!=null && (typedHash.equals(storedHash)))isAuthenticated = true;
		}

		return isAuthenticated;
	}


	public void changeConfig(Context context, String oldPasswd, String newPasswd_a, String newPasswd_b, String algo, String panicPassword, int panicNumber) throws YapeaException {


		if(context!=null){ 

			File file = new File(context.getFilesDir(), Constants.CONFIG_FILE);

			if(isAuthenticated(context, oldPasswd)){

				if(null != newPasswd_a && null != newPasswd_b && (newPasswd_a.equals(newPasswd_b)) ){
					byte[] hash = CryptoUtils.getInstance().getKeyHash(context, newPasswd_a.toCharArray());

					String hashedKey;
					try {
						hashedKey = byteArrayToHexString(hash);
					} catch (UnsupportedEncodingException e) {
						throw new YapeaException(AppMessages.getInstance().getMessage("Utils.createConfig.failedToTransformKeyHash"), e);
					}

					if(panicPassword == null || panicPassword.length() == 0){
						panicPassword = "null";
						panicNumber = 0;
					}else{
						hash = CryptoUtils.getInstance().getKeyHash(context, panicPassword.toCharArray());
						try {
							panicPassword = byteArrayToHexString(hash);
						} catch (UnsupportedEncodingException e) {
							throw new YapeaException(AppMessages.getInstance().getMessage("Utils.createConfig.failedToTransformKeyHash"), e);
						}
					}

					configObjToConfigFile(new ConfigObj(algo, hashedKey, false, panicPassword, panicNumber), file);

				}else throw new YapeaException(AppMessages.getInstance().getMessage("Utils.changeConfig.diffPasswd"));

			}else throw new YapeaException(AppMessages.getInstance().getMessage("Utils.changeConfig.wrongPasswd"));

		}else throw new YapeaException(AppMessages.getInstance().getMessage("Utils.changeConfig.nullContext"));

	}

	public void createConfig(Context context, String oldPasswd, String newPasswd_a, String newPasswd_b, String algo, String panicPassword, int panicNumber) throws YapeaException {
		File file = new File(context.getFilesDir(), Constants.CONFIG_FILE);


		ConfigObj configObj = new ConfigObj();

		if(context != null){

			if(newPasswd_a.equals(newPasswd_b)){

				byte[] hash = CryptoUtils.getInstance().getKeyHash(context, newPasswd_a.toCharArray());
				try {
					configObj.setHashedKey(byteArrayToHexString(hash));
				} catch (UnsupportedEncodingException e) {
					throw new YapeaException(AppMessages.getInstance().getMessage("Utils.createConfig.failedToTransformKeyHash"), e);
				}

				if(panicPassword == null || panicPassword.length() == 0){
					panicPassword = "null";
					panicNumber = 0;
				}else{
					hash = CryptoUtils.getInstance().getKeyHash(context, panicPassword.toCharArray());
					try {
						panicPassword = byteArrayToHexString(hash);
					} catch (UnsupportedEncodingException e) {
						throw new YapeaException(AppMessages.getInstance().getMessage("Utils.createConfig.failedToTransformKeyHash"), e);
					}
				}

				configObj.setPanicPassword(panicPassword);
				configObj.setPanicNumber(panicNumber);


			}else throw new YapeaException(AppMessages.getInstance().getMessage("Utils.createConfig.diffPasswd"));


			configObj.setEncAlgo(algo);	
			configObjToConfigFile(configObj, file);
		}



	}

	public String getDeviceData(){

		StringBuffer deviceData = new StringBuffer();
		deviceData.append(Build.CPU_ABI+" ");
		deviceData.append(Build.DEVICE+" ");
		deviceData.append(Build.MANUFACTURER+" ");
		deviceData.append(Build.MODEL+" ");
		deviceData.append(Build.HARDWARE+" ");


		return deviceData.toString();

	}

	public String getIMEI(Context context){
		//<uses-permission android:name="android.permission.READ_PHONE_STATE" />

		String imei = null;

		try {
			TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE); 
			imei = mngr.getDeviceId();
		} catch (Exception e) {}

		return imei;

	}



	public String byteArrayToHexString(byte[] raw) throws UnsupportedEncodingException 
	{
		byte[] hex = new byte[2 * raw.length];
		int index = 0;

		for (byte b : raw) {
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		return new String(hex, "ASCII");
	}

	public void writeTextToFile(File file, String text) throws YapeaException {

		FileWriter fw = null;
		BufferedWriter out = null;

		try {
			fw = new FileWriter(file);
			out = new BufferedWriter(fw);
			out.write(text+"\n");
			out.close();
		} catch (IOException e) {
			throw new YapeaException(AppMessages.getInstance().getMessage("Utils.writeTextToFile.failToWriteFile"), e);
		} finally {
			if(null != fw) try { fw.close(); } catch (IOException e) { throw new YapeaException(AppMessages.getInstance().getMessage("Utils.writeTextToFile.failToWriteFile"), e); }
			if(null != out) try { out.close(); } catch (IOException e) { throw new YapeaException(AppMessages.getInstance().getMessage("Utils.writeTextToFile.failToWriteFile"), e); }
		}
	}

	public String getYapeaImageDir() {
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.yapea/"; 
	}

	public String getCurrentDateTimeFormated(String format){
		Date date = new Date();
		Format formatter = new SimpleDateFormat(format);
		String stime = formatter.format(date);
		return stime;
	}

	public byte[] getBytesFromFile(File file) throws YapeaException {
		InputStream is = null;

		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			throw new YapeaException(AppMessages.getInstance().getMessage("Utils.getBytesFromFile.fileTooLArge"));
		}

		byte[] bytes = new byte[(int)length];

		int offset = 0;
		int numRead = 0;
		try {
			is = new FileInputStream(file);
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}
		} catch (IOException e) {
			throw new YapeaException(e);
		}finally{
			if(null != is) try { is.close(); } catch (IOException e) { throw new YapeaException(e); }
		}

		if (offset < bytes.length) {
			throw new YapeaException(new IOException("Could not completely read file "+file.getName()));
		}

		return bytes;
	}

	public void byteArrayToFile(byte[] bytes, String strFilePath) throws YapeaException{
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFilePath);
			fos.write(bytes);

		} catch (FileNotFoundException e) {
			throw new YapeaException(e);
		} catch (IOException e) {
			throw new YapeaException(e);
		} finally {
			if(null!=fos)
				try {
					fos.close();
				} catch (IOException e) {
					throw new YapeaException(e);
				}
		}
	}

	public Bitmap byteArrayToBitmap(byte[] source){

		return BitmapFactory.decodeByteArray(source , 0, source.length);

	}

	public void dumpAppData(Context context) {

		clearCache();

		deleteAllPictures();

		File file = new File(context.getFilesDir(), Constants.CONFIG_FILE);
		file.delete();

	}

	public void clearCache(){
		if(StaticObj.KEY != null) StaticObj.KEY = null;
	}

	public byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
		return data;
	}

	public int isPanicAuthenticated(Context context, String passwd, int countPanic) {
		int ret = 0;
		String typedHash = null;
		String storedHash = null;
		ConfigObj cfg = null;
		if(context!=null){ 
			File file = new File(context.getFilesDir(), Constants.CONFIG_FILE);
			
			try {
				cfg = configFileToConfigObj(file);
				typedHash = byteArrayToHexString(CryptoUtils.getInstance().getKeyHash(context, passwd.toCharArray()));

				storedHash = cfg.getPanicPassword();

			} catch (Exception e) {e.printStackTrace();}

			if(typedHash!=null && storedHash!=null && (typedHash.equals(storedHash))){
				ret = 1;
				if(countPanic+1 == cfg.getPanicNumber()) deleteAllPictures();
			}
		}
		return ret;
	}

	private void deleteAllPictures() {
		String yapeaDir = getYapeaImageDir();

		File imageDir = new File(yapeaDir);

		if(imageDir.exists()){

			String[] contents = imageDir.list();
			for (int i = 0; i < contents.length; i++) {
				File f = new File(yapeaDir+contents[i]);
				f.delete();
			}
			imageDir.delete();
		}
		
	}
}
