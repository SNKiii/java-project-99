package hexlet.code.mapper;

import org.mapstruct.Condition;
import org.mapstruct.Named;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Component
public class JsonNullableMapper {

//    @Named("fromJsonNullable")
//    public <T> T fromJsonNullable(JsonNullable<T> jsonNullable) {
//        return jsonNullable != null ? jsonNullable.orElse(null) : null;
//    }

    @Condition
    public <T> boolean isPresent(JsonNullable<T> nullable) {
        return nullable != null && nullable.isPresent();
    }
}
