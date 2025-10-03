package words;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DictionaryEntry {

    @JsonProperty("hwi")
    private String word;

    @JsonProperty("fl")
    private String speechPart;

    public String getWord() {
        return word;
    }

    public String getSpeechPart() {
        return speechPart;
    }

}
