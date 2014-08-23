package com.pTricKg.sunshine;

import com.pTricKg.sunshine.data.WeatherContract;
import com.pTricKg.sunshine.data.WeatherContract.WeatherEntry;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author ptrickg
 *
 *  Presents detail view of weather upon click on day in main
 *  Activity.
 *  
 */

public class DetailActivity extends ActionBarActivity {
		
	public static final int DETAIL_LOADER = 0;
	public static final String DATE_KEY = "forecast_date";
    private static final String LOCATION_KEY = "location";
	public static final String[] FORECAST_COLUMNS = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

        private ShareActionProvider mShareActionProvider;
        private String mLocation;
        private String mForecast;
        private String mForecastStr;
        
        public static String[] columns = {
		        WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
		        WeatherContract.WeatherEntry.COLUMN_DATETEXT,
		        WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
		        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
		        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
		        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
		        WeatherContract.WeatherEntry.COLUMN_PRESSURE,
		        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
		        WeatherContract.WeatherEntry.COLUMN_DEGREES,
		        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
		        WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
		};
        
        public DetailFragment() {
            setHasOptionsMenu(true);
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            if (savedInstanceState != null) {
                mLocation = savedInstanceState.getString(LOCATION_KEY);
            }
            super.onActivityCreated(savedInstanceState);
      }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	return inflater.inflate(R.layout.fragment_detail, container, false);
//            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//
//            // The detail Activity called via intent.  Inspect the intent for forecast data.
//            Intent intent = getActivity().getIntent();
//            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//                mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
//                ((TextView) rootView.findViewById(R.id.detail_text))
//                        .setText(mForecastStr);
//            }
//
//            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);

            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Get the provider and hold onto it to set/change the share intent.
            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            // Attach an intent to this ShareActionProvider.  You can update this at any time,
            // like when the user selects a new piece of data they might like to share.
            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }

        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    mForecastStr + FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Intent intent = getActivity().getIntent();
            if (intent == null || !intent.hasExtra(DATE_KEY)) {
                return null;
            }
            String forecastDate = intent.getStringExtra(DATE_KEY);

            // Sort order:  Ascending, by date.
            String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATETEXT + " ASC";

            mLocation = Utility.getPreferredLocation(getActivity());
            Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                    mLocation, forecastDate);
           

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    weatherUri,
                    columns,
                    null,
                    null,
                    sortOrder
            );
//			String[] columns = {
//			        WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
//			        WeatherContract.WeatherEntry.COLUMN_DATETEXT,
//			        WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
//			        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
//			        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
//			        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
//			        WeatherContract.WeatherEntry.COLUMN_PRESSURE,
//			        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
//			        WeatherContract.WeatherEntry.COLUMN_DEGREES,
//			        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
//			        WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
//			};
//			
//			
//			
//			mLocation = Utility.getPreferredLocation(getActivity());
//			Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(mLocation, DATE_KEY);
//			
//			return new CursorLoader(
//                    getActivity(),
//                    weatherUri,
//                    columns,
//                    null,
//                    null,
//                    null
//            );
		}

		@Override
		public void onLoadFinished(Loader<Cursor> ooader, Cursor data) {

			if (!data.moveToFirst()) { return; }

            String dateString = Utility.formatDate(
                    data.getString(data.getColumnIndex(WeatherEntry.COLUMN_DATETEXT)));
            ((TextView) getView().findViewById(R.id.detail_date_textview))
                    .setText(dateString);

            String weatherDescription =
                    data.getString(data.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC));
            ((TextView) getView().findViewById(R.id.detail_forecast_textview))
                    .setText(weatherDescription);

            boolean isMetric = Utility.isMetric(getActivity());

            String high = Utility.formatTemperature(
                    data.getDouble(data.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP)), isMetric);
            ((TextView) getView().findViewById(R.id.detail_high_textview)).setText(high);

            String low = Utility.formatTemperature(
                    data.getDouble(data.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP)), isMetric);
            ((TextView) getView().findViewById(R.id.detail_low_textview)).setText(low);

            // We still need this for the share intent
            mForecast = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
			//			// TODO Auto-generated method stub
//			if (!data.moveToFirst()) { return; }
//
//            String dateString = Utility.formatDate(
//                    data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATETEXT)));
//            String description =
//                    data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC));
//            double high = data.getDouble(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
//            double low =  data.getDouble(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
//
//            boolean isMetric = Utility.isMetric(getActivity());
//
//            TextView dateView = (TextView) getView().findViewById(R.id.detail_date_textview);
//            TextView forecastView = (TextView) getView().findViewById(R.id.detail_forecast_textview);
//            TextView highView = (TextView) getView().findViewById(R.id.detail_high_textview);
//            TextView lowView = (TextView) getView().findViewById(R.id.detail_low_textview);
//            
//            dateView.setText(Utility.formatDate(dateString));
//            forecastView.setText(description);
//            highView.setText(Utility.formatTemperature(high, isMetric)+ "\u00B0");
//            lowView.setText(Utility.formatTemperature(low, isMetric) + "\u00B0");
//            
//            mForecastStr = String.format("%s - %s - %s/%s", dateView.getText(), forecastView.getText(),
//            		highView.getText(), lowView.getText());
//            
			
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            if (null != mLocation) {
            	outState.putString(LOCATION_KEY, mLocation);
        } 
        }
		@Override
		public void onResume() {
			super.onResume();
			if (null != mLocation && !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
				getLoaderManager().restartLoader(DETAIL_LOADER, null, null);
			}
    }
    }
}
