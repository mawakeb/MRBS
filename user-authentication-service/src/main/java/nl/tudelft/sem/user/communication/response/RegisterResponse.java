package nl.tudelft.sem.user.communication.response;

import nl.tudelft.sem.user.object.Type;

public class RegisterResponse {
    private String netId;
    private String name;
    private Type type;

    /**
     * Instantiates a new Register response.
     *
     * @param netId the net id
     * @param name  the name
     * @param type  the type
     */
    public RegisterResponse(String netId, String name, Type type) {
        this.netId = netId;
        this.name = name;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
