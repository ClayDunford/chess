package serverfacade;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.ErrorMessage;
import model.GameData;
import model.UserData;
import model.requests.CreateGameRequest;
import model.requests.JoinGameRequest;
import model.results.CreateGameResult;
import model.results.ListGamesResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
        private final HttpClient client = HttpClient.newHttpClient();
        private final String serverUrl;

        public ServerFacade(String url) {serverUrl = url;}

        public AuthData register(UserData userData) throws ResponseException {
            var request = buildRequest("POST", "/user", userData, null);
            var response = sendRequest(request);
            return handleResponse(response, AuthData.class);
        }

        public AuthData login(UserData userData) throws ResponseException {
            var request = buildRequest("POST", "/session", userData, null);
            var response = sendRequest(request);
            return handleResponse(response, AuthData.class);
        }

        public void logout(String authToken) throws ResponseException {

            var request = buildRequest("DELETE", "/session", null, authToken);
            var response = sendRequest(request);

            handleResponse(response, null);
        }

        public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest) throws ResponseException {
            var request = buildRequest("POST", "/game", createGameRequest, authToken);
            var response = sendRequest(request);
            return handleResponse(response, CreateGameResult.class);
        }

        public ListGamesResult listGames(String authToken) throws ResponseException {
            var request = buildRequest("GET", "/game", null, authToken);
            var response = sendRequest(request);
            return handleResponse(response, ListGamesResult.class);
        }

        public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws ResponseException {
            var request = buildRequest("PUT", "/game", joinGameRequest, authToken);
            var response = sendRequest(request);
            handleResponse(response, null);
        }

        public void clear() throws ResponseException {
            var request = buildRequest("DELETE", "/db",null, null);
            sendRequest(request);
        }


        private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + path))
                    .method(method, makeRequestBody(body));
            if (body != null) {
                request.setHeader("Content-Type", "application/json");
            }
            if (authToken != null) {
                request.setHeader("authorization", authToken);
            }
            return request.build();
        }

        private HttpRequest.BodyPublisher makeRequestBody(Object request) {
            if (request != null) {
                return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
            } else {
                return HttpRequest.BodyPublishers.noBody();
            }
        }

        private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
            try {
                return client.send(request, HttpResponse.BodyHandlers.ofString());

            } catch (Exception ex) {
                throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
            }
        }

        private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
            var status = response.statusCode();
            if(!isSuccessful(status)) {
                var body = response.body();

                if (body != null) {
                    ErrorMessage error = new Gson().fromJson(body, ErrorMessage.class);
                    throw new ResponseException(ResponseException.fromHttpStatusCode(status), error.message());
                }

                throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
            }
            if (responseClass != null) {
                return new Gson().fromJson(response.body(), responseClass);
            }
            return null;
        }


        private boolean isSuccessful(int status) {return status / 100 == 2;}


}
