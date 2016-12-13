package com.example.hello;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.View;
import api.Server;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

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
		
		fragAccount.setLabelText("用户名");
		fragAccount.setHintText("请输入用户名");
		
		fragPassword.setLabelText("密码");
		fragPassword.setHintText("请输入密码");
		fragPassword.setIsPassword(true);
	}

	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}
	void goLogin(){
		
		String account=fragAccount.getText();
		String passwordHash=MD5.getMD5(fragPassword.getText());
		
		OkHttpClient client=Server.getSharedClient();
		
		RequestBody requestbody=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account", account)
				.addFormDataPart("passwordHash",passwordHash)
				.build();
		
		Request request=Server.requestBuilderWithApi("login")
				.method("post", null)
				.post(requestbody)
				.build();
			
			final ProgressDialog progressdialog=new ProgressDialog(LoginActivity.this);
			progressdialog.setMessage("Login...");
			progressdialog.setCancelable(false);
			progressdialog.setCanceledOnTouchOutside(false);
			progressdialog.show();
			
			client.newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(final Call arg0, Response arg1) throws IOException {
					final String message =arg1.body().string();
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							progressdialog.dismiss();
							if(message.equals("")){
								LoginActivity.this.onResponse(arg0,"密码错误！");
							}else{
								try{								
									LoginActivity.this.onResponse(arg0, message);								
								}catch(Exception e){								
									e.printStackTrace();								
									LoginActivity.this.onFailure(arg0, e);
								}
							}							
						}
					});				
				}			
				@Override
				public void onFailure(final Call arg0, final IOException arg1) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							progressdialog.dismiss();
							onFailure(arg0, arg1);							
						}
					});				
				}
			});		
//		Intent itnt=new Intent(this,HelloWorldActivity.class);
//		startActivity(itnt);
	}
	
	
	void onResponse(Call call,String response){
//		new AlertDialog.Builder(this)
//		.setTitle("请求成功")
//		.setMessage(response)
//		.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//		}).show();
		if(!response.equals("密码错误！")){
			Intent itnt=new Intent(this,HelloWorldActivity.class);
			startActivity(itnt);	
		}
		}
	
	void onFailure(Call call,Exception e){
		new AlertDialog.Builder(this)
		.setTitle("请求失败")
		.setMessage(e.getLocalizedMessage())
		.setPositiveButton("确认",null)
		.show();
	}
	
	
	void goRecoverPassword(){
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}
}
