package com.pTricKg.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts from a
 * {@link Cursor} to a {@link ListView}.
 * 
 * FRACK THIS SHOOTSS
 */
public class ForecastAdapter extends CursorAdapter {

	private final int VIEW_TYPE_TODAY = 0;
	private final int VIEW_TYPE_FUTURE_DAY = 1;

	public ForecastAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public int getItemViewType(int position) {
		return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		int viewType = getItemViewType(cursor.getPosition());
		int layoutId = -1;
		switch (viewType) {
		case VIEW_TYPE_TODAY: {
			layoutId = R.layout.list_item_forecast_today;
			break;
		}
		case VIEW_TYPE_FUTURE_DAY: {
			layoutId = R.layout.list_item_forecast;
			break;
		}
		}
		
		View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		view.setTag(viewHolder);
		return view;
		
		//return LayoutInflater.from(context).inflate(layoutId, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		// swapping viewHolder for findViewByIds
		ViewHolder viewHolder = (ViewHolder) view.getTag();

		// Read weather icon ID from cursor
		int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID);
		// Use placeholder image for now
		//ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
//		iconView.setImageResource(R.drawable.ic_launcher);
		viewHolder.iconView.setImageResource(R.drawable.ic_launcher);;
		// Read date from cursor
		String dateString = cursor.getString(ForecastFragment.COL_WEATHER_DATE);
		// Find TextView and set formatted date on it
		//TextView dateView = (TextView) view
//				.findViewById(R.id.list_item_date_textview);
//		dateView.setText(Utility.getFriendlyDayString(context, dateString));
		
		viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateString));
		// Read weather forecast from cursor
//		String description = cursor
//				.getString(ForecastFragment.COL_WEATHER_DESC);
		// Find TextView and set weather forecast on it
		//TextView descriptionView = (TextView) view
		//	.findViewById(R.id.list_item_forecast_textview);
//		descriptionView.setText(description);
		
		String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
		viewHolder.descriptionView.setText(description);
		// Read user preference for metric or imperial temperature units
		boolean isMetric = Utility.isMetric(context);

		// Read high temperature from cursor
		double high = cursor.getFloat(ForecastFragment.COL_WEATHER_MAX_TEMP);
		// TODO: Find TextView and set formatted high temperature on it
		//TextView highView = (TextView) view
//				.findViewById(R.id.list_item_high_textview);
//		highView.setText(Utility.formatTemperature(high, isMetric));
		viewHolder.highTempView.setText(Utility.formatTemperature(high, isMetric));
		// Read low temperature from cursor
		double low = cursor.getFloat(ForecastFragment.COL_WEATHER_MIN_TEMP);
		// TODO: Find TextView and set formatted low temperature on it
//		TextView lowView = (TextView) view
//				.findViewById(R.id.list_item_low_textview);
//		lowView.setText(Utility.formatTemperature(low, isMetric));
		viewHolder.lowTempView.setText(Utility.formatTemperature(low, isMetric));
	}

	public static class ViewHolder {
		public final ImageView iconView;
		public final TextView dateView;
		public final TextView descriptionView;
		public final TextView highTempView;
		public final TextView lowTempView;

		public ViewHolder(View view) {
			iconView = (ImageView) view.findViewById(R.id.list_item_icon);
			dateView = (TextView) view
					.findViewById(R.id.list_item_date_textview);
			descriptionView = (TextView) view
					.findViewById(R.id.list_item_forecast_textview);
			highTempView = (TextView) view
					.findViewById(R.id.list_item_high_textview);
			lowTempView = (TextView) view
					.findViewById(R.id.list_item_low_textview);

		}
	}
}
