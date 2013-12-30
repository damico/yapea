package org.jdamico.yapea;

import java.io.File;

import org.jdamico.yapea.commons.StaticObj;
import org.jdamico.yapea.commons.Utils;
import org.jdamico.yapea.commons.YapeaException;
import org.jdamico.yapea.crypto.CryptoUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class YapeaConfigActivity extends Activity {

	Button save_config_button = null;
	Button reset_button = null;
	Boolean isConfigExistent = false;
	String oldPasswd = null;
	String newPasswd_a = null;
	String newPasswd_b = null;
	String algo = null;
	TextView oldPasswdTv = null;
	TextView newPasswd_aTv = null;
	TextView newPasswd_bTv = null;
	RadioGroup algoRadioGrp = null;
	AlertDialog.Builder adb = null;

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

		oldPasswdTv = (TextView) findViewById(R.id.old_pass_txt);
		reset_button = (Button) findViewById(R.id.reset_button);
		
		if(!isConfigExistent){
			oldPasswdTv.setEnabled(false);
			reset_button.setEnabled(false);
		}

		algoRadioGrp = (RadioGroup) findViewById(R.id.algoRadioGrp);

		newPasswd_aTv = (TextView) findViewById(R.id.new_pass_txt_a);

		newPasswd_bTv = (TextView) findViewById(R.id.new_pass_txt_b);

		save_config_button = (Button) findViewById(R.id.save_config_button);

		
		adb.setView(this.findViewById(R.layout.activity_yapea_config));

		reset_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				adb.setTitle("Deletar todos os dados da aplicação?");
				adb.setIcon(android.R.drawable.ic_dialog_alert);

				adb.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

											
						Utils.getInstance().dumpÁpp(getApplicationContext());

						Intent intent = new Intent(getApplicationContext(), YapeaMainActivity.class);
						startActivityForResult(intent, 0);

					} });


				adb.setNegativeButton("Não", new DialogInterface.OnClickListener() {
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

				oldPasswd = oldPasswdTv.getText().toString();
				newPasswd_a = newPasswd_aTv.getText().toString();
				newPasswd_b = newPasswd_bTv.getText().toString();

				switch (algoRadioGrp.getCheckedRadioButtonId()) {
				case R.id.aes_radio : algo = "AES";
				break;
				case R.id.blow_fish_radio : algo = "BLOWFISH";
				break;

				}

				if(isConfigExistent)
					try {
						Utils.getInstance().changeConfig(v.getContext(), oldPasswd, newPasswd_a, newPasswd_b, algo);
					} catch (YapeaException e) {

						e.printStackTrace();
					}
				else
					try {

						Utils.getInstance().createConfig(v.getContext(), oldPasswd, newPasswd_a, newPasswd_b, algo);
					} catch (YapeaException e) {
						e.printStackTrace();
					}
				if(Utils.getInstance().isAuthenticated(v.getContext(), newPasswd_a)){
					StaticObj.KEY = newPasswd_a;
					Intent intent = new Intent(v.getContext(), YapeaMainActivity.class);
					startActivityForResult(intent, 0);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.yapea_config, menu);
		return true;
	}

}
