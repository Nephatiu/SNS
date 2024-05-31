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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.hdodenhof.circleimageview.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private boolean exitRepeat = false;
	private String version = "";
	private String build = "";
	private String activeSite = "";
	private String currentPage = "";
	private String homeInstance = "";
	private String myDNS = "";
	private double colorStage = 0;
	private double pageLoad = 0;
	
	private LinearLayout base;
	private LinearLayout inner;
	private LinearLayout header;
	private ProgressBar loadBar;
	private WebView accessportal;
	private TextView settings_btn;
	private CircleImageView sns;
	private TextView title;
	private CircleImageView eurodns;
	private TextView exit_btn;
	
	private TimerTask exitDouble;
	private SharedPreferences memory;
	private Intent go_to = new Intent();
	private TimerTask colorLoop;
	private TimerTask loadPage;
	private TimerTask unloadPage;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		base = findViewById(R.id.base);
		inner = findViewById(R.id.inner);
		header = findViewById(R.id.header);
		loadBar = findViewById(R.id.loadBar);
		accessportal = findViewById(R.id.accessportal);
		accessportal.getSettings().setJavaScriptEnabled(true);
		accessportal.getSettings().setSupportZoom(true);
		settings_btn = findViewById(R.id.settings_btn);
		sns = findViewById(R.id.sns);
		title = findViewById(R.id.title);
		eurodns = findViewById(R.id.eurodns);
		exit_btn = findViewById(R.id.exit_btn);
		memory = getSharedPreferences("memory", Activity.MODE_PRIVATE);
		
		accessportal.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				loadBar.setVisibility(View.VISIBLE);
				loadPage = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								pageLoad = accessportal.getProgress();
								loadBar.setProgress((int)pageLoad);
								if (pageLoad == 100) {
									loadPage.cancel();
									unloadPage = new TimerTask() {
										@Override
										public void run() {
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													loadBar.setVisibility(View.GONE);
												}
											});
										}
									};
									_timer.schedule(unloadPage, (int)(500));
								}
							}
						});
					}
				};
				_timer.scheduleAtFixedRate(loadPage, (int)(10), (int)(10));
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				
				super.onPageFinished(_param1, _param2);
			}
		});
		
		settings_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				go_to.setAction(Intent.ACTION_VIEW);
				go_to.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(go_to);
				finish();
			}
		});
		
		sns.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "She Need Smo.re [Admin]");
				title.setText("She Need Smo.re [".concat(version.concat("~b".concat(build.concat("]")))));
				title.setTextColor(0xFFAED581);
				header.setBackgroundColor(0xFF424242);
				accessportal.stopLoading();
				accessportal.loadUrl("https://".concat(homeInstance.concat("/yunohost/admin")));
				activeSite = "sns";
				settings_btn.setVisibility(View.VISIBLE);
				settings_btn.setEnabled(true);
				accessportal.clearHistory();
				colorLoop.cancel();
				colorStage = 1;
				colorLoop = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (colorStage == 1) {
									title.setTextColor(0xFF039BE5);
									colorStage++;
								}
								else {
									if (colorStage == 2) {
										title.setTextColor(0xFFD32F2F);
										colorStage++;
									}
									else {
										if (colorStage == 3) {
											title.setTextColor(0xFFFFEB3B);
											colorStage++;
										}
										else {
											if (colorStage == 4) {
												title.setTextColor(0xFFAED581);
												colorStage = 1;
											}
											else {
												SketchwareUtil.showMessage(getApplicationContext(), "Great job, moron! You broke the app...");
												colorStage = 1;
											}
										}
									}
								}
							}
						});
					}
				};
				_timer.scheduleAtFixedRate(colorLoop, (int)(5000), (int)(1500));
			}
		});
		
		title.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "Resetting...");
				if (activeSite.equals("sns")) {
					accessportal.stopLoading();
					accessportal.clearCache(true);
					accessportal.loadUrl("https://".concat(homeInstance.concat("/yunohost/admin")));
					accessportal.clearHistory();
				}
				else {
					accessportal.stopLoading();
					accessportal.clearCache(true);
					accessportal.loadUrl("https://".concat(myDNS));
					accessportal.clearHistory();
				}
				return true;
			}
		});
		
		title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "Reloading...");
				currentPage = accessportal.getUrl();
				accessportal.stopLoading();
				accessportal.clearCache(true);
				accessportal.loadUrl(currentPage);
			}
		});
		
		eurodns.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "DNS");
				colorLoop.cancel();
				title.setText("DNS [She Need Smo.re]");
				title.setTextColor(0xFF03A9F4);
				header.setBackgroundColor(0xFF03A9F4);
				accessportal.stopLoading();
				accessportal.loadUrl("https://".concat(myDNS));
				activeSite = "dns";
				settings_btn.setEnabled(false);
				settings_btn.setVisibility(View.INVISIBLE);
				accessportal.clearHistory();
			}
		});
		
		exit_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_doubleExit();
			}
		});
	}
	
	private void initializeLogic() {
		_enableJavascript();
		version = "v1.00";
		build = "23";
		exitRepeat = false;
		activeSite = "sns";
		setTitle("She Need Smo.re");
		loadBar.setBackground(new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] {0xFFB71C1C,0xFF4CAF50}));
		memory.edit().putString("Version", version).commit();
		memory.edit().putString("Build", build).commit();
		if (memory.contains("Instance")) {
			homeInstance = memory.getString("Instance", "");
			if (memory.contains("DNS")) {
				myDNS = memory.getString("DNS", "");
			}
			else {
				go_to.setAction(Intent.ACTION_VIEW);
				go_to.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(go_to);
				finish();
			}
		}
		else {
			go_to.setAction(Intent.ACTION_VIEW);
			go_to.setClass(getApplicationContext(), SettingsActivity.class);
			startActivity(go_to);
			finish();
		}
		loadBar.setVisibility(View.GONE);
		title.setText("She Need Smo.re [".concat(version.concat("~b".concat(build.concat("]")))));
		accessportal.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		accessportal.loadUrl("https://".concat(homeInstance.concat("/yunohost/admin")));
		colorStage = 1;
		colorLoop = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (colorStage == 1) {
							title.setTextColor(0xFF039BE5);
							colorStage++;
						}
						else {
							if (colorStage == 2) {
								title.setTextColor(0xFFD32F2F);
								colorStage++;
							}
							else {
								if (colorStage == 3) {
									title.setTextColor(0xFFFFEB3B);
									colorStage++;
								}
								else {
									if (colorStage == 4) {
										title.setTextColor(0xFFAED581);
										colorStage = 1;
									}
									else {
										SketchwareUtil.showMessage(getApplicationContext(), "Great job, moron! You broke the app...");
										colorStage = 1;
									}
								}
							}
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(colorLoop, (int)(5000), (int)(1500));
	}
	
	@Override
	public void onBackPressed() {
		if (accessportal.canGoBack()) {
			accessportal.goBack();
		}
		else {
			_doubleExit();
		}
	}
	public void _enableJavascript() {
		accessportal.getSettings().setJavaScriptEnabled(true);
		accessportal.getSettings().setDomStorageEnabled(true);
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