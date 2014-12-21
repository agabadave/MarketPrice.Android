package ug.co.dave.marketprice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

/**
 * Created by dave on 12/21/2014.
 */
public class Logout extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.logout_message).setTitle(R.string.logout)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //remove the settings..
                        PreferenceClass.writeInteger(Logout.this.getActivity(), PreferenceClass.LOGGED_IN_ID, 0);
                        PreferenceClass.writeString(Logout.this.getActivity(), PreferenceClass.LOGGED_IN_USERNAME, null);

                        //remove backstack and redirect to home...
                        Intent intent = new Intent(Logout.this.getActivity(), MarketActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        //logged out message...
                        Toast.makeText(Logout.this.getActivity(), getString(R.string.been_logged_out), Toast.LENGTH_LONG).show();

                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();

    }
}
