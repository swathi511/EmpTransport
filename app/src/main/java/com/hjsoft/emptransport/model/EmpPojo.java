package com.hjsoft.emptransport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hjsoft on 2/6/17.
 */
public class EmpPojo implements Serializable {

    @SerializedName("routeseq")
    @Expose
    private String routeseq;
    @SerializedName("locationname")
    @Expose
    private String locationname;
    @SerializedName("tripemp")
    @Expose
    private List<TripEmpPojo> tripemp = null;

    public String getRouteseq() {
        return routeseq;
    }

    public void setRouteseq(String routeseq) {
        this.routeseq = routeseq;
    }

    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }

    public List<TripEmpPojo> getTripEmpPojo() {
        return tripemp;
    }

    public void setTripEmpPojo(List<TripEmpPojo> tripemp) {
        this.tripemp = tripemp;
    }


}
