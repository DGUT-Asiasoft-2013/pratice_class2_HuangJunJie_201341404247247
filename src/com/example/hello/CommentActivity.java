package com.example.hello;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import api.Server;
import entity.Article;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pages.NewListActivity;

public class CommentActivity extends Activity{

	EditText editText;
	Article article;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		
		editText=(EditText) findViewById(R.id.text);
		article=(Article) getIntent().getSerializableExtra("pos");
		
		findViewById(R.id.btn_send).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendComment();
				
			}
		});
	}
	
	void sendComment(){
		String comment=editText.getText().toString();
		
		MultipartBody body=new MultipartBody.Builder()
				.addFormDataPart("text", comment)
				.build();
		
		Request request=Server.requestBuilderWithApi("/article/"+article.getId()+"/comments")
				.method("post",null)
				.post(body)
				.build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				
				try{
					Log.d("response string", arg1.body().string());
				}catch(Exception e){
					Log.d("response string error", e.getMessage());
				}
				
				runOnUiThread(new Runnable() {
					public void run() {
						onSucceed();
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						CommentActivity.this.onFailure(arg1);
					}
				});
				
			}
		});			
	}
	void onSucceed(){
		new AlertDialog.Builder(this).setMessage("ÆÀÂÛ³É¹¦£¡")
		.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
			}
		}).show();
	}	
	void onFailure(Exception e){
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
	}
}
