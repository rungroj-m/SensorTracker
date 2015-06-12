package com.example.sensortracker.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class ConnectDate extends AsyncTask<String, Integer, ArrayList<String>> {

	protected ProgressDialog loading;
	private final String URL;
	private static String sensor;
	private final static String link = "http://ime.ist.hokudai.ac.jp/~yamamoto/xbee/xbee-download-by-day.cgi?";
	public static ArrayList<String> getURLandName(String str) {
		Log.d("title","in getURLandNAme");
		ArrayList<String> ans = new ArrayList<String>();
		Pattern pattern = Pattern.compile("<TR><TD> (.*?) </TD>");
	    Matcher matcher = pattern.matcher(str);
	    while (matcher.find()) {
	    	String ymd = matcher.group(1);
	    	Log.d("title",ymd);
	        ans.add(link+sensor+"&ymd="+ymd+">"+ymd);
	    }
		return ans;
	}
	
	public ConnectDate(Activity activity,String URL){
		loading = new ProgressDialog(activity);
		loading.setTitle("Sensor");
		loading.setMessage("loading ... ");
		loading.setCancelable(false);
		loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.URL = URL;
		sensor = URL.substring(URL.indexOf("addr="));
		Log.d("sensor",sensor);
	}

	@Override
	protected ArrayList<String> doInBackground(String... s) {
		try{
		Log.d("title", URL);
		URL oracle = new URL(URL);
		URLConnection yc = oracle.openConnection();
		Log.d("title","can connect");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String inputLine;
		String temp = "";
		while ((inputLine = in.readLine()) != null) {
			temp += inputLine;
		}
		in.close();
		return getURLandName(temp);	
		}
		catch(Exception e){
			return null;
		}
		
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		loading.dismiss();
	}

	@Override
	protected void onPreExecute() {
		loading.show();
		super.onPreExecute();
	}

}