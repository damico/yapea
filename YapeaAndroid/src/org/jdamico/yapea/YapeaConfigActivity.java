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

import org.jdamico.yapea.commons.AppMessages;
import org.jdamico.yapea.commons.Utils;
import org.jdamico.yapea.commons.YapeaException;
import org.jdamico.yapea.crypto.CryptoUtils;
import org.jdamico.yapea.dataobjects.ConfigObj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class YapeaConfigActivity extends Activity {

	Button save_config_button = null;
	Button reset_button = null;
	Button clear_cache_button = null;
	
	Boolean isConfigExistent = false;
	String oldPasswd = null;
	String newPasswd_a = null;
	String newPasswd_b = null;
	String panicPAsswd = null;
	int panicNumber = 0;
	String algo = null;
	EditText oldPasswdEt = null;
	EditText newPasswd_aEt = null;
	EditText newPasswd_bEt = null;
	
	EditText panicPasswdEt = null;
	
	RadioGroup algoRadioGrp = null;
	AlertDialog.Builder adb = null;
	
	NumberPicker panicNumberNp = null;

	
	TextView algoTv = null;
	TextView oldPtV = null;
	TextView newPtV = null;
	TextView newP2tV = null;
	TextView panicTv = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yapea_config);

		adb = new AlertDialog.Builder(this);

		Bundle extras = null;
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if(extras == null) {
				isConfigExistent= null;
			} else {
				isConfigExistent= extras.getBoolean("isConfigExistent");
			}
		} else {
			isConfigExistent= (Boolean) savedInstanceState.getSerializable("isConfigExistent");

		}
		
		panicNumberNp = (NumberPicker) findViewById(R.id.panicNumber);
		panicNumberNp.setMinValue(1);
		panicNumberNp.setMaxValue(9);
		
		panicNumber = panicNumberNp.getValue();
		
		panicTv = (TextView) findViewById(R.id.panicTv);
		panicTv.setText(AppMessages.getInstance().getMessage("GLOBAL.panicTv"));
		
		algoTv = (TextView) findViewById(R.id.mem_key_textView);
		algoTv.setText(AppMessages.getInstance().getMessage("GLOBAL.algoTv"));
		
		oldPtV = (TextView) findViewById(R.id.oldPtV);
		oldPtV.setText(AppMessages.getInstance().getMessage("GLOBAL.oldPtV"));
		
		newPtV = (TextView) findViewById(R.id.newPtV);
		newPtV.setText(AppMessages.getInstance().getMessage("GLOBAL.newPtV"));
		
		newP2tV = (TextView) findViewById(R.id.newP2tV);
		newP2tV.setText(AppMessages.getInstance().getMessage("GLOBAL.newP2tV"));
		
		oldPasswdEt = (EditText) findViewById(R.id.old_pass_txt);
		
		clear_cache_button  = (Button) findViewById(R.id.clear_cache_button);
		clear_cache_button.setText(AppMessages.getInstance().getMessage("GLOBAL.clear_cache_button"));
		
		clear_cache_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.getInstance().clearCache();
				Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage("GLOBAL.cache_cleaned"), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getApplicationContext(), YapeaAuthActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		
		reset_button = (Button) findViewById(R.id.reset_button);
		reset_button.setText(AppMessages.getInstance().getMessage("GLOBAL.reset_button"));
		algoRadioGrp = (RadioGroup) findViewById(R.id.algoRadioGrp);
		panicPasswdEt = (EditText) findViewById(R.id.panicPasswdEt);

		if(!isConfigExistent){
			oldPasswdEt.setEnabled(false);
			reset_button.setEnabled(false);
		}else{
			try {
				ConfigObj cfgObj = Utils.getInstance().getConfigFile(getApplicationContext());
				if(cfgObj.getEncAlgo().equals("BLOWFISH")) algoRadioGrp.check(R.id.blow_fish_radio);
				
				if(cfgObj.getPanicNumber() > 0){
					panicPasswdEt.setText(cfgObj.getPanicPassword());
					panicNumberNp.setValue(cfgObj.getPanicNumber());
				}
				
			} catch (YapeaException e) {
				Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage(e.getMessage()), Toast.LENGTH_LONG).show();
			}
		}

		
		

		newPasswd_aEt = (EditText) findViewById(R.id.new_pass_txt_a);

		newPasswd_bEt = (EditText) findViewById(R.id.new_pass_txt_b);

		save_config_button = (Button) findViewById(R.id.save_config_button);
		
		save_config_button.setText(AppMessages.getInstance().getMessage("GLOBAL.save_config_button"));

		adb.setView(this.findViewById(R.layout.activity_yapea_config));

		reset_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				adb.setTitle(AppMessages.getInstance().getMessage("YapeaConfigActivity.onCreate.dumpAppData"));
				adb.setIcon(android.R.drawable.ic_dialog_alert);

				adb.setPositiveButton(AppMessages.getInstance().getMessage("GLOBAL.yes"), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {


						Utils.getInstance().dumpAppData(getApplicationContext());

						Intent intent = new Intent(getApplicationContext(), YapeaMainActivity.class);
						startActivityForResult(intent, 0);

					} });


				adb.setNegativeButton(AppMessages.getInstance().getMessage("GLOBAL.no"), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getApplicationContext(), YapeaMainActivity.class);
						startActivityForResult(intent, 0);
					} });

				adb.show();



			}
		});

		save_config_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String yapeaDir = Utils.getInstance().getYapeaImageDir();
				File dir = new File(yapeaDir); 
				if(dir !=null && !dir.exists()) dir.mkdirs();

				oldPasswd = oldPasswdEt.getText().toString();
				newPasswd_a = newPasswd_aEt.getText().toString();
				newPasswd_b = newPasswd_bEt.getText().toString();
				panicPAsswd = panicPasswdEt.getText().toString();

				switch (algoRadioGrp.getCheckedRadioButtonId()) {
				case R.id.aes_radio : algo = "AES";
				break;
				case R.id.blow_fish_radio : algo = "BLOWFISH";
				break;

				}

				if(isConfigExistent)
					try {
						Utils.getInstance().changeConfig(v.getContext(), oldPasswd, newPasswd_a, newPasswd_b, algo, panicPAsswd, panicNumber);
					} catch (YapeaException e) {
						Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage(e.getMessage()), Toast.LENGTH_LONG).show();
					}
				else
					try {

						Utils.getInstance().createConfig(v.getContext(), oldPasswd, newPasswd_a, newPasswd_b, algo, panicPAsswd, panicNumber);
					} catch (YapeaException e) {
						Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage(e.getMessage()), Toast.LENGTH_LONG).show();
					}
				if(Utils.getInstance().isAuthenticated(v.getContext(), newPasswd_a)){
					try {
						CryptoUtils.getInstance().storeKeyInCache(newPasswd_a, getApplicationContext());
					} catch (YapeaException e) {
						Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage(e.getMessage()), Toast.LENGTH_LONG).show();
					}
					Intent intent = new Intent(v.getContext(), YapeaMainActivity.class);
					startActivityForResult(intent, 0);
				}else Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage("YapeaAuthActivity.onCreate.WrongKey"), Toast.LENGTH_LONG).show();
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

}
