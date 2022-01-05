package nl.tudelft.sem.user.communication.request;

public class RegisterRequest {
    private String netId;
    private String name;
    private String password;

    public RegisterRequest(String netId, String name, String password) {
        this.netId = netId;
        this.name = name;
        this.password = password;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
