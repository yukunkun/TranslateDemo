package com.example.yukun.youdaotrans.floatwindowdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.yukun.youdaotrans.R;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_a);
		Button startFloatWindow = (Button) findViewById(R.id.start_float_window);
		startFloatWindow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
				startService(intent);
				finish();
			}
		});
	}
}
