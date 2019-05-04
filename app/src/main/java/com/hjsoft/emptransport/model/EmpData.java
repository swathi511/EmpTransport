package com.hjsoft.emptransport.model;

import java.io.Serializable;

/**
 * Created by hjsoft on 2/6/17.
 */
public class EmpData implements Serializable {

    String empId,empName,mobileNo,emailId,repPlace,empTimeIn,empLoc,otp;

    public EmpData(String empId,String empName,String mobileNo,String emailId,String repPlace,String empTimeIn,String empLoc,String otp)
    {
        this.empId=empId;
        this.empName=empName;
        this.mobileNo=mobileNo;
        this.emailId=emailId;
        this.repPlace=repPlace;
        this.empTimeIn=empTimeIn;
        this.empLoc=empLoc;
        this.otp=otp;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRepPlace() {
        return repPlace;
    }

    public void setRepPlace(String repPlace) {
        this.repPlace = repPlace;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmpLoc() {
        return empLoc;
    }

    public void setEmpLoc(String empLoc) {
        this.empLoc = empLoc;
    }

    public String getEmpTimeIn() {
        return empTimeIn;
    }

    public void setEmpTimeIn(String empTimeIn) {
        this.empTimeIn = empTimeIn;
    }
}
