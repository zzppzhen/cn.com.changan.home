package cn.com.changan.huaxian.entity;

public class MyParkingCarEntity {
    private String vin;
    private String id;
    private String carCreateTime;
    private String carParkingDate;

    public String getCarParkingDate() {
        return carParkingDate;
    }

    public void setCarParkingDate(String carParkingDate) {
        this.carParkingDate = carParkingDate;
    }

    private String carStopWarehouseName;
    private String carLat;
    private String carLng;
    private String carSeries;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarCreateTime() {
        return carCreateTime;
    }

    public void setCarCreateTime(String carCreateTime) {
        this.carCreateTime = carCreateTime;
    }

    public String getCarStopWarehouseName() {
        return carStopWarehouseName;
    }

    public void setCarStopWarehouseName(String carStopWarehouseName) {
        this.carStopWarehouseName = carStopWarehouseName;
    }

    public String getCarLat() {
        return carLat;
    }

    public void setCarLat(String carLat) {
        this.carLat = carLat;
    }

    public String getCarLng() {
        return carLng;
    }

    public void setCarLng(String carLng) {
        this.carLng = carLng;
    }

    public String getCarSeries() {
        return carSeries;
    }

    public void setCarSeries(String carSeries) {
        this.carSeries = carSeries;
    }
}
