package konkuk.tourkk.chons.global.common.webclient;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JsonResponseParser {

    public static final DateTimeFormatter YYYYMMDD_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static List<Map<String, String>> getMainResponses(Map response) {
        Map<String, Object> body = (Map<String, Object>) ((Map<String, Object>) response.get(
            "response")).get("body");
        Map<String, Object> items = (Map<String, Object>) body.get("items");
        List<Map<String, String>> itemList = (List<Map<String, String>>) items.get("item");
        return itemList;
    }
}
