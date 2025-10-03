package utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import words.DictionaryEntry;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DictionaryFetcher {
    private final HttpClient client;

    public DictionaryFetcher() {
        client = HttpClient.newHttpClient();
    }

    // Creates json string from call to dictionary API and returns list of all entries matching the word argument
    //& Insert logic for multiple entries? Could be tedious
    public List<DictionaryEntry> fetchEntry(String word) throws IOException, InterruptedException {

        // Build request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.dictionaryapi.com/api/v3/references/collegiate/json/" + word + "?key=91b9d99d-1e37-494a-b59a-4ffc58efa02f"))
                .GET()
                .build();

        // Send request and receive response; ofString() is a BodyHandlers method that converts it to string versus other formats
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonString = response.body();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<List<DictionaryEntry>>() {} );
    }
}


