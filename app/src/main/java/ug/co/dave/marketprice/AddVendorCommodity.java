package ug.co.dave.marketprice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ug.co.dave.marketprice.entities.Commodity;
import ug.co.dave.marketprice.entities.VendorCommodity;


public class AddVendorCommodity extends BaseActivity {

    private EditText costEditText;
    private Spinner commoditySpiner;
    private Button saveButton;

    private List<Commodity> commodities;

    public static String VENDOR_COMMODITY_ID = "vendorCommodityId";

    private VendorCommodity commodity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //enabling the progress dialog...
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor_commodity);

        //setting views...
        costEditText = (EditText)findViewById(R.id.add_vendor_commodity_cost);
        commoditySpiner = (Spinner)findViewById(R.id.add_vendor_commodity_market);
        saveButton = (Button)findViewById(R.id.add_vendor_commodity_save);

        //while edit we pass vendorCommdity selected...
        if(getIntent().hasExtra(VENDOR_COMMODITY_ID)){
            int id = getIntent().getIntExtra(VENDOR_COMMODITY_ID, 0);
            if(id > 0){
                getNetworkService().vendorCommodity(id, new Callback<VendorCommodity>() {
                    @Override
                    public void success(VendorCommodity vendorCommodity, Response response) {
                        AddVendorCommodity.this.commodity = vendorCommodity;
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
        //getting markets and assigning them the spinner...
        loadMarketsInSpinner();

        saveButton.setOnClickListener(saveListner);
    }

    private void loadMarketsInSpinner(){
        setProgressBarIndeterminateVisibility(true);

        getNetworkService().commodities(new Callback<List<Commodity>>() {
            @Override
            public void success(List<Commodity> commodities, Response response) {
                //we have the markets so lets do the needful..
                AddVendorCommodity.this.commodities = commodities;

                List<String> commodityNames = new ArrayList<String>();
                for (Commodity m : commodities)
                    commodityNames.add(m.getName());

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AddVendorCommodity.this,
                        android.R.layout.simple_expandable_list_item_1, commodityNames);
                AddVendorCommodity.this.commoditySpiner.setAdapter(spinnerAdapter);

                //setting the default spinner item...
                if(commodity != null){
                    String selectedCommodity = commodity.getCommodity().getName();
                    int setPosition = spinnerAdapter.getPosition(selectedCommodity);
                    //now we set the spinner position
                    commoditySpiner.setSelection(setPosition);

                    //setting the cost as well..
                    costEditText.setText(Double.toString(commodity.getPrice()));

                }

                //stop the dialog from loading..
                setProgressBarIndeterminateVisibility(false);

            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(AddVendorCommodity.this, getString(R.string.no_markets), Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);

            }
        });
    }

    View.OnClickListener saveListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //add a restriction to ensure that the cost is added..
            if(costEditText.getText().toString().trim().equals("")){
                Toast.makeText(AddVendorCommodity.this, getString(R.string.no_cost_error), Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                if(commodity == null)
                    commodity = new VendorCommodity();

                //getting the vendor to save..
                commodity.setVendorId(PreferenceClass.readInteger(AddVendorCommodity.this, PreferenceClass.LOGGED_IN_ID, 0));
                commodity.setPrice(Double.parseDouble(costEditText.getText().toString().trim()));

                //now get the value of selected product...
                commodity.setCommodityId(commodities.get(commoditySpiner.getSelectedItemPosition()).getId());

                //otherwise lets save.. :)
                setProgressBarIndeterminateVisibility(true);
                getNetworkService().saveVendorCommodity(commodity, new Callback<VendorCommodity>() {
                    @Override
                    public void success(VendorCommodity vendorCommodity, Response response) {
                        if(vendorCommodity.getId() > 0){
                            Toast.makeText(AddVendorCommodity.this, getString(R.string.commodity_vendor_successful), Toast.LENGTH_SHORT).show();

                            //redirect back to the vendor..
                            Intent intent = new Intent(AddVendorCommodity.this, VendorActivity.class);
                            intent.putExtra(VendorActivity.VENDOR_ID, commodity.getVendorId());
                            startActivity(intent);

                        }
                        else
                            Toast.makeText(AddVendorCommodity.this, getString(R.string.add_commodity_vendor_error), Toast.LENGTH_SHORT).show();

                        setProgressBarIndeterminateVisibility(false);
                        return;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(AddVendorCommodity.this, getString(R.string.commodity_vendor_network_error), Toast.LENGTH_LONG).show();
                        setProgressBarIndeterminateVisibility(false);
                        return;
                    }
                });

            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(AddVendorCommodity.this, getString(R.string.add_commodity_vendor_error), Toast.LENGTH_LONG).show();
                return;
            }
        }
    };

}
