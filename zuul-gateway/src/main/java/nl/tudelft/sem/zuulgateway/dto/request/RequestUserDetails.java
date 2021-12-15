package nl.tudelft.sem.zuulgateway.dto.request;

public class RequestUserDetails {
    private String netId;
    private String password;

    public RequestUserDetails(String netId, String password) {
        this.netId = netId;
        this.password = password;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNetId() {
        return netId;
    }

    public String getPassword() {
        return password;
    }
}
