package ug.co.dave.marketprice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ug.co.dave.marketprice.entities.Market;
import ug.co.dave.marketprice.entities.Vendor;


public class MarketActivity extends BaseActivity {

    private ExpandableListView expandableListView;
    private List<Market> markets;
    private SimpleExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        //implementing the the expandable adapter..
        expandableListView = (ExpandableListView)findViewById(R.id.market_listview_expandable);

        //nw opening the retirevor..
        RetrieveMarkets retriever = new RetrieveMarkets();
        retriever.execute();

        //implementing on child click listener...
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Vendor selectedVendor = markets.get(groupPosition).getVendors().get(childPosition);

                Intent intent = new Intent(MarketActivity.this, VendorActivity.class);
                intent.putExtra(VendorActivity.VENDOR_ID, selectedVendor.getId());
                startActivity(intent);

                return true;
            }
        });

    }

    private List<Map<String, String>> groupMarkets(List<Market> gMarkets){
        List<Map<String, String>> result = new ArrayList<>();
        if(gMarkets != null){
            for(Market m : gMarkets){
                Map<String, String> map = new HashMap<>();
                map.put("ROOT_NAME", m.getName());
                result.add(map);
            }
        }
        return result;
    }

    private List<List<Map<String, String>>> childVendors(List<Market> gMarkets){
        List<List<Map<String, String>>> result = new ArrayList<>();
        if(gMarkets != null){
            for(Market m : gMarkets){
                List<Map<String, String>> mList = new ArrayList<>();
                for(Vendor v : m.getVendors()){
                    Map<String, String> vMap = new HashMap<>();
                    vMap.put("CHILD_NAME", v.getFullName());
                    mList.add(vMap);
                }
                result.add(mList);
            }
        }
        return result;
    }

    private class RetrieveMarkets extends AsyncTask<Void, Void, List<Market>>{
        ProgressDialog progressDialog;
        boolean isSuccessful;

        public RetrieveMarkets(){
            progressDialog = new ProgressDialog(MarketActivity.this);
        }

        @Override
        protected List<Market> doInBackground(Void... params) {
            try{
                markets = getNetworkService().markets();
                return markets;
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait_load_message));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<Market> markets) {
            super.onPostExecute(markets);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            if(markets == null){
                Toast.makeText(MarketActivity.this, getString(R.string.no_markets), Toast.LENGTH_LONG).show();
                return;
            }
            else if(markets.isEmpty()){
                Toast.makeText(MarketActivity.this, getString(R.string.no_markets), Toast.LENGTH_LONG).show();
                return;
            }
            List<Map<String, String>> parents = groupMarkets(markets);
            List<List<Map<String, String>>> children = childVendors(markets);
            adapter = new SimpleExpandableListAdapter(
                    MarketActivity.this,

                    parents,
                    R.layout.expandable_list_parent_item,
                    new String[] { "ROOT_NAME" },
                    new int[] { R.id.expandable_list_parent_item_parent },

                    children,
                    R.layout.expandable_list_child_item,
                    new String[] { "CHILD_NAME" },
                    new int[] { R.id.expandable_list_child_item_child}
            );

            expandableListView.setAdapter(adapter);
        }
    }
}
