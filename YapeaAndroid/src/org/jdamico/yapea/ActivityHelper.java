package org.jdamico.yapea;

import java.io.File;
import java.io.IOException;

import org.jdamico.yapea.commons.Constants;
import org.jdamico.yapea.commons.Utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class ActivityHelper {

	private static ActivityHelper INSTANCE = null;

	private ActivityHelper(){}

	public static ActivityHelper getInstance(){
		if(null == INSTANCE) INSTANCE = new ActivityHelper();
		return INSTANCE;
	}

	public Intent takePicture(View v){

		String yapeaDir = Utils.getInstance().getYapeaImageDir();
		String file = yapeaDir+Utils.getInstance().getCurrentDateTimeFormated(Constants.TIMESTAMP_FORMAT)+".jpg";



		File newfile = new File(file);
		try {
			newfile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}       

		Uri outputFileUri = Uri.fromFile(newfile);

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		return cameraIntent;

	}



}
