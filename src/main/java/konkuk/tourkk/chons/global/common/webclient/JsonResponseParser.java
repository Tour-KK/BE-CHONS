package konkuk.tourkk.chons.global.common.webclient;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class JsonResponseParser {

    public static final DateTimeFormatter YYYYMMDD_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static List<Map<String, String>> getMainResponses(Map data) {
        Map<String, Object> response = (Map<String, Object>) data.get("response");
        Map<String, Object> body = (Map<String, Object>) (response).get("body");
        Map<String, Object> items = (Map<String, Object>) body.get("items");
        List<Map<String, String>> itemList = (List<Map<String, String>>) items.get("item");
        return itemList;
    }
}
