package com.luyc.esclient;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * @author luyc
 * @since 2022/12/28 16:28
 */
public class UIMRScanRecord {
    String rptCode;//uiMRScanRpt

    @JsonProperty(value = "hosp_id")
    int hosp_id;

    @JsonProperty(value = "name")
    String name;            //名称            从配置文件devices.xml中获得；

    @JsonProperty(value = "modality")
    String modality;        //类型            从配置文件devices.xml中获得；

    @JsonProperty(value = "manufacturer")
    String manufacturer;    //供应商           从配置文件devices.xml中获得；

    @JsonProperty(value = "system_type")
    String systemType;      //型号            从配置文件devices.xml中获得；

    @JsonProperty(value = "system_serial_number")
    String systemSerialNumber;    //系统序列号    从配置文件devices.xml中获得； or from tube_history

    @JsonProperty(value = "study_uid")
    String studyUID;

    @JsonProperty(value = "real_exposure_time")
    float realExposureTime;

    @JsonProperty(value = "cumulative_scan_time")
    float cumulativeScanTime;

    @JsonProperty(value = "real_scan_length")
    float realScanLength;

    @JsonProperty(value = "voltage")
    int voltage;

    @JsonProperty(value = "cumulative_mas")
    float cumulativeMas;

    @JsonProperty(value = "patient_id")
    String patientID;

    @JsonProperty(value = "patient_sex")
    String patientSex;

    @JsonProperty(value = "patient_birthday")
    String patientBirthday;

    @JsonProperty(value = "study_date_time")
    LocalDateTime studyDateTime;

    @JsonProperty(value = "study_ins_uid")
    String studyInsUID;

    @JsonProperty(value = "study_description")
    String studyDescription;

    @JsonProperty(value = "schedule_ae_title")
    String scheduleAETitle;

    @JsonProperty(value = "access_no")
    int accessionNo;

    @JsonProperty(value = "study_count")
    int studyCount;

    public String getRptCode() {
        return rptCode;
    }

    public void setRptCode(String rptCode) {
        this.rptCode = rptCode;
    }

    public int getHosp_id() {
        return hosp_id;
    }

    public void setHosp_id(int hosp_id) {
        this.hosp_id = hosp_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getSystemSerialNumber() {
        return systemSerialNumber;
    }

    public void setSystemSerialNumber(String systemSerialNumber) {
        this.systemSerialNumber = systemSerialNumber;
    }

    public String getStudyUID() {
        return studyUID;
    }

    public void setStudyUID(String studyUID) {
        this.studyUID = studyUID;
    }

    public float getRealExposureTime() {
        return realExposureTime;
    }

    public void setRealExposureTime(float realExposureTime) {
        this.realExposureTime = realExposureTime;
    }

    public float getCumulativeScanTime() {
        return cumulativeScanTime;
    }

    public void setCumulativeScanTime(float cumulativeScanTime) {
        this.cumulativeScanTime = cumulativeScanTime;
    }

    public float getRealScanLength() {
        return realScanLength;
    }

    public void setRealScanLength(float realScanLength) {
        this.realScanLength = realScanLength;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public float getCumulativeMas() {
        return cumulativeMas;
    }

    public void setCumulativeMas(float cumulativeMas) {
        this.cumulativeMas = cumulativeMas;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getPatientBirthday() {
        return patientBirthday;
    }

    public void setPatientBirthday(String patientBirthday) {
        this.patientBirthday = patientBirthday;
    }

    public LocalDateTime getStudyDateTime() {
        return studyDateTime;
    }

    public void setStudyDateTime(LocalDateTime studyDateTime) {
        this.studyDateTime = studyDateTime;
    }

    public String getStudyInsUID() {
        return studyInsUID;
    }

    public void setStudyInsUID(String studyInsUID) {
        this.studyInsUID = studyInsUID;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    public String getScheduleAETitle() {
        return scheduleAETitle;
    }

    public void setScheduleAETitle(String scheduleAETitle) {
        this.scheduleAETitle = scheduleAETitle;
    }

    public int getAccessionNo() {
        return accessionNo;
    }

    public void setAccessionNo(int accessionNo) {
        this.accessionNo = accessionNo;
    }

    public int getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(int studyCount) {
        this.studyCount = studyCount;
    }
}
