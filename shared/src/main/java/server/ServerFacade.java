package server;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
        private final HttpClient client = HttpClient.newHttpClient();
        private final String serverUrl;

        public ServerFacade(String url) {serverUrl = url;}

        private HttpRequest buildRequest(String method, String path, Object body) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + path))
                    .method(method, makeRequestBody(body));
            if (body != null) {
                request.setHeader("Content-Type", "application/json");
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

        private HttpResponse<String> sendRequest(HttpRequest request) throws Response


}
