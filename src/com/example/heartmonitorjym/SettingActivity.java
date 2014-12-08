package com.example.heartmonitorjym;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        
    }
	
	public void onClickBack (View v){
		finish();
	}
	
}