package com.example.heartmonitorjym;








import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends TabActivity {

	public static final String TAG = MainActivity.class.getSimpleName();
	public final static String EXTRA_MESSAGE = "com.anko.ui.MESSAGE";
	private RadioGroup mTabButtonGroup;
	private TabHost mTabHost;

	public static final String TAB_PERSONAL = "PERSONAL_ACTIVITY";
	public static final String TAB_TEST = "TEST_ACTIVITY";
	public static final String TAB_DOCTOR = "DOCTOR_ACTIVITY";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
		initView();
    }

	private void findViewById() {
		mTabButtonGroup = (RadioGroup) findViewById(R.id.home_radio_button_group);
	}
	
	private void initView() {

		mTabHost = getTabHost();

		Intent i_personal = new Intent(this, PersonalActivity.class);
		Intent i_test = new Intent(this, TestActivity.class);
		Intent i_doctor = new Intent(this, DoctorActivity.class);
		
		mTabHost.addTab(mTabHost.newTabSpec(TAB_PERSONAL).setIndicator(TAB_PERSONAL)
				.setContent(i_personal));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_TEST).setIndicator(TAB_TEST)
				.setContent(i_test));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_DOCTOR)
				.setIndicator(TAB_DOCTOR).setContent(i_doctor));		

		mTabHost.setCurrentTabByTag(TAB_PERSONAL);

		mTabButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {				        

						case R.id.home_tab_test:
							mTabHost.setCurrentTabByTag(TAB_TEST);														
							break;

						case R.id.home_tab_doctor:
							mTabHost.setCurrentTabByTag(TAB_DOCTOR);
							break;								                  							
						case R.id.home_tab_personal:
							mTabHost.setCurrentTabByTag(TAB_PERSONAL);
							break;

						default:
							break;
						}
					}
				});
	}
	
	public void onClickSetting (View v){
		
		Intent intent=new Intent(MainActivity.this,SettingActivity.class);
		startActivity(intent);
	}
	
	
}
