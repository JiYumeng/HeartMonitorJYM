package com.example.heartmonitorjym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
    }
	
	public void onClickLogin (View v){
		Intent inteny= new Intent(this,MainActivity.class);
		startActivity(inteny);
		finish();
	}
	
}