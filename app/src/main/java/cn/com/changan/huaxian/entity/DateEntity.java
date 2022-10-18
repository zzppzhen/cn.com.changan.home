package cn.com.changan.huaxian.entity;

import java.util.List;

public class DateEntity {
    private String date;
    private List<MyParkingCarEntity> entityList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MyParkingCarEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<MyParkingCarEntity> entityList) {
        this.entityList = entityList;
    }
}
