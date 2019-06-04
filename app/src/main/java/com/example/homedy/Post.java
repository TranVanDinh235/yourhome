package com.example.homedy;
import java.util.ArrayList;

public class Post {
    private String name;
    private String time;
    private String title;
    private String description;
    private int area;
    private int rent;
    private ArrayList<String> url_image = new ArrayList<>();
    private String id;
    private String address;
    private String phone;
    private String posttype;
    private String typeroom;
    private Double lat;
    private Double lng;
    private String name_person;
    private String phone_person;

    private static ArrayList<Post> posts = new ArrayList<>();
    private static ArrayList<Post> homeItemsSearches = new ArrayList<>();
    private static ArrayList<Post> homeItemsPost = new ArrayList<>();
    private static ArrayList<Post> homeItemsSearchByMap = new ArrayList<>();

    public Post(){}

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

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
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



    public static ArrayList<Post> getPosts(){
        return posts;
    }


    public static void setPosts(ArrayList<Post> posts) {
        Post.posts = posts;
    }

    public static ArrayList<Post> getHomeItemsSearches() {
        return homeItemsSearches;
    }

    public static void setHomeItemsSearches(ArrayList<Post> homeItemsSearches) {
        Post.homeItemsSearches = homeItemsSearches;
    }

    public static ArrayList<Post> getHomeItemsPost() {
        return homeItemsPost;
    }

    public static void setHomeItemsPost(ArrayList<Post> homeItemsPost) {
        Post.homeItemsPost = homeItemsPost;
    }

    public static ArrayList<Post> getHomeItemsSearchByMap() {
        return homeItemsSearchByMap;
    }

    public static void setHomeItemsSearchByMap(ArrayList<Post> homeItemsSearchByMap) {
        Post.homeItemsSearchByMap = homeItemsSearchByMap;
    }
}
