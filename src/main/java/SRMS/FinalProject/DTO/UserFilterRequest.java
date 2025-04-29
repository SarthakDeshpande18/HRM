package SRMS.FinalProject.DTO;


public class UserFilterRequest {

    //@NotNull(message = "Filter field is required and cannot be null")
    private Integer filter;

    public Integer getFilter() {
        return filter;
    }

    public void setFilter(Integer filter) {
        this.filter = filter;
    }
}


