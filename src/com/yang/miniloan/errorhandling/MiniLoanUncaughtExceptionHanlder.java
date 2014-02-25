package com.yang.miniloan.errorhandling;

import java.lang.Thread.UncaughtExceptionHandler;

import android.util.Log;

import com.yang.miniloan.MiniLoanActivity;

public class MiniLoanUncaughtExceptionHanlder implements UncaughtExceptionHandler {
	Thread.UncaughtExceptionHandler androidDefaultEH;

	private static UncaughtExceptionHandler androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();

	public MiniLoanUncaughtExceptionHanlder(UncaughtExceptionHandler androidDefaultEH) {
		this.androidDefaultEH = androidDefaultEH;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e(MiniLoanActivity.TAG, "Uncaught Exception:", ex);

		// TODO should I call default EH?
		androidDefaultUEH.uncaughtException(thread, ex);
	}
}
