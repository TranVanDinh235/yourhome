package com.example.homedy;

import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class Address {
    private String city;
    private String district;
    private String commune;
    private String street;
    private String location;

    public static String[] citys = {"Hà Nội", "Hồ Chí Minh", "Hải Phòng", "Đà Nẵng", "Bắc Ninh" };
    public static String[] districts = {"Hoàn Kiếm", "Cầu Giấy", "Nam Từ Liêm", "Thanh Xuân", "Ba Đình"};
    public static String[] communes = {"Mai Dịch", "Dịch Vọng Hậu","Trung Châu", "Vân Nam", "Hát Môn"};
    public static String[] streets = {"Hồ Tùng Mậu", "Xuân Thuỷ", "Phạm Hùng", "Doãn Kế Thiện", "Trần Bình"};
    public static String[] locations = {"Số 4, ngõ 24", "số 9, ngõ 3", "", "ngõ 20", "ngõ 90"};

    public static Address newInstance(String city, String district, String commune, String street, String location) {
        Address address = new Address();
        address.setCity(city);
        address.setDistrict(district);
        address.setCommune(commune);
        address.setStreet(street);
        address.setLocation(location);
        return address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static List<Address> getListAddressExample(){
        List<Address> addresses = new ArrayList<>();
        for(int i = 0; i < citys.length; i ++){
            Address address = newInstance(citys[i], districts[i], communes[i],streets[i],locations[i]);
            addresses.add(address);
        }
        return addresses;
    }

}
