package de.christinecoenen.code.programguide.plugins.parliament;


import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.christinecoenen.code.programguide.ProgramGuideRequest;
import de.christinecoenen.code.programguide.helper.StringRequest;
import de.christinecoenen.code.programguide.model.Channel;
import de.christinecoenen.code.programguide.model.Show;
import de.christinecoenen.code.programguide.plugins.BaseProgramGuideDownloader;

public class ParliamentDownloader extends BaseProgramGuideDownloader {

	private static final String TAG = ParliamentDownloader.class.getSimpleName();
	private static final String XTML_URL_1 = "https://www.bundestag.de/includes/datasources/tv.xml";
	private static final String XTML_URL_2 = "https://www.bundestag.de/includes/datasources/tv2.xml";

	public ParliamentDownloader(RequestQueue queue, Channel channel, ProgramGuideRequest.Listener listener) {
		super(queue, channel, listener);
	}

	@Override
	public void downloadWithoutCache() {
		String url = (channel == Channel.PARLAMENTSFERNSEHEN_1) ? XTML_URL_1 : XTML_URL_2;

		request = new StringRequest(url, "UTF-8", new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, "xml loaded");
						parse(response);
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG,  "error loading xml: " + error.getMessage());
				downloaderListener.onRequestError();
			}
		});

		queue.add(request);
	}

	private void parse(String xml) {
		Show show = ParliamentParser.parse(xml);
		downloaderListener.onRequestSuccess(show);
	}
}
