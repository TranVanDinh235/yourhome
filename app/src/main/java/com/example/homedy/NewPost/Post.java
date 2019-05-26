package com.example.homedy.NewPost;

public class Post {
    String title;
    String description;
    String posttype;
    String typeroom;
    int gia;
    int dientich;
    String address;
    String postavatar;
    String phone;
    String[] images;

    static Post post;
    public Post() {
    }

    public Post(String title, String description, String posttype, String typeroom, int gia, int dientich, String address, String phone, String postavatar, String[] images) {
        this.title = title;
        this.description = description;
        this.posttype = posttype;
        this.typeroom = typeroom;
        this.gia = gia;
        this.dientich = dientich;
        this.address = address;
        this.postavatar = postavatar;
        this.phone = phone;
        this.images = images;
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

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getDientich() {
        return dientich;
    }

    public void setDientich(int dientich) {
        this.dientich = dientich;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostavatar() {
        return postavatar;
    }

    public void setPostavatar(String postavatar) {
        this.postavatar = postavatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public static Post getPost() {
        return post;
    }

    public static void setPost(Post post) {
        Post.post = post;
    }
}

