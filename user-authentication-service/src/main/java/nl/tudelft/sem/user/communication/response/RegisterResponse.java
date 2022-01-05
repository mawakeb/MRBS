package nl.tudelft.sem.user.communication.response;

public class RegisterResponse {
    private String netId;
    private String name;

    public RegisterResponse(String netId, String name) {
        this.netId = netId;
        this.name = name;
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
}
