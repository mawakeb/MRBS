package nl.tudelft.sem.user.communication.request;

import nl.tudelft.sem.user.object.Type;

public class RegisterRequest {
    private String netId;
    private String name;
    private String password;
    private Type type;

    public RegisterRequest(String netId, String name, String password, Type type) {
        this.netId = netId;
        this.name = name;
        this.password = password;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
