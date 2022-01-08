package nl.tudelft.sem.user.communication.request;

public class LoginRequest {
    private String netId;
    private String password;

    public LoginRequest(String netId, String password) {
        this.netId = netId;
        this.password = password;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
