package murph32.core;

import java.io.Serializable;

/**
 * Created by Dukat on 5/6/2014.
 */
public class Response implements Serializable {

    private static final long serialVersionUID = -7107147034623068822L;
    private String header;
    private Serializable payload;

    public Response(String header, Serializable payload) {
        this.header = header;
        this.payload = payload;
    }

    public String getHeader() {
        return header;
    }

    public Object getPayload() {
        return payload;
    }
}
