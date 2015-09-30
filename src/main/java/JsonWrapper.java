import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.google.common.base.Throwables.propagate;

public class JsonWrapper {

    static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw propagate(e);
        }
    }

    public static <TYPE> TYPE fromJson(String jsonString, Class<TYPE> clazz) {
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw propagate(e);
        }

    }

}
