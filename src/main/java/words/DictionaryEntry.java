package words;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DictionaryEntry {

    @JsonProperty("hwi")
    private Hwi hwi;

    @JsonProperty("fl")
    private String partOfSpeech;

    @JsonProperty("meta")
    private Meta meta;


    public String getWord() {
        return (this.hwi == null ? null : hwi.getHw());
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getMetaId() {
        return this.meta.getId();
    }

    @Override
    public String toString() {
        return String.format("Word: %s\nPart of speech: %s\n", this.getWord(), partOfSpeech);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Hwi {
        private String hw;
        public String getHw() { return hw; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Meta {
        private String id;
        public String getId() {return id;}
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryEntry that = (DictionaryEntry) o;
        return Objects.equals(hwi, that.hwi) && Objects.equals(partOfSpeech, that.partOfSpeech) && Objects.equals(meta, that.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hwi, partOfSpeech, meta);
    }


}
