package com.vivek.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MyAndroidAppActivity extends Activity {

  private ToggleButton toggleButton1, toggleButton2;
  private Button btnDisplay;

  @Override
  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	addListenerOnButton();

  }

  public void addListenerOnButton() {

	toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
	toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
	btnDisplay = (Button) findViewById(R.id.btnDisplay);

	btnDisplay.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {

		   StringBuffer result = new StringBuffer();
		   result.append("toggleButton1 : ").append(toggleButton1.getText());
		   result.append("\ntoggleButton2 : ").append(toggleButton2.getText());

		   Toast.makeText(MyAndroidAppActivity.this, result.toString(),
			Toast.LENGTH_SHORT).show();

		}

	});

  }
}