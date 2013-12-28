package org.jdamico.yapea;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class YapeaAuthActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yapea_auth);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.yapea_auth, menu);
		return true;
	}

}
