package murph32.core;

import java.io.Serializable;

/**
 * Created by Dukat on 5/3/2014.
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 4699481941056369550L;
    private String header;
    private Serializable payload;

    public Request(String header, Serializable payload) {
        this.header = header;
        this.payload = payload;
    }

    public String getHeader() {
        return header;
    }

    public Serializable getPayload() {
        return payload;
    }
}
