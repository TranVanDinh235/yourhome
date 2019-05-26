package com.example.homedy.Home;

import com.example.homedy.R;

import java.util.ArrayList;

public class HomeItem {
    String name;
    String time;
    String title;
    String description;
    int area;
    int gia;
    ArrayList<String> url_image = new ArrayList<>();
    String id;
    String address;
    String phone;
    String posttype;
    String typeroom;
    Double lat;
    Double lng;
    String name_person;
    String phone_person;

    private static ArrayList<HomeItem> homeItems = new ArrayList<>();
    private static ArrayList<HomeItem> homeItemsSearch = new ArrayList<>();
    private static ArrayList<HomeItem> homeItemsPost = new ArrayList<>();
    private static ArrayList<HomeItem> homeItemsSearchByMap = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public ArrayList<String> getUrl_image() {
        return url_image;
    }

    public void setUrl_image(ArrayList<String> url_image) {
        this.url_image = url_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosttype() {
        return posttype;
    }

    public void setPosttype(String posttype) {
        this.posttype = posttype;
    }

    public String getTyperoom() {
        return typeroom;
    }

    public void setTyperoom(String typeroom) {
        this.typeroom = typeroom;
    }


    public String getName_person() {
        return name_person;
    }

    public void setName_person(String name_person) {
        this.name_person = name_person;
    }

    public String getPhone_person() {
        return phone_person;
    }

    public void setPhone_person(String phone_person) {
        this.phone_person = phone_person;
    }



    public static ArrayList<HomeItem> getHomeItems(){
        return homeItems;
    }


    public static void setHomeItems(ArrayList<HomeItem> homeItems) {
        HomeItem.homeItems = homeItems;
    }

    public static ArrayList<HomeItem> getHomeItemsSearch() {
        return homeItemsSearch;
    }

    public static void setHomeItemsSearch(ArrayList<HomeItem> homeItemsSearch) {
        HomeItem.homeItemsSearch = homeItemsSearch;
    }

    public static ArrayList<HomeItem> getHomeItemsPost() {
        return homeItemsPost;
    }

    public static void setHomeItemsPost(ArrayList<HomeItem> homeItemsPost) {
        HomeItem.homeItemsPost = homeItemsPost;
    }

    public static ArrayList<HomeItem> getHomeItemsSearchByMap() {
        return homeItemsSearchByMap;
    }

    public static void setHomeItemsSearchByMap(ArrayList<HomeItem> homeItemsSearchByMap) {
        HomeItem.homeItemsSearchByMap = homeItemsSearchByMap;
    }
}
