package ug.co.dave.marketprice.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 12/19/2014.
 */
public class Vendor extends BaseEntity {
    private String fullName;

    private String username;

    private String password;

    private int marketId;

    private Market market;

    private List<VendorCommodity> vendorCommodities = new ArrayList<>();

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public List<VendorCommodity> getVendorCommodities() {
        return vendorCommodities;
    }

    public void setVendorCommodities(List<VendorCommodity> vendorCommodities) {
        this.vendorCommodities = vendorCommodities;
    }
}
