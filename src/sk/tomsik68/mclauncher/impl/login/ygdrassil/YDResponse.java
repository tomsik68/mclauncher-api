package sk.tomsik68.mclauncher.impl.login.ygdrassil;

import net.minidev.json.JSONObject;

public class YDResponse {
    private String error, message;

    public YDResponse(JSONObject json) {
        setError(json.get("error").toString());
        setMessage(json.get("errorMessage").toString());
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
