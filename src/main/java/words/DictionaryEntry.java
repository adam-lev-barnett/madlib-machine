package words;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DictionaryEntry {

    @JsonProperty("hwi")
    private Hwi hwi;

    @JsonProperty("fl")
    private String speechPart;

    public String getWord() {
        return (this.hwi == null ? null :hwi.getHw());
    }

    public String getSpeechPart() {
        return speechPart;
    }

    @Override
    public String toString() {
        return String.format("Word: %s\nPart of speech: %s\n", hwi, speechPart);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Hwi {
        private String hw;
        public String getHw() { return hw; }
    }


}
