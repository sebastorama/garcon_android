package br.usp.icmc.redesmoveis.garcon;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	private String tableNumber;
	private String apiUrl = "http://192.168.1.105:3000";
	protected ArrayList<MenuItem> myMenuItems = new ArrayList<MenuItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_list_view);
		this.tableNumber = getIntent().getExtras().getString("table_number");		
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
		super.onDestroy();
	}
	
	public void okAlertDialog(String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(":D");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
	}
	
	public void orderItemAPICall(View v) {
		String itemId = (String) v.getTag();
		System.out.println("Item id: " + itemId);

		String URL = this.apiUrl + "/order.json";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("table_number", this.tableNumber);
		params.put("item_id", itemId);
		
		JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
			       new Response.Listener<JSONObject>() {
			           @Override
			           public void onResponse(JSONObject response) {
			               try {
			                   VolleyLog.v("Response:%n %s", response.toString(4));
			                   okAlertDialog("Pedido Realizado com Sucesso");
			                   
			               } catch (JSONException e) {
			                   e.printStackTrace();
			               }
			           }
			       }, new Response.ErrorListener() {
			           @Override
			           public void onErrorResponse(VolleyError error) {
			               VolleyLog.e("Error: ", error.toString());
			               okAlertDialog("Problemas detectados com seu pedido, tente novamente.");
			           }
			       });
		
		this.getRequestQueue().add(req);

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
			                   populateListView();
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
	
	public void checkOrderStatus() {
		
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
			super(MenuActivity.this, 0, myMenuItems);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Make sure we have a view to work with
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.menu_item_view, parent, false);
			}
			
			// Find the menu item to work with.
			MenuItem currentMenuItem = getItem(position);
			
			// Fill the view
			TextView menuItemNameView = (TextView)itemView.findViewById(R.id.menu_item_name);
			TextView menuItemPriceView = (TextView)itemView.findViewById(R.id.menu_item_price);
			Button menuItemOrderButton = (Button)itemView.findViewById(R.id.menu_item_order_button);
			
			menuItemNameView.setText(currentMenuItem.getName());
			menuItemPriceView.setText(currentMenuItem.getFormattedPrice());
			menuItemOrderButton.setTag(currentMenuItem.getId());
			
			return itemView;
		}		
	}
}
