package com.example.hello;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HelloWorldActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helloworld);
	}

	public void startAnim(View v){
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(v, "rotation", 0.0f, 360f);
        rotateAnim.setDuration(3000);
        rotateAnim.start();

    }
}
