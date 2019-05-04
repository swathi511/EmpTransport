package com.hjsoft.emptransport.webservices;

import com.google.gson.JsonObject;
import com.hjsoft.emptransport.model.AllTripsData;
import com.hjsoft.emptransport.model.AllTripsPojo;
import com.hjsoft.emptransport.model.DistancePojo;
import com.hjsoft.emptransport.model.DutyPojo;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.Pojo;
import com.hjsoft.emptransport.model.SnapDistance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by hjsoft on 25/5/17.
 */
public interface API {

    @POST("Login/CheckLogin")
    Call<Pojo> validate(@Body JsonObject v);

    @GET("DriverTrips/GetDriverTrips")
    Call<List<DutyPojo>> getDutyData(@Query("login") String login,
                                     @Query("pwd") String pwd,
                                     @Query("companycode") String companyCode);

    @POST("Trip/UpdateTripInfo")
    Call<Pojo> sendingUpdates(@Body JsonObject v);

    @PUT("Trip/AcceptTrip")
    Call<Pojo> acceptDuty(@Body JsonObject v);

    @GET("Trip/GetTripEmps")
    Call<List<EmpPojo>> getEmpData(@Query("tripid") String tripId,
                                   @Query("companyid") String companyId);

    @GET
    Call<DistancePojo> getDistanceDetails(@Url String urlString);

    @PUT("Employee/UpdateUsedStatus")
    Call<Pojo> sendEmpValidation(@Body JsonObject v);

    @GET("Totaltrips/GetDriverTotalTrips")
    Call<List<AllTripsPojo>> getAllTrips(@Query("companyid") String companyId,
                                         @Query("driverid") String driverId,
                                         @Query("fromdate") String fromDate,
                                         @Query("todate") String toDate);

    @GET
    Call<DistancePojo> getOSDistanceDetails(@Url String urlString);

    @GET
    Call<SnapDistance> getSnapToRoadDetails(@Url String urlString);

    @POST("TripCoordinates/AddDetails")
    Call<Pojo> sendDistanceForInterval(@Body JsonObject v);



}
