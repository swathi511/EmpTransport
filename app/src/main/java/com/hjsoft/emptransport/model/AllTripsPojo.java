package com.hjsoft.emptransport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hjsoft on 22/6/17.
 */
public class AllTripsPojo {

    @SerializedName("tripid")
    @Expose
    private String tripid;
    @SerializedName("tripdate")
    @Expose
    private String tripdate;
    @SerializedName("RouteName")
    @Expose
    private String routeName;
    @SerializedName("roosterno")
    @Expose
    private String roosterno;
    @SerializedName("tripstatus")
    @Expose
    private String tripstatus;
    @SerializedName("modifieddate")
    @Expose
    private String modifieddate;
    @SerializedName("drivermobile")
    @Expose
    private String drivermobile;
    @SerializedName("driverdutystatus")
    @Expose
    private String driverdutystatus;
    @SerializedName("gpsroutedetails")
    @Expose
    private String gpsroutedetails;
    @SerializedName("gpstotkms")
    @Expose
    private Integer gpstotkms;
    @SerializedName("gpdtothrs")
    @Expose
    private Integer gpdtothrs;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("vehcategory")
    @Expose
    private String vehcategory;

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }

    public String getTripdate() {
        return tripdate;
    }

    public void setTripdate(String tripdate) {
        this.tripdate = tripdate;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRoosterno() {
        return roosterno;
    }

    public void setRoosterno(String roosterno) {
        this.roosterno = roosterno;
    }

    public String getTripstatus() {
        return tripstatus;
    }

    public void setTripstatus(String tripstatus) {
        this.tripstatus = tripstatus;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public String getDrivermobile() {
        return drivermobile;
    }

    public void setDrivermobile(String drivermobile) {
        this.drivermobile = drivermobile;
    }

    public String getDriverdutystatus() {
        return driverdutystatus;
    }

    public void setDriverdutystatus(String driverdutystatus) {
        this.driverdutystatus = driverdutystatus;
    }

    public String getGpsroutedetails() {
        return gpsroutedetails;
    }

    public void setGpsroutedetails(String gpsroutedetails) {
        this.gpsroutedetails = gpsroutedetails;
    }

    public Integer getGpstotkms() {
        return gpstotkms;
    }

    public void setGpstotkms(Integer gpstotkms) {
        this.gpstotkms = gpstotkms;
    }

    public Integer getGpdtothrs() {
        return gpdtothrs;
    }

    public void setGpdtothrs(Integer gpdtothrs) {
        this.gpdtothrs = gpdtothrs;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }

    public String getVehcategory() {
        return vehcategory;
    }

    public void setVehcategory(String vehcategory) {
        this.vehcategory = vehcategory;
    }
}
