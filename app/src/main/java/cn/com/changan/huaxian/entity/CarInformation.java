package cn.com.changan.huaxian.entity;

public class CarInformation {
    public String vin;//车架号
    public String address;//位置
    public String store;//仓库
    public String MissingPart;//缺件

    public String getVin() {
        return vin;
    }

    public String getAddress() {
        return address;
    }

    public String getStore() {
        return store;
    }

    public String getMissingPart() {
        return MissingPart;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setMissingPart(String missingPart) {
        MissingPart = missingPart;
    }
}
