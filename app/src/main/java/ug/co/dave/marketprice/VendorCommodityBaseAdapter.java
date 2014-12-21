package ug.co.dave.marketprice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ug.co.dave.marketprice.entities.Vendor;
import ug.co.dave.marketprice.entities.VendorCommodity;

/**
 * Created by dave on 12/20/2014.
 */
public class VendorCommodityBaseAdapter extends BaseAdapter {
    private Vendor vendor;
    private Context context;

    public VendorCommodityBaseAdapter(Vendor vendor, Context context) {
        this.vendor = vendor;
        this.context = context;
    }

    @Override
    public int getCount() {
        return vendor.getVendorCommodities().size();
    }

    @Override
    public Object getItem(int position) {
        return vendor.getVendorCommodities().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VendorCommodity vendorCommodity = vendor.getVendorCommodities().get(position);
        VendorCommodityHolder holder = null;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.vendor_commodity, null);

            holder = new VendorCommodityHolder();
            holder.title = (TextView)convertView.findViewById(R.id.vendor_commodity_title);
            holder.value = (TextView)convertView.findViewById(R.id.vendor_commodity_price);
            holder.unitOfSale = (TextView)convertView.findViewById(R.id.vendor_commodity_unit_sale);

            convertView.setTag(holder);

        }

        else
            holder = (VendorCommodityHolder)convertView.getTag();

        //now passing the paramters...
        holder.title.setText(vendorCommodity.getCommodity().getName());
        holder.value.setText(Double.toString(vendorCommodity.getPrice()));
        holder.unitOfSale.setText(vendorCommodity.getCommodity().getUnitOfSale());

        return convertView;
    }

    private static class VendorCommodityHolder{
        public TextView title;
        public TextView value;
        public TextView unitOfSale;
    }
}
