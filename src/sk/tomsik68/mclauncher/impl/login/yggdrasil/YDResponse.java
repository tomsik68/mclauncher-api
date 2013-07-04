package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;

public class YDResponse {
    private String error, message;

    public YDResponse(JSONObject json) {
        if(json.containsKey("error"))
            setError(json.get("error").toString());
        if(json.containsKey("errorMessage"))
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
