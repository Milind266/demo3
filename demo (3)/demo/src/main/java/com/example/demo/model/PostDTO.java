package com.example.demo.model;



import java.util.Date;

public class PostDTO {
    private String postBody;
    private Long userID;

    public PostDTO() {}

    public PostDTO(String postBody, Long userID) {
        this.postBody = postBody;
        this.userID = userID;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }


}
