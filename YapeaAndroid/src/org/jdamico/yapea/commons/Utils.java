package org.jdamico.yapea.commons;

import java.io.File;

import org.jdamico.yapea.dataobjects.ConfigObj;

import android.content.Context;

public class Utils {
	
	private static Utils INSTANCE = null;
	
	private Utils(){}
	
	public static Utils getInstance(){
		if(null == INSTANCE) INSTANCE = new Utils();
		return INSTANCE;
	}
	
	public ConfigObj getConfigFile(Context context){
		
		ConfigObj ret = null;
		File file = new File(context.getFilesDir(), Constants.CONFIG_FILE);
		
		if(file.exists()){
			ret = parseConfig(file);
		}
		
		return ret;
		
	}

	private ConfigObj parseConfig(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	public void changeConfig(String oldPasswd, String newPasswd_a,
			String newPasswd_b, String algo) {
		// TODO Auto-generated method stub
		
	}

	public void createConfig(String oldPasswd, String newPasswd_a,
			String newPasswd_b, String algo) {
		// TODO Auto-generated method stub
		
	}

}
