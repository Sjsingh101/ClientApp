package accurate.gaw.livestreamingapp;

public class MyListData{
    private String description,description1,description2;
    private int imgId;

    public MyListData(String description,String description1,String description2, int imgId) {
        this.description = description;
        this.description1 = description1;
        this.description2 = description2;
        this.imgId = imgId;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
