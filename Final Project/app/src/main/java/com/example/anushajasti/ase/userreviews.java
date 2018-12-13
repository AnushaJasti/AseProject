package com.example.anushajasti.ase;

public class userreviews {
    String emailid, productname, review;

    public userreviews(String emailid, String productname, String review) {
        this.emailid = emailid;
        this.productname = productname;
        this.review = review;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getProductname() {
        return productname;
    }

    public String getReview() {
        return review;
    }


}
