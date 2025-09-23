package travelbot.demo.services;

import org.springframework.stereotype.Service;
import travelbot.demo.config.PriorityRulesProperties;

import java.text.Normalizer;
import java.util.Locale;

@Service
public class KeywordLeadService {
    private final PriorityRulesProperties props;

    public KeywordLeadService(PriorityRulesProperties props) {
        this.props = props;
    }

    public boolean isHigh(String text) {
        if (text == null || text.isBlank()) return false;
        String t = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT);
        for (String kw : props.getHighKeywords()) {
            if (t.contains(kw.toLowerCase(Locale.ROOT))) return true;
        }
        return false;
    }
}