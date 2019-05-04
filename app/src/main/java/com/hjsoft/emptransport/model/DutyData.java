package com.hjsoft.emptransport.model;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by hjsoft on 26/5/17.
 */
public class DutyData implements Serializable{

    String companyId,customerId,tripId,tripDate,vehCatId,vehRegId,otherVeh,driverId,otherDriver,routeId,routeName,roosterNo,tripStatus,preparedBy,
    modifiedDate,driverMobile,driverDutyStatus,gpsRouteDetails,vehRegNo,vehCat,longitude,latitude;
    int gpsTotKms,gpdTotHrs;


    public DutyData(String companyId,String customerId,String tripId,String tripDate,String vehCatId,String vehRegId,String otherVeh,
                    String driverId,String otherDriver,String routeId,String routeName,String roosterNo,String tripStatus,String preparedBy,
                    String modifiedDate,String driverMobile,String driverDutyStatus,String gpsRouteDetails,String vehRegNo,
                    String vehCat,int gpsTotKms,int gpdTotHrs,String longitude,String latitude)
    {
        this.companyId=companyId;
        this.customerId=customerId;
        this.tripId=tripId;
        this.tripDate=tripDate;
        this.vehCatId=vehCatId;
        this.vehRegId=vehRegId;
        this.otherVeh=otherVeh;
        this.driverId=driverId;
        this.otherDriver=otherDriver;
        this.routeId=routeId;
        this.routeName=routeName;
        this.roosterNo=roosterNo;
        this.tripStatus=tripStatus;
        this.preparedBy=preparedBy;
        this.modifiedDate=modifiedDate;
        this.driverMobile=driverMobile;
        this.driverDutyStatus=driverDutyStatus;
        this.gpsRouteDetails=gpsRouteDetails;
        this.vehRegNo=vehRegNo;
        this.vehCat = vehCat;
        this.gpsTotKms=gpsTotKms;
        this.gpdTotHrs=gpdTotHrs;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getVehCatId() {
        return vehCatId;
    }

    public void setVehCatId(String vehCatId) {
        this.vehCatId = vehCatId;
    }

    public String getVehRegId() {
        return vehRegId;
    }

    public void setVehRegId(String vehRegId) {
        this.vehRegId = vehRegId;
    }

    public String getOtherVeh() {
        return otherVeh;
    }

    public void setOtherVeh(String otherVeh) {
        this.otherVeh = otherVeh;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOtherDriver() {
        return otherDriver;
    }

    public void setOtherDriver(String otherDriver) {
        this.otherDriver = otherDriver;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
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

    public int getGpsTotKms() {
        return gpsTotKms;
    }

    public void setGpsTotKms(int gpsTotKms) {
        this.gpsTotKms = gpsTotKms;
    }

    public int getGpdTotHrs() {
        return gpdTotHrs;
    }

    public void setGpdTotHrs(int gpdTotHrs) {
        this.gpdTotHrs = gpdTotHrs;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
}
