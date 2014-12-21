package ug.co.dave.marketprice;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import ug.co.dave.marketprice.R;

/**
 * Created by dave on 12/21/2014.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected INetworkService getNetworkService(){
        return ((MarketPriceApplication)this.getApplication()).getNetworkService();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_market, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        String loggedInUser = PreferenceClass.readString(this, PreferenceClass.LOGGED_IN_USERNAME, null);
        if(loggedInUser == null){
            menu.removeItem(R.id.action_logout);
            menu.removeItem(R.id.action_add_vendor_commodity);
            menu.removeItem(R.id.action_my_commodities);
        }
        else
            menu.removeItem(R.id.action_login);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_login:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;

            case R.id.action_my_commodities:
                int id = PreferenceClass.readInteger(this, PreferenceClass.LOGGED_IN_ID, 0);
                if(id > 0){
                    Intent intent = new Intent(this, VendorActivity.class);
                    intent.putExtra(VendorActivity.VENDOR_ID, id);
                    startActivity(intent);
                }
                break;
            case R.id.action_logout:
                DialogFragment logoutDialog = new Logout();
                logoutDialog.show(getSupportFragmentManager(), "LogoutDialog");

                break;

            case R.id.action_add_vendor_commodity:
                Intent addVendorCommodityIntent = new Intent(this, AddVendorCommodity.class);
                startActivity(addVendorCommodityIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
