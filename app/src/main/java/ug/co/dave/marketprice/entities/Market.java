package ug.co.dave.marketprice.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 12/19/2014.
 */
public class Market extends BaseEntity  {
    private String name;

    private String address;

    private List<Vendor> vendors = new ArrayList<>();

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }
}
