package com.hjsoft.emptransport.model;

/**
 * Created by hjsoft on 1/7/17.
 */
public class JourneyDetails {

    String tripId,driverId,tripDate,routeName,distance,time,routeDetails,pickupLat,pickupLng,dropLat,dropLng;

    public JourneyDetails(String tripId,String driverId,String tripDate,String routeName,String distance,String time,String routeDetails,String pickupLat,String pickupLng,String dropLat,String dropLng)
    {
        this.tripId=tripId;
        this.driverId=driverId;
        this.tripDate=tripDate;
        this.routeName=routeName;
        this.distance=distance;
        this.time=time;
        this.routeDetails=routeDetails;
        this.pickupLat=pickupLat;
        this.pickupLng=pickupLng;
        this.dropLat=dropLat;
        this.dropLng=dropLng;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRouteDetails() {
        return routeDetails;
    }

    public void setRouteDetails(String routeDetails) {
        this.routeDetails = routeDetails;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getPickupLng() {
        return pickupLng;
    }

    public void setPickupLng(String pickupLng) {
        this.pickupLng = pickupLng;
    }

    public String getDropLat() {
        return dropLat;
    }

    public void setDropLat(String dropLat) {
        this.dropLat = dropLat;
    }

    public String getDropLng() {
        return dropLng;
    }

    public void setDropLng(String dropLng) {
        this.dropLng = dropLng;
    }
}
