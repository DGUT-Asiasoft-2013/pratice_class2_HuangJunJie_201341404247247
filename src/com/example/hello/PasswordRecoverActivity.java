package com.example.hello;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import api.Server;
import fragments.PasswordRecoverStep1Fragment;
import fragments.PasswordRecoverStep1Fragment.OnGoNextListener;
import fragments.PasswordRecoverStep2Fragment;
import fragments.PasswordRecoverStep2Fragment.OnSubmitClickedListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordRecoverActivity extends Activity {

	PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
	PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_password_recover);
		
 		step1.setOnGoNextListener(new OnGoNextListener() {
			
			@Override
			public void onGoNext() {
				goStep2();
			}
		});
 		
 		step2.setOnSubmitClickedListener(new OnSubmitClickedListener() {
			
			@Override
			public void onSubmitClicked() {
				goSubmit();
				
			}
		});
 		
 		
 		getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();
	}
	
	void goStep2(){
		
		getFragmentManager().beginTransaction()
		
		.setCustomAnimations(R.animator.slide_in_right, 
				             R.animator.slide_out_left,
				             R.animator.slide_in_left,
				             R.animator.slide_out_right)
		
		.replace(R.id.container, step2).addToBackStack(null).commit();
	}
	
	void goSubmit(){
		OkHttpClient client=Server.getSharedClient();
		MultipartBody body=new MultipartBody.Builder()
				.addFormDataPart("email", step1.getText())
				.addFormDataPart("passwordHash", MD5.getMD5(step2.getText()))
				.build();
				
		Request request=Server.requestBuilderWithApi("passwordrecover").post(body).build();
		
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
					
				runOnUiThread(new Runnable() {
					public void run() {
						PasswordRecoverActivity.this.onResponse();
					}
				});				
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {				
				
			}
		});	
	}
		
		void onResponse(){
			new AlertDialog.Builder(this).setMessage("������ĳɹ���")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();	
					overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
				}
			}).show();				
		}
}