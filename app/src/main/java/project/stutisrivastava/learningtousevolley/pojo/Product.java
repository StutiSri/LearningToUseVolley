package project.stutisrivastava.learningtousevolley.pojo;

/**
 * Created by stutisrivastava on 2/29/16.
 */
public class Product {

    String productName;
    String productImgUrl;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    @Override
    public String toString() {
        return productName +", " +productImgUrl;
    }
}
