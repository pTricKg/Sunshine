package com.pTricKg.sunshine;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
 
public class Utility {
    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_location_default));
    }

	public static CharSequence formatTemperature(double double1,
			boolean isMetric) {
		// TODO Auto-generated method stub
		return null;
	}

	public static CharSequence formatDate(String dateString) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean isMetric(FragmentActivity activity) {
		// TODO Auto-generated method stub
		return false;
	}
}
