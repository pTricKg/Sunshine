package com.pTricKg.sunshine;

import java.util.Date;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pTricKg.sunshine.data.WeatherContract;
import com.pTricKg.sunshine.data.WeatherContract.LocationEntry;
import com.pTricKg.sunshine.data.WeatherContract.WeatherEntry;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView}
 * layout.
 * 
 * WHOOOWEEE
 */
public class ForecastFragment extends Fragment implements
		LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mForecastAdapter;

	private static final int FORECAST_LOADER = 0;
	private String mLocation;

	// For the forecast view we're showing only a small subset of the stored
	// data.
	// Specify the columns we need.
	private static final String[] FORECAST_COLUMNS = {
			// In this case the id needs to be fully qualified with a table
			// name, since
			// the content provider joins the location & weather tables in the
			// background
			// (both have an _id column)
			// On the one hand, that's annoying. On the other, you can search
			// the weather table
			// using the location set by the user, which is only in the Location
			// table.
			// So the convenience is worth it.
			WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
			WeatherEntry.COLUMN_DATETEXT, WeatherEntry.COLUMN_SHORT_DESC,
			WeatherEntry.COLUMN_MAX_TEMP, WeatherEntry.COLUMN_MIN_TEMP,
			LocationEntry.COLUMN_LOCATION_SETTING };

	// These indices are tied to FORECAST_COLUMNS. If FORECAST_COLUMNS changes,
	// these
	// must change.
	public static final int COL_WEATHER_ID = 0;
	public static final int COL_WEATHER_DATE = 1;
	public static final int COL_WEATHER_DESC = 2;
	public static final int COL_WEATHER_MAX_TEMP = 3;
	public static final int COL_WEATHER_MIN_TEMP = 4;
	public static final int COL_LOCATION_SETTING = 5;

	public ForecastFragment() {
	}

	@Override
	// binding loader to activity
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(FORECAST_LOADER, null, this);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Add this line in order for this fragment to handle menu events.
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.forecastfragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			updateWeather();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         
    	// trying out SimpleCursorAdapter
    	// The SimpleCursorAdapter will take data from the database through the
        // Loader and use it to populate the ListView it's attached to.
        mForecastAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item_forecast,
                null,
                // the column names to use to fill the textviews
                new String[]{WeatherContract.WeatherEntry.COLUMN_DATETEXT,
                        WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP
                },
                // the textviews to fill with the data pulled from the columns above
                new int[]{R.id.list_item_date_textview,
                        R.id.list_item_forecast_textview,
                        R.id.list_item_high_textview,
                        R.id.list_item_low_textview
                },
                0
        );
        
        mForecastAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                boolean isMetric = Utility.isMetric(getActivity());
                switch (columnIndex) {
                    case COL_WEATHER_MAX_TEMP:
                    case COL_WEATHER_MIN_TEMP: {
                        // we have to do some formatting and possibly a conversion
                        ((TextView) view).setText(Utility.formatTemperature(
                                cursor.getDouble(columnIndex), isMetric));
                        return true;
                    }
                    case COL_WEATHER_DATE: {
                        String dateString = cursor.getString(columnIndex);
                        TextView dateView = (TextView) view;
                        dateView.setText(Utility.formatDate(dateString));
                        return true;
                    }
                }
                return false;
            }
        });
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				//String forecast = mForecastAdapter.getItem(i); // pull from adapter
				//Toast.makeText(getActivrootView., forecast, Toast.LENGTH_LONG).show();
				SimpleCursorAdapter adapter = (SimpleCursorAdapter) adapterView.getAdapter();
				Cursor cursor = adapter.getCursor();
				if (null != cursor && cursor.moveToPosition(i)) {
					boolean isMetric = Utility.isMetric(getActivity());
					String forecast = String.format("%s - %s - %s/%s",
							Utility.formatDate(cursor.getString(COL_WEATHER_DATE)),
									cursor.getString(COL_WEATHER_DESC),
									Utility.formatTemperature(cursor.getDouble(COL_WEATHER_MAX_TEMP), isMetric),
									Utility.formatTemperature(cursor.getDouble(COL_WEATHER_MIN_TEMP), isMetric));
									
					Intent intent = new Intent(getActivity(), DetailActivity.class)
							.putExtra(DetailActivity.DATE_KEY, cursor.getString(COL_WEATHER_DATE));
					startActivity(intent);
									
				}
				
			}
        	
        });

        return rootView;
    }

	// new update weather method to populate real data
	private void updateWeather() {
		String location = Utility.getPreferredLocation(getActivity());
		new FetchWeatherTask(getActivity()).execute(location);
	}

	@Override
	public void onStart() {
		super.onStart();
		updateWeather();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		// This is called when a new Loader needs to be created. This
		// fragment only uses one loader, so we don't care about checking the
		// id.

		// To only show current and future dates, get the String representation
		// for today,
		// and filter the query to return weather only for dates after or
		// including today.
		// Only return data after today.
		String startDate = WeatherContract.getDbDateString(new Date());

		// Sort order: Ascending, by date.
		String sortOrder = WeatherEntry.COLUMN_DATETEXT + " ASC";

		mLocation = Utility.getPreferredLocation(getActivity());
		Uri weatherForLocationUri = WeatherEntry
				.buildWeatherLocationWithStartDate(mLocation, startDate);

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		return new CursorLoader(getActivity(), weatherForLocationUri,
				FORECAST_COLUMNS, null, null, sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		mForecastAdapter.swapCursor(arg1);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		mForecastAdapter.swapCursor(null);
	}

	

}
