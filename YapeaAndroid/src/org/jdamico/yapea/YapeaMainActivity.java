package org.jdamico.yapea;

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

import java.io.File;

import org.jdamico.yapea.commons.ActivityHelper;
import org.jdamico.yapea.commons.AppMessages;
import org.jdamico.yapea.commons.Constants;
import org.jdamico.yapea.commons.StaticObj;
import org.jdamico.yapea.commons.Utils;
import org.jdamico.yapea.commons.YapeaException;
import org.jdamico.yapea.crypto.CryptoUtils;
import org.jdamico.yapea.dataobjects.ConfigObj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class YapeaMainActivity extends Activity {

	Button cam_button = null;
	Button gallery_button = null;
	Button config_button = null;
	Boolean isConfigExistent = false;
	TextView chachedMemTv = null;
	String key = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yapea_main);

		
		try {
			key = CryptoUtils.getInstance().retrieveKeyFromCache(getApplicationContext());
		} catch (YapeaException e) {
			Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage(e.getMessage()), Toast.LENGTH_LONG).show();
		}

		cam_button = (Button) findViewById(R.id.cam_button);
		gallery_button = (Button) findViewById(R.id.gallery_button);
		config_button = (Button) findViewById(R.id.config_button);
		chachedMemTv = (TextView) findViewById(R.id.mem_key_textView);
		
		cam_button.setText(AppMessages.getInstance().getMessage("GLOBAL.cam_button"));
		gallery_button.setText(AppMessages.getInstance().getMessage("GLOBAL.gallery_button"));
		config_button.setText(AppMessages.getInstance().getMessage("GLOBAL.config_button"));


		Context context = getApplicationContext();
		ConfigObj config = null;
		try {
			config = Utils.getInstance().getConfigFile(context);
		} catch (YapeaException e) {
			Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage(e.getMessage()), Toast.LENGTH_LONG).show();
		}
		if(null == config){
			cam_button.setEnabled(false);
			gallery_button.setEnabled(false);
		}else{
			isConfigExistent = true;
			
			
			
			if(key != null  && Utils.getInstance().isAuthenticated(getApplicationContext(), key)) chachedMemTv.setText(AppMessages.getInstance().getMessage("YapeaMainActivity.onCreate.keyInCache"));
			else{
				Intent intent = new Intent(context, YapeaAuthActivity.class);
				startActivityForResult(intent, 0);
			}

			cam_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(key != null && Utils.getInstance().isAuthenticated(v.getContext(), key)){
						Intent cameraIntent = ActivityHelper.getInstance().takePicture(v);
						startActivityForResult(cameraIntent, Constants.TAKE_PHOTO_CODE);
					}else{
						Intent intent = new Intent(v.getContext(), YapeaAuthActivity.class);
						startActivityForResult(intent, 0);
					}
				}
			});
		}

		config_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isConfigExistent){
					enterCfg(v);
				}else if(isConfigExistent && key != null && Utils.getInstance().isAuthenticated(v.getContext(), key)){
					enterCfg(v);
				}else{
					Intent intent = new Intent(v.getContext(), YapeaAuthActivity.class);
					startActivityForResult(intent, 0);
				}
			}

			public void enterCfg(View v){
				Intent intent = new Intent(v.getContext(), YapeaConfigActivity.class);
				intent.putExtra("isConfigExistent", isConfigExistent);
				startActivityForResult(intent, 0);
			}
		});

		gallery_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(key != null && Utils.getInstance().isAuthenticated(v.getContext(), key)){
					Intent intent = new Intent(v.getContext(), ImageListActivity.class);
					intent.putExtra("isConfigExistent", isConfigExistent);
					startActivityForResult(intent, 0);
				}else{
					Intent intent = new Intent(v.getContext(), YapeaAuthActivity.class);
					startActivityForResult(intent, 0);
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(AppMessages.getInstance().getMessage("GLOBAL.about"));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case 0:
			Intent intent = new Intent(getApplicationContext(), YapeaAboutActivity.class);
			startActivityForResult(intent, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

			String yapeaDir = Utils.getInstance().getYapeaImageDir();

			File imageDir = new File(yapeaDir);

			if(imageDir.exists()){

				String[] contents = imageDir.list();
				for (int i = 0; i < contents.length; i++) {
					if(contents[i].substring(contents[i].length()-3, contents[i].length()).equalsIgnoreCase("jpg")){

						File f = new File(yapeaDir+contents[i]);
						try {
							byte[] plainContent = Utils.getInstance().getBytesFromFile(f);
							byte[] cipherContent = CryptoUtils.getInstance().enc(getApplicationContext(), key, plainContent, Utils.getInstance().getConfigFile(getApplicationContext()).getEncAlgo());
							Utils.getInstance().byteArrayToFile(cipherContent, yapeaDir+contents[i]+".yapea");
						} catch (YapeaException e) {
							Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage(e.getMessage()), Toast.LENGTH_LONG).show();
						}finally{
							f.delete();
						}

					}
				}

			} //TODO add exception

		}else{
			String yapeaDir = Utils.getInstance().getYapeaImageDir();

			File imageDir = new File(yapeaDir);

			if(imageDir.exists()){

				String[] contents = imageDir.list();
				for (int i = 0; i < contents.length; i++) {
					if(contents[i].substring(contents[i].length()-3, contents[i].length()).equalsIgnoreCase("jpg")){

						File f = new File(yapeaDir+contents[i]);
						f.delete();


					}
				}

			} //TODO add exception
		}
	}

	@Override
	public void onBackPressed() {
		
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);

	}

}
