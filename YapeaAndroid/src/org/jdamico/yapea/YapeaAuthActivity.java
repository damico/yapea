package org.jdamico.yapea;

import org.jdamico.yapea.commons.AppMessages;
import org.jdamico.yapea.commons.StaticObj;
import org.jdamico.yapea.commons.Utils;
import org.jdamico.yapea.commons.YapeaException;
import org.jdamico.yapea.crypto.CryptoUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class YapeaAuthActivity extends Activity {

	Button authButton = null;
	EditText keyEt = null;
	TextView key_textView = null;
	int countPanic = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yapea_auth);

		authButton = (Button) findViewById(R.id.auth_button);
		authButton.setText(AppMessages.getInstance().getMessage("GLOBAL.authButton"));
		
		key_textView = (TextView) findViewById(R.id.key_textView);
		key_textView.setText(AppMessages.getInstance().getMessage("GLOBAL.key_textView"));

		keyEt = (EditText) findViewById(R.id.key_text);

		authButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(Utils.getInstance().isAuthenticated(v.getContext(), keyEt.getText().toString())){
					
					
					try {
						CryptoUtils.getInstance().storeKeyInCache(keyEt.getText().toString(), v.getContext());
					} catch (YapeaException e) {
						Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage("YapeaAuthActivity.onCreate.failToStoreKeyInCache"), Toast.LENGTH_LONG).show();
					}
					
					
					Intent intent = new Intent(v.getContext(), YapeaMainActivity.class);
					startActivityForResult(intent, 0);
				}else{
					countPanic = countPanic + Utils.getInstance().isPanicAuthenticated(v.getContext(), keyEt.getText().toString(), countPanic);
					//System.out.println("============== countPanic"+countPanic);
					Toast.makeText(getApplicationContext(), AppMessages.getInstance().getMessage("YapeaAuthActivity.onCreate.WrongKey"), Toast.LENGTH_LONG).show();
				}
				keyEt.setText("");
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
