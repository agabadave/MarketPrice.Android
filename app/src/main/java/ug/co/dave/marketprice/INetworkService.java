package ug.co.dave.marketprice;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ug.co.dave.marketprice.entities.Commodity;
import ug.co.dave.marketprice.entities.Market;
import ug.co.dave.marketprice.entities.Vendor;
import ug.co.dave.marketprice.entities.VendorCommodity;

/**
 * Created by dave on 12/19/2014.
 */
public interface INetworkService {

    @GET("/markets")
    List<Market> markets();

    @GET("/markets")
    void markets(Callback<List<Market>> markets);

    @GET("/markets")
    void market(@Query("id") int id, Callback<Market> market);

    @GET("/markets")
    Market market(@Query("id") int id);

    @GET("/vendors")
    Vendor vendor(@Query("id") int id);

    @POST("/vendors/Login")
    Vendor Login(@Body LoginActivity.LoginDetails loginDetails);

    @GET("/commodities")
    void commodities(Callback<List<Commodity>> commodities);

    @POST("/VendorCommodities")
    void saveVendorCommodity(@Body VendorCommodity vendorCommodity, Callback<VendorCommodity> vendorCommodityCallback);

    @GET("/VendorCommodities")
    void vendorCommodity(@Query("id") int id, Callback<VendorCommodity> vendorCommodityCallback);
}

