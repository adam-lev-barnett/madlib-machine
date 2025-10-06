package words;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import utility.exceptions.NullEntryException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DictionaryFetcher {
    private final HttpClient client;

    public DictionaryFetcher() {
        client = HttpClient.newHttpClient();
    }

    // Creates json string from call to dictionary API and returns list of all entries matching the word argument
    //& Insert logic for multiple entries? Could be tedious
    public void fetchEntry(String word) throws IOException, InterruptedException, NullEntryException {

        // Build request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.dictionaryapi.com/api/v3/references/collegiate/json/" + word.toLowerCase() + "?key=91b9d99d-1e37-494a-b59a-4ffc58efa02f"))
                .GET()
                .build();

        // Send request and receive response; ofString() is a BodyHandlers method that converts it to string versus other formats
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonString = response.body();

        ObjectMapper mapper = new ObjectMapper();
        List<DictionaryEntry> wordList = mapper.readValue(jsonString, new TypeReference<List<DictionaryEntry>>() {} );
        Set<String> partsOfSpeech = new HashSet<>();
        for (DictionaryEntry entry : wordList) {
            if (entry.getMetaId().contains(word + ":")) {
                partsOfSpeech.add(entry.getPartOfSpeech());
            }
        }
        DictionaryEntries.getInstance().addEntries(word, partsOfSpeech);
    }
}


