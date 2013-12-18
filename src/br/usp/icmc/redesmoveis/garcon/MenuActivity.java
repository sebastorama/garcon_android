package br.usp.icmc.redesmoveis.garcon;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class MenuActivity extends Activity {
	private RequestQueue mRequestQueue;
	private String apiUrl = "http://192.168.1.105:3000";
	protected ArrayList<MenuItem> myMenuItems = new ArrayList<MenuItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_list_view);
		populateMenuItemsArrayAPICall();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void populateMenuItemsArray(JSONObject response) throws JSONException {
		JSONArray menuItemsJSON = response.getJSONArray("menu_items");
		int i;
		for(i=0; i < menuItemsJSON.length(); i++) {
			JSONObject currentJSON = menuItemsJSON.getJSONObject(i);
			String currentId = currentJSON.getString("id");
			String currentName = currentJSON.getString("name");
			String currentPrice = currentJSON.getString("price");
			MenuItem menuItem = new MenuItem(currentId, currentName, currentPrice);
			this.myMenuItems.add(menuItem);
		}
		populateListView();
	}
	
	
	public void populateMenuItemsArrayAPICall() {
		String resource = "/menu.json";
		String URL = this.apiUrl + resource;
		
		System.out.println(URL);
		
		JsonObjectRequest req = new JsonObjectRequest(URL, null,
			       new Response.Listener<JSONObject>() {
			           @Override
			           public void onResponse(JSONObject response) {
			               try {
			                   VolleyLog.v("Response:%n %s", response.toString(4));
			                   populateMenuItemsArray(response);
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
		
		this.getRequestQueue().add(req);
	}


	public RequestQueue getRequestQueue() {
		if (this.mRequestQueue == null) {
			this.mRequestQueue = Volley.newRequestQueue(this);	
		}
		
		return this.mRequestQueue;
	}
	
	private void populateListView() {
		ArrayAdapter<MenuItem> adapter = new MenuListAdapter();
		ListView list = (ListView) findViewById(R.id.menuListView);
		list.setAdapter(adapter);
	}
	
	private class MenuListAdapter extends ArrayAdapter<MenuItem> {
		public MenuListAdapter() {
			super(MenuActivity.this, R.layout.menu_item_view);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Make sure we have a view to work with
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.menu_item_view, parent, false);
			}
			
			// Find the menu item to work with.
			MenuItem currentMenuItem = myMenuItems.get(position);
			System.out.println(currentMenuItem.getName());
			
			// Fill the view
			TextView menuItemNameView = (TextView)itemView.findViewById(R.id.menu_item_name);
			TextView menuItemPriceView = (TextView)itemView.findViewById(R.id.menu_item_price);
			
			return itemView;
		}
		
		
	}
}
