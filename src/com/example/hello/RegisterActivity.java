package com.example.hello;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import inputcells.PictureInputCellFragment;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends Activity {
	
	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellName;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	SimpleTextInputCellFragment fragInputEmailAddress;
	PictureInputCellFragment pictureInputCell;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		fragInputCellAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.intput_account);
		fragInputCellName=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);
		fragInputCellPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		fragInputEmailAddress=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.intput_email);
		pictureInputCell=(PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.picture);
	
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submit();
				
			}
		});
		
	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		fragInputCellAccount.setLabelText("�û���");
		fragInputCellAccount.setHintText("�������û���");
		
		fragInputCellName.setLabelText("����");
		fragInputCellName.setHintText("����������");
		
		fragInputCellPassword.setLabelText("����");
		fragInputCellPassword.setHintText("����������");
		fragInputCellPassword.setIsPassword(true);
		
		fragInputCellPasswordRepeat.setLabelText("�ظ�����");
		fragInputCellPasswordRepeat.setHintText("�������ظ�����");
		fragInputCellPasswordRepeat.setIsPassword(true);
		
		fragInputEmailAddress.setLabelText("��������");
		fragInputEmailAddress.setHintText("�������������");
	}
	
	
	private void submit(){
		
		String password=fragInputCellPassword.getText();
		String passwordRepeat=fragInputCellPasswordRepeat.getText();
		
		if(!password.equals(passwordRepeat)){
			new AlertDialog.Builder(RegisterActivity.this).setMessage("�����������벻һ��").setPositiveButton("ȷ��", null).show();
		
		}
		String account=fragInputCellAccount.getText();
		String name=fragInputCellName.getText();
		String email=fragInputEmailAddress.getText();
		
		MultipartBody requestBodyBuilder=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account", account)
				.addFormDataPart("name", name)
				.addFormDataPart("email", email)
				.addFormDataPart("passwordHash", password)
				.build();
		
		OkHttpClient client=new OkHttpClient();
		
		okhttp3.Request request=new okhttp3.Request.Builder()
				.url("http://172.27.0.24:8080/membercenter/api/register")
				.method("post", null)
				.post(requestBodyBuilder)
				.build();
		
		final 	ProgressDialog progressDialog=new ProgressDialog(this);
		progressDialog.setMessage("Please Waitting...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();
						RegisterActivity.this.onResponse(arg0, arg1.body().toString());
					}
				});
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();
						RegisterActivity.this.onFailure(arg0, arg1);
					}
				});
				
			}
		});
	}
	
	void onResponse(Call arg0,String string){
		new AlertDialog.Builder(this)
		.setMessage("����ɹ�")
		.setPositiveButton("ȷ��", null)
		.show();
	}
	
	void onFailure(Call arg0, IOException arg1){
		new AlertDialog.Builder(this)
		.setTitle("����ʧ��")
		.setMessage(arg1.getLocalizedMessage())
		.setPositiveButton("ȷ��", null)
		.show();
	}
}
