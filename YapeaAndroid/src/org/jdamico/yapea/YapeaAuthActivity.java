package org.jdamico.yapea;

import org.jdamico.yapea.commons.StaticObj;
import org.jdamico.yapea.commons.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class YapeaAuthActivity extends Activity {
	
	Button authButton = null;
	TextView keyTv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yapea_auth);
		
		authButton = (Button) findViewById(R.id.auth_button);
		
		keyTv = (TextView) findViewById(R.id.key_text);
		
		authButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(Utils.getInstance().isAuthenticated(v.getContext(), keyTv.getText().toString())){
					StaticObj.KEY = keyTv.getText().toString();
					Intent intent = new Intent(v.getContext(), YapeaMainActivity.class);
	                startActivityForResult(intent, 0);
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.yapea_auth, menu);
		return true;
	}

}
