package ug.co.dave.marketprice;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ug.co.dave.marketprice.entities.Vendor;


public class LoginActivity extends ActionBarActivity {

    private EditText username, password;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //enabling the progress dialog...
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.login_username);
        password = (EditText)findViewById(R.id.login_password);
        loginButton = (Button)findViewById(R.id.login_login_button);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if username and password have been entered...
                LoginDetails loginDetails = new LoginDetails();
                loginDetails.username = username.getText().toString().trim();
                loginDetails.password = password.getText().toString().trim();

                if(loginDetails.username.equals("") || loginDetails.password.equals("")){
                    Toast.makeText(LoginActivity.this, getString(R.string.usernmae_password_missing), Toast.LENGTH_LONG).show();
                    return;
                }

                //executing the login proces..
                LoginAsync login = new LoginAsync();
                login.execute(loginDetails);

            }
        });
    }

    public static class LoginDetails{
        public String username;
        public String password;
    }

    private class LoginAsync extends AsyncTask<LoginDetails, Void, Boolean>{

        Vendor vendor;
        @Override
        protected Boolean doInBackground(LoginDetails... params) {
            //getting the vendor, if present..

            try {
                vendor = ((MarketPriceApplication)LoginActivity.this.getApplication()).getNetworkService().Login(params[0]);
                if(vendor != null){
                    PreferenceClass.writeString(LoginActivity.this, PreferenceClass.LOGGED_IN_USERNAME, vendor.getUsername());
                    PreferenceClass.writeInteger(LoginActivity.this, PreferenceClass.LOGGED_IN_ID, vendor.getId());

                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
            loginButton.setEnabled(false); //disable the login button.
            Toast.makeText(LoginActivity.this, getString(R.string.please_wait_load_message), Toast.LENGTH_LONG).show();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            //renabling the buttons..
            loginButton.setEnabled(true);
            setProgressBarIndeterminateVisibility(false);

            //continuing with other works...
            if(aBoolean){
                //redirect to the vendor page..
                 if(vendor != null){
                    Toast.makeText(LoginActivity.this, "Welcome " + vendor.getFullName(), Toast.LENGTH_LONG).show();

                     //now for the redirect...
                    Intent intent = new Intent(LoginActivity.this, VendorActivity.class);
                    intent.putExtra(VendorActivity.VENDOR_ID, vendor.getId());
                    startActivity(intent);
                    return;
                }
            }

            Toast.makeText(LoginActivity.this, getString(R.string.vendor_not_found), Toast.LENGTH_LONG).show();
        }
    }
}
