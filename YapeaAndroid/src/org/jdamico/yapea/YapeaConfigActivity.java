package org.jdamico.yapea;

import org.jdamico.yapea.commons.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class YapeaConfigActivity extends Activity {

	Button save_config_button = null;
	Boolean isConfigExistent = false;
	String oldPasswd = null;
	String newPasswd_a = null;
	String newPasswd_b = null;
	String algo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yapea_config);
		
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
		
		
		save_config_button = (Button) findViewById(R.id.save_config_button);
		
		save_config_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(isConfigExistent) Utils.getInstance().changeConfig(oldPasswd, newPasswd_a, newPasswd_b, algo);
				else Utils.getInstance().createConfig(oldPasswd, newPasswd_a, newPasswd_b, algo);
				
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
