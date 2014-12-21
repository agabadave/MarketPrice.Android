package ug.co.dave.marketprice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ug.co.dave.marketprice.entities.Vendor;
import ug.co.dave.marketprice.entities.VendorCommodity;


public class VendorActivity extends BaseActivity {

    public static final String VENDOR_ID = "vendorId";

    public Vendor vendor;
    public ListView vendorCommoditiesListView;
    public VendorCommodityBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        vendorCommoditiesListView = (ListView)findViewById(R.id.vendor_commodities);

        int id = getIntent().getIntExtra(VENDOR_ID, 0);

        RetrieveVendor retriever = new RetrieveVendor();
        retriever.execute(id);

    }

    private class RetrieveVendor extends AsyncTask<Integer, Void, Vendor>{
        ProgressDialog progressDialog;

        public RetrieveVendor(){
            progressDialog = new ProgressDialog(VendorActivity.this);
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
        protected void onPostExecute(final Vendor vendor) {
            super.onPostExecute(vendor);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            if(vendor == null){
                Toast.makeText(VendorActivity.this, getString(R.string.no_vendor), Toast.LENGTH_LONG).show();
                return;
            }

            if(vendor.getVendorCommodities().isEmpty())
                Toast.makeText(VendorActivity.this, getString(R.string.vendor_no_commodities), Toast.LENGTH_LONG).show();

            adapter = new VendorCommodityBaseAdapter(vendor, VendorActivity.this);
            vendorCommoditiesListView.setAdapter(adapter);

            //setting the vendor
            VendorActivity.this.setTitle(vendor.getFullName());

            //implementing the edit at this point...
            //check if user is logged in and is the same as this one..
            int vendorId = PreferenceClass.readInteger(VendorActivity.this, PreferenceClass.LOGGED_IN_ID, 0);
            if(vendorId > 0 && vendorId == vendor.getId()){
                vendorCommoditiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        VendorCommodity selectedVc = vendor.getVendorCommodities().get(position);
                        if(selectedVc != null){
                            Intent editVc = new Intent(VendorActivity.this, AddVendorCommodity.class);
                            editVc.putExtra(AddVendorCommodity.VENDOR_COMMODITY_ID, selectedVc.getId());
                            startActivity(editVc);
                        }
                    }
                });
            }

        }

        @Override
        protected Vendor doInBackground(Integer... params) {
            int id = params[0];

            try {
                vendor = getNetworkService().vendor(id);
                return vendor;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
