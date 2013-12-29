package org.jdamico.yapea;

import org.jdamico.yapea.commons.Utils;
import org.jdamico.yapea.dataobjects.ConfigObj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class YapeaMainActivity extends Activity {
	
	Button cam_button = null;
	Button gallery_button = null;
	Button config_button = null;
	Boolean isConfigExistent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yapea_main);
		
		
		
		cam_button = (Button) findViewById(R.id.cam_button);
		gallery_button = (Button) findViewById(R.id.gallery_button);
		config_button = (Button) findViewById(R.id.config_button);
		
		
		
		Context context = getApplicationContext();
		ConfigObj config = Utils.getInstance().getConfigFile(context);
		if(null == config){
			cam_button.setEnabled(false);
			//cam_button.setClickable(false);
		}else isConfigExistent = true;
		
		config_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(v.getContext(), YapeaConfigActivity.class);
				
				intent.putExtra("isConfigExistent", isConfigExistent);
				
                startActivityForResult(intent, 0);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.yapea_main, menu);
		return true;
	}

}
