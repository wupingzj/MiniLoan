package com.yang.miniloan;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.yang.miniloan.errorhandling.MiniLoanUncaughtExceptionHanlder;
import com.yang.miniloan.login.DummyLoginServiceImpl;
import com.yang.miniloan.login.LoginService;
import com.yang.miniloan.login.SimpleCredential;
import com.yang.miniloan.util.SystemUiHider;

//This version does hide system UIs.
// It simply deletes all code that hids system UIs.
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MiniLoanActivityBak2 extends ActionBarActivity {
	public static final String TAG = "MiniLoanActivity";
	public final static String EXTRA_MESSAGE = "com.yang.miniloan.MiniLoanActivity.MESSAGE";

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	private Thread.UncaughtExceptionHandler androidDefaultEH = Thread.getDefaultUncaughtExceptionHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// MY ERROR HANDLING
		//Thread.UncaughtExceptionHandler androidDefaultEH = Thread.getDefaultUncaughtExceptionHandler();
		Thread.UncaughtExceptionHandler handler = new MiniLoanUncaughtExceptionHanlder(androidDefaultEH);
		Thread.setDefaultUncaughtExceptionHandler(handler);
		// END: MY ERROR HANDLING
		
		
		// delegate to LoginActivity
		//startUpNewActivity(LoginActivity.class);
		// finish

		setContentView(R.layout.activity_mini_loan);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			setupHoneycombOrAbove();
		} else {
			setupSupportV7();
		}
		
		
		
		//hideSetup();
	}

	private void hideSetup() {
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				//if (true) return;
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
					}
					controlsView.animate().translationY(visible ? 0 : mControlsHeight).setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
				}

				if (visible && AUTO_HIDE) {
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.Quote).setOnTouchListener(mDelayHideTouchListener);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		
		//delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	public static String ADMIN = "admin";
	public static String Kevin = "kevin";

	public void onLogin(View view) {
		// Intent intent = new Intent(this, WellcomeActivity.class);
		EditText accountET = (EditText) findViewById(R.id.Account);
		String account = accountET.getText().toString();
		EditText pwdET = (EditText) findViewById(R.id.Password);
		String pwd = pwdET.getText().toString();

		// TODO to be wired using Spring Android
		LoginService loginService = new DummyLoginServiceImpl();
		if (loginService.login(new SimpleCredential(account, pwd))) {
			// TODO log
			Log.i(TAG, "Login succeeded.");
			messageBox("login", "Login succeeded.");

		} else {
			// TODO reject
			Log.i(TAG, "Login failed.");
			messageBox("login", "Login failed.");

			// force an exception.
			throw new RuntimeException("TO BE IMPLEMENTED222");

		}
	}

	// *********************************************************
	// generic dialog, takes in the method name and error message
	// *********************************************************
	private void messageBox(String method, String message) {
		Log.d("Message: " + method, message);

		AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
		messageBox.setTitle(method);
		messageBox.setMessage(message);
		messageBox.setCancelable(false);
		messageBox.setNeutralButton("OK", null);
		messageBox.show();
	}
	
	public void onSendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.Password);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	// *********************** action bar ***********************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.miniloan_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            openSearch();
	            return true;
	        case R.id.action_settings:
	            openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	// display Up action button
	private void setupSupportV7() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupHoneycombOrAbove() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void openSettings() {
		messageBox("Search", "Show settings ...");
	}

	private void openSearch() {
		messageBox("Search", "Search in action...");
	}
}
