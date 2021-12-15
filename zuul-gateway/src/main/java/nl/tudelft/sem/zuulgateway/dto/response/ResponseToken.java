package nl.tudelft.sem.zuulgateway.dto.response;

public class ResponseToken {
    private String jwt;

    public ResponseToken(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
