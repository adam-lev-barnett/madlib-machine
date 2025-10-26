package madlibgeneration;

import java.util.List;

public record MadlibDTO(String originalText, String blankedText, String filledText, List<String> posList) {
}
