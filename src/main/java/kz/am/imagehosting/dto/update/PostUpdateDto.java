package kz.am.imagehosting.dto.update;

public class PostUpdateDto {
    private String postName;
    private Integer accessId;

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Integer getAccessId() {
        return accessId;
    }

    public void setAccessId(Integer accessId) {
        this.accessId = accessId;
    }

}
