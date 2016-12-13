package com.example.hello;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class FeedContentActivity extends Activity {
	
	TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.text);
		String data=getIntent().getStringExtra("data");
		textView=(TextView) findViewById(R.id.text1);
		textView.setText(data);
//		
	}

}