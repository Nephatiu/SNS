package re.needsmo.she;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class SettingsActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private boolean exitRepeat = false;
	
	private LinearLayout base;
	private LinearLayout inner;
	private LinearLayout header;
	private LinearLayout optionDialog;
	private TextView settings_btn;
	private TextView title;
	private TextView exit_btn;
	private TextView textview1;
	private TextView textview4;
	private LinearLayout linear2;
	private LinearLayout linear3;
	private TextView textview5;
	private TextView textview2;
	private TextView textview6;
	private EditText homeURL;
	private TextView textview3;
	private TextView textview7;
	private EditText DNSURL;
	
	private Intent go_to = new Intent();
	private SharedPreferences memory;
	private TimerTask exitDouble;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.settings);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		base = findViewById(R.id.base);
		inner = findViewById(R.id.inner);
		header = findViewById(R.id.header);
		optionDialog = findViewById(R.id.optionDialog);
		settings_btn = findViewById(R.id.settings_btn);
		title = findViewById(R.id.title);
		exit_btn = findViewById(R.id.exit_btn);
		textview1 = findViewById(R.id.textview1);
		textview4 = findViewById(R.id.textview4);
		linear2 = findViewById(R.id.linear2);
		linear3 = findViewById(R.id.linear3);
		textview5 = findViewById(R.id.textview5);
		textview2 = findViewById(R.id.textview2);
		textview6 = findViewById(R.id.textview6);
		homeURL = findViewById(R.id.homeURL);
		textview3 = findViewById(R.id.textview3);
		textview7 = findViewById(R.id.textview7);
		DNSURL = findViewById(R.id.DNSURL);
		memory = getSharedPreferences("memory", Activity.MODE_PRIVATE);
		
		settings_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (memory.contains("Instance")) {
					if (memory.contains("DNS")) {
						go_to.setAction(Intent.ACTION_VIEW);
						go_to.setClass(getApplicationContext(), MainActivity.class);
						startActivity(go_to);
						finish();
					}
					else {
						SketchwareUtil.showMessage(getApplicationContext(), "Please save a valid configuration first!");
					}
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Please save a valid configuration first!");
				}
			}
		});
		
		exit_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_doubleExit();
			}
		});
		
		textview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		textview5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (homeURL.getText().toString().contains(".")) {
					if (DNSURL.getText().toString().contains(".")) {
						memory.edit().putString("Instance", homeURL.getText().toString()).commit();
						memory.edit().putString("DNS", DNSURL.getText().toString()).commit();
						SketchwareUtil.showMessage(getApplicationContext(), "Settings saved successfully!");
					}
					else {
						SketchwareUtil.showMessage(getApplicationContext(), "Please enter a valid URL for your DNS services too...");
					}
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Please enter a valid server URL first!");
				}
			}
		});
	}
	
	private void initializeLogic() {
		exitRepeat = false;
		if (memory.contains("Instance")) {
			if (memory.contains("DNS")) {
				DNSURL.setText(memory.getString("DNS", ""));
			}
			homeURL.setText(memory.getString("Instance", ""));
		}
	}
	
	@Override
	public void onBackPressed() {
		if (memory.contains("Instance")) {
			if (memory.contains("DNS")) {
				go_to.setAction(Intent.ACTION_VIEW);
				go_to.setClass(getApplicationContext(), MainActivity.class);
				startActivity(go_to);
				finish();
			}
			else {
				SketchwareUtil.showMessage(getApplicationContext(), "Please save a valid configuration first!");
			}
		}
		else {
			SketchwareUtil.showMessage(getApplicationContext(), "Please save a valid configuration first!");
		}
	}
	public void _doubleExit() {
		if (exitRepeat) {
			finishAffinity();
		}
		else {
			exitRepeat = true;
			SketchwareUtil.showMessage(getApplicationContext(), "Once more within 2 seconds to actually exit...");
			exitDouble = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							exitRepeat = false;
						}
					});
				}
			};
			_timer.schedule(exitDouble, (int)(2000));
		}
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}