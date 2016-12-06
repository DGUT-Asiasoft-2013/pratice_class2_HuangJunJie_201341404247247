package com.example.hello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import inputcells.SimpleTextInputCellFragment;

public class LoginActivity extends Activity {
	
	SimpleTextInputCellFragment fragAccount;
	SimpleTextInputCellFragment fragPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				goRegister();
			}
		});
		
		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goLogin();
			}
		}); 
		
          findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goRecoverPassword();
			}
		});
		
		fragAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		fragAccount.setLabelText("�û���");
		fragAccount.setHintText("�������û���");
		
		fragPassword.setLabelText("����");
		fragPassword.setHintText("����������");
		fragPassword.setIsPassword(true);
	}

	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}
	void goLogin(){
		Intent itnt=new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);
	}
	
	void goRecoverPassword(){
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}
}
