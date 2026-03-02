package server;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import io.javalin.http.Context;
import model.AuthData;
import model.ErrorMessage;
import model.UserData;

import service.ClearService;

public class ClearHandler {
    private final ClearService clearService;
    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void clear(Context ctx) {
        clearService.clear();
        ctx.result();
    }
}
