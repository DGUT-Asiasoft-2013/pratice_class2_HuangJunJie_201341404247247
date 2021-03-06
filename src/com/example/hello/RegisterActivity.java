package com.example.hello;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import api.Server;
import inputcells.PictureInputCellFragment;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {
	
	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellName;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	SimpleTextInputCellFragment fragInputEmailAddress;
	PictureInputCellFragment fragInputAvatar;
	
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
		fragInputAvatar=(PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.picture);
	
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
		
		fragInputCellAccount.setLabelText("用户名");
		fragInputCellAccount.setHintText("请输入用户名");
		
		fragInputCellName.setLabelText("名字");
		fragInputCellName.setHintText("请输入名字");
		
		fragInputCellPassword.setLabelText("密码");
		fragInputCellPassword.setHintText("请输入密码");
		fragInputCellPassword.setIsPassword(true);
		
		fragInputCellPasswordRepeat.setLabelText("重复密码");
		fragInputCellPasswordRepeat.setHintText("请输入重复密码");
		fragInputCellPasswordRepeat.setIsPassword(true);
		
		fragInputEmailAddress.setLabelText("电子邮箱");
		fragInputEmailAddress.setHintText("请输入电子邮箱");
	}
	
	
	  void submit(){
		
		String password=fragInputCellPassword.getText();
		String passwordRepeat=fragInputCellPasswordRepeat.getText();
		
		if(!password.equals(passwordRepeat)){
			new AlertDialog
			.Builder(RegisterActivity.this)
			.setMessage("两次密码输入不一致")
			.setPositiveButton("好", null)
			.show();
			
			return;
		}
		
		password = MD5.getMD5(password);
		
		String account=fragInputCellAccount.getText();
		String name=fragInputCellName.getText();
		String email=fragInputEmailAddress.getText();
		
		OkHttpClient client=Server.getSharedClient();
		
		MultipartBody.Builder requestBodyBuilder=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account", account)
				.addFormDataPart("name", name)
				.addFormDataPart("email", email)
				.addFormDataPart("passwordHash", password);
		
		if(fragInputAvatar.getPngData()!=null){
			requestBodyBuilder
			.addFormDataPart(
					"avatar","avatar",
					RequestBody
					.create(MediaType.parse("image/png"),
							fragInputAvatar.getPngData()));
		}
		
		Request request=Server.requestBuilderWithApi("register")
				.method("post", null)
				.post(requestBodyBuilder.build())
				.build();
		
		final 	ProgressDialog progressDialog=new ProgressDialog(this);
		progressDialog.setMessage("Please Waitting...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				final String responseString =arg1.body().string();
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();
						RegisterActivity.this.onResponse(arg0, responseString);
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
		.setMessage("注册成功")
		.setPositiveButton("确认", null)
		.show();
	}
	
	void onFailure(Call arg0, IOException arg1){
		new AlertDialog.Builder(this)
		.setTitle("请求失败")
		.setMessage(arg1.getLocalizedMessage())
		.setPositiveButton("确认", null)
		.show();
	}
}
