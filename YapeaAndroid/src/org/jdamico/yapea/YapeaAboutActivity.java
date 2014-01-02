package org.jdamico.yapea;

import org.jdamico.yapea.commons.AppMessages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class YapeaAboutActivity extends Activity {
	
	Button back_button = null;
	TextView version = null;
	TextView author = null;
	TextView source = null;
	TextView lic = null;
			

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yapea_about);
		
		back_button = (Button) findViewById(R.id.back_button);
		back_button.setText(AppMessages.getInstance().getMessage("GLOBAL.back_button"));
		
		version = (TextView) findViewById(R.id.version);
		version.setText(AppMessages.getInstance().getMessage("GLOBAL.version"));
		
		author = (TextView) findViewById(R.id.author);
		author.setText(AppMessages.getInstance().getMessage("GLOBAL.author"));
		
		source = (TextView) findViewById(R.id.source);
		source.setText(AppMessages.getInstance().getMessage("GLOBAL.source"));
		
		lic = (TextView) findViewById(R.id.lic);
		lic.setText(AppMessages.getInstance().getMessage("GLOBAL.lic"));
		
		back_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), YapeaMainActivity.class);
				startActivityForResult(intent, 0);
				
			}
		});
	}



}
