package server.handlers;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import io.javalin.http.Context;

import model.ErrorMessage;
import service.ClearService;

public class ClearHandler {
    private final ClearService clearService;
    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void clear(Context ctx) {
        try {
            clearService.clear();
            ctx.result();
        } catch (DataAccessException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(400).result(new Gson().toJson(message));
        }
    }
}
