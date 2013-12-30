package org.jdamico.yapea;

import org.jdamico.yapea.commons.Constants;
import org.jdamico.yapea.commons.StaticObj;
import org.jdamico.yapea.commons.Utils;
import org.jdamico.yapea.commons.YapeaException;
import org.jdamico.yapea.dataobjects.ConfigObj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class YapeaMainActivity extends Activity {
	
	Button cam_button = null;
	Button gallery_button = null;
	Button config_button = null;
	Boolean isConfigExistent = false;
	TextView chachedMemTv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yapea_main);
		
		
		
		cam_button = (Button) findViewById(R.id.cam_button);
		gallery_button = (Button) findViewById(R.id.gallery_button);
		config_button = (Button) findViewById(R.id.config_button);
		chachedMemTv = (TextView) findViewById(R.id.mem_key_textView);
		
		
		Context context = getApplicationContext();
		ConfigObj config = null;
		try {
			config = Utils.getInstance().getConfigFile(context);
		} catch (YapeaException e) {
			e.printStackTrace();
		}
		if(null == config){
			cam_button.setEnabled(false);
			gallery_button.setEnabled(false);
		}else{
			isConfigExistent = true;
			if(StaticObj.KEY != null) chachedMemTv.setText("Chave em cache.");
			else{
				Intent intent = new Intent(context, YapeaAuthActivity.class);
                startActivityForResult(intent, 0);
			}
			
			cam_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent cameraIntent = ActivityHelper.getInstance().takePicture(v);
					startActivityForResult(cameraIntent, Constants.TAKE_PHOTO_CODE);
					
				}
			});
		}
		
		config_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(v.getContext(), YapeaConfigActivity.class);
				intent.putExtra("isConfigExistent", isConfigExistent);
                startActivityForResult(intent, 0);
				
			}
		});
		
		gallery_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(v.getContext(), ImageListActivity.class);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    if (requestCode == Constants.TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
	        Log.d("CameraDemo", "Pic saved");


	    }
	}

}
