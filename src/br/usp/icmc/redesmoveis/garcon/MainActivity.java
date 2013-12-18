package br.usp.icmc.redesmoveis.garcon;


import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {
	private String apiUrl = "http://192.168.1.105:3000";
	private String tableNumber;
	private RequestQueue mRequestQueue;
	private ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(this);	
		}
		
		return mRequestQueue;
	}
	
	public void getMenuAPICall() {
		String resource = "/menu.json";
		String URL = this.apiUrl + resource;
		
		JsonObjectRequest req = new JsonObjectRequest(URL, null,
			       new Response.Listener<JSONObject>() {
			           @Override
			           public void onResponse(JSONObject response) {
			               try {
			                   VolleyLog.v("Response:%n %s", response.toString(4));
			                   fillMenu(response);
			               } catch (JSONException e) {
			                   e.printStackTrace();
			               }
			           }
			       }, new Response.ErrorListener() {
			           @Override
			           public void onErrorResponse(VolleyError error) {
			               VolleyLog.e("Error: ", error.toString());
			           }
			       });
	}
	
	public void fillMenu(JSONObject menu) {
		
	}
	
	public void assignTable(String tableNumber, JSONObject response) {
		this.tableNumber = tableNumber;
		String status = null;
		try {
			status = response.getString("status");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(status.equals("success")) {
			System.out.println("Assigned Table Number: " + this.tableNumber);
			Intent i = new Intent(this, MenuActivity.class);
			startActivity(i);
			
		} else {
			System.out.println("Table Not Assigned =/");
			
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Mesa est‡ ocupada");
            dlgAlert.setTitle("Ops =/");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            
            this.tableNumber = null;
		}
	}
	
	public void assignTableAPICall(View view) {
		EditText tableNumberText = (EditText) findViewById(R.id.table_number);
		final String tableNumber = tableNumberText.getText().toString(); 
		String URL = this.apiUrl + "/get_table.json";
		HashMap <String, String> params = new HashMap <String, String>();
		params.put("table_number", tableNumber);
		System.out.println(URL);
		
		// Hide the keyboard
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(tableNumberText.getWindowToken(), 0);
		
		// Create the request to the API server
		JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
			       new Response.Listener<JSONObject>() {
			           @Override
			           public void onResponse(JSONObject response) {
			               try {
			                   VolleyLog.v("Response:%n %s", response.toString(4));
			                   assignTable(tableNumber, response);
			               } catch (JSONException e) {
			                   e.printStackTrace();
			               }
			           }
			       }, new Response.ErrorListener() {
			           @Override
			           public void onErrorResponse(VolleyError error) {
			               VolleyLog.e("Error: ", error.toString());
			           }
			       });
		
		// Queue the request to Volley
		this.getRequestQueue().add(req);
		
	}
	
//	private class MenuListAdapter extends ArrayAdapter<Menu> {
//		public MenuListAdapter() {
//			super(MainActivity.this, R.layout.menu_item_view, this.menuItems);
//		}
//		
//	}

}
