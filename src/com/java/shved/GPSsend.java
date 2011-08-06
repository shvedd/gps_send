package com.java.shved;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
   	
public class GPSsend extends Activity {
	
	private LocationManager lm;
	private LocationListener locationListener;
	
	TextView tv2;
	TextView tv3;
	TextView tv4;
	TextView tv6;
	TextView tv7;
	Button sbmt;
	Button rdy;
	EditText etmail;
	EditText eturl;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);              
        
        tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);
        tv4 = (TextView)findViewById(R.id.textView4);
        tv6 = (TextView)findViewById(R.id.textView6);
        tv7 = (TextView)findViewById(R.id.textView7);
        etmail = (EditText)findViewById(R.id.editText1);
        eturl = (EditText)findViewById(R.id.editText2);
        sbmt = (Button)findViewById(R.id.button1);
        rdy = (Button)findViewById(R.id.button2);                	
        
        	sbmt.setOnClickListener (new OnClickListener()
        	{
        		public void onClick(View v)
        		{
        			if(etmail.length()==0)
        			{
        				sbmt.setText("Wrong, try again!");        				
        			}
        			else if(eturl.length()==0)
        			{
        				sbmt.setText("Wrong, try again!");
        			}
        			else
        			{
        				sbmt.setText("Success!");
        				sbmt.setEnabled(false);
        				etmail.setEnabled(false);
        				eturl.setEnabled(false);
        				rdy.setEnabled(true);
        			}
        		}
        	});        	        	
    		rdy.setOnClickListener(new OnClickListener()
        	{
        		public void onClick(View v)
        		{
        			rdy.setEnabled(false);
                	new CountDownTimer(60000, 1000)
                	{
                		public void onTick(long millisUntilFinished){
                			tv7.setText(""+millisUntilFinished / 1000);
                		}
                		public void onFinish(){
                			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                			onConn();
                			start();
                		}                		
                	}.start();                	
        			//lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        			//onConn();        			        			                	
        		}
        	});
    		
        lm = (LocationManager)
        		getSystemService(Context.LOCATION_SERVICE);
        locationListener = new NewLocationListener();

    }
    
    private class NewLocationListener implements LocationListener
    {

		@Override
		public void onLocationChanged(Location location) {
			tv2.setText(""+location.getLatitude());
			tv3.setText(""+location.getLongitude());			
		}

		@Override
		public void onProviderDisabled(String provider) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
    
    public void onConn()
    {
    	//if(tv2.length()>0)
    	//{
    	String lat =(""+tv2.getText()); 
    	String lon =(""+tv3.getText());
    	String elmail =(""+etmail.getText());
    	String url =(eturl.getText()+"?lat="+lat+"&long="+lon+"&email="+elmail);
    	    
    		tv4.setText(url);

    		HttpClient httpClient = new DefaultHttpClient();
    		HttpGet httpGet = new HttpGet(url);
    		ResponseHandler<String> responseHandler = new BasicResponseHandler();
    		Handler handler = new Handler()
    		{
    		};
    		
    			try
    			{
    				httpClient.execute(httpGet, responseHandler).toString();
    				httpClient.getConnectionManager().shutdown();
    				handler.sendEmptyMessage(0);
    			}
    			catch(ClientProtocolException e)
    			{
    				tv4.setText(""+e.getMessage());
    				handler.sendEmptyMessage(1);
    			}
    			catch(IOException e)
    			{
    				tv4.setText(""+e.getMessage());
    				handler.sendEmptyMessage(1);
    			}
    		
    	//}
    }    
}
