package com.hjsoft.emptransport.model;

import java.io.Serializable;

/**
 * Created by hjsoft on 22/6/17.
 */
public class AllTripsData implements Serializable {

    String tripId,tripDate,routeName,roosterNo,tripStatus,modifiedDate,driverMobile,driverDutyStatus,gpsRouteDetails,vehRegNo,vehCat;

    int gpsToKms,gpdToHrs;

    public AllTripsData(String tripId,String tripDate,String routeName,String roosterNo,String tripStatus,String modifiedDate,String driverMobile,
                        String  driverDutyStatus,String gpsRouteDetails,int gpsToKms,int gpdToHrs,String vehRegNo,String vehCat)
    {
        this.tripId=tripId;
        this.tripDate=tripDate;
        this.routeName=routeName;
        this.roosterNo=roosterNo;
        this.tripStatus=tripStatus;
        this.modifiedDate=modifiedDate;
        this.driverMobile=driverMobile;
        this.driverDutyStatus=driverDutyStatus;
        this.gpsRouteDetails=gpsRouteDetails;
        this.gpsToKms=gpsToKms;
        this.gpdToHrs=gpdToHrs;
        this.vehRegNo=vehRegNo;
        this.vehCat=vehCat;

    }


    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRoosterNo() {
        return roosterNo;
    }

    public void setRoosterNo(String roosterNo) {
        this.roosterNo = roosterNo;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public String getDriverDutyStatus() {
        return driverDutyStatus;
    }

    public void setDriverDutyStatus(String driverDutyStatus) {
        this.driverDutyStatus = driverDutyStatus;
    }

    public String getGpsRouteDetails() {
        return gpsRouteDetails;
    }

    public void setGpsRouteDetails(String gpsRouteDetails) {
        this.gpsRouteDetails = gpsRouteDetails;
    }

    public int getGpsToKms() {
        return gpsToKms;
    }

    public void setGpsToKms(int gpsToKms) {
        this.gpsToKms = gpsToKms;
    }

    public int getGpdToHrs() {
        return gpdToHrs;
    }

    public void setGpdToHrs(int gpdToHrs) {
        this.gpdToHrs = gpdToHrs;
    }

    public String getVehRegNo() {
        return vehRegNo;
    }

    public void setVehRegNo(String vehRegNo) {
        this.vehRegNo = vehRegNo;
    }

    public String getVehCat() {
        return vehCat;
    }

    public void setVehCat(String vehCat) {
        this.vehCat = vehCat;
    }
}


