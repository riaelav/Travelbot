package travelbot.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "travelbot")
public class PriorityRulesProperties {
    private List<String> highKeywords = new ArrayList<>();

    public List<String> getHighKeywords() {
        return highKeywords;
    }

    public void setHighKeywords(List<String> highKeywords) {
        this.highKeywords = highKeywords;
    }
}
