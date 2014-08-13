package com.pTricKg.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class WeatherProvider extends ContentProvider {

	// add constants to help uri querys
	private static final int WEATHER = 100;
	private static final int WEATHER_WITH_LOCATION = 101;
	private static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
	private static final int LOCATION = 300;
	private static final int LOCATION_ID = 301;
	
	// content provider uri matcher
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    
	WeatherDbHelper mOpenHelper;

	private static UriMatcher buildUriMatcher() {
		
		// All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WeatherContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, WeatherContract.PATH_WEATHER, WEATHER);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*/*", WEATHER_WITH_LOCATION_AND_DATE);

        matcher.addURI(authority, WeatherContract.PATH_LOCATION, LOCATION);
        matcher.addURI(authority, WeatherContract.PATH_LOCATION + "/#", LOCATION_ID);

        return matcher;
	}
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mOpenHelper = new WeatherDbHelper(getContext());
		return true;
		
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		
		// used to associate mime type with given uri
		final int match = sUriMatcher.match(uri);
		switch (match) {
        case WEATHER_WITH_LOCATION_AND_DATE:
            return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
        case WEATHER_WITH_LOCATION:
            return WeatherContract.WeatherEntry.CONTENT_TYPE;
        case WEATHER:
            return WeatherContract.WeatherEntry.CONTENT_TYPE;
        case LOCATION_ID:
        	return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
        case LOCATION:
        	return WeatherContract.WeatherEntry.CONTENT_TYPE;
        default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
