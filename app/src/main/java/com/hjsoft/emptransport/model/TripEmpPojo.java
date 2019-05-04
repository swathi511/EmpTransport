package com.hjsoft.emptransport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hjsoft on 9/6/17.
 */
public class TripEmpPojo implements Serializable {

    @SerializedName("employeeid")
    @Expose
    private String employeeid;
    @SerializedName("employeename")
    @Expose
    private String employeename;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("emailid")
    @Expose
    private String emailid;
    @SerializedName("reportingplace")
    @Expose
    private String reportingplace;
    @SerializedName("emptimein")
    @Expose
    private String emptimein;
    @SerializedName("emplocation")
    @Expose
    private String emplocation;
    @SerializedName("routeseq")
    @Expose
    private String routeseq;
    @SerializedName("reportingtime")
    @Expose
    private String reportingtime;
    @SerializedName("otp")
    @Expose
    private String otp;

    public String getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getReportingplace() {
        return reportingplace;
    }

    public void setReportingplace(String reportingplace) {
        this.reportingplace = reportingplace;
    }

    public String getEmptimein() {
        return emptimein;
    }

    public void setEmptimein(String emptimein) {
        this.emptimein = emptimein;
    }

    public String getEmplocation() {
        return emplocation;
    }

    public void setEmplocation(String emplocation) {
        this.emplocation = emplocation;
    }

    public String getRouteseq() {
        return routeseq;
    }

    public void setRouteseq(String routeseq) {
        this.routeseq = routeseq;
    }

    public String getReportingtime() {
        return reportingtime;
    }

    public void setReportingtime(String reportingtime) {
        this.reportingtime = reportingtime;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


}
