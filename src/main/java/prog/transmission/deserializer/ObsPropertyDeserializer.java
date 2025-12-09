package prog.transmission.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.lang.reflect.Method;

public class ObsPropertyDeserializer<T> extends JsonDeserializer<T> implements ContextualDeserializer {

    private JavaType javaType;

    public ObsPropertyDeserializer() {

    }

    private ObsPropertyDeserializer(JavaType javaType) {
        this.javaType = javaType;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) {
        return new ObsPropertyDeserializer<>(deserializationContext.getContextualType());
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Class<T> targetClass = (Class<T>) javaType.getRawClass();

        try {
            T instance = targetClass.getDeclaredConstructor().newInstance();

            node.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                String value = entry.getValue().asText();

                try {
                    String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method getter = targetClass.getMethod(getterName);

                    Object obsProperty = getter.invoke(instance);

                    Method setValue = obsProperty.getClass().getMethod("setValue", String.class);
                    setValue.invoke(obsProperty, value);

                } catch(NoSuchMethodException e) {

                } catch (Exception e) {
                    throw new RuntimeException("Erreur lors de la désérialisation du champ " + fieldName, e);
                }
            });

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Impossible d'instancier la classe " + targetClass.getName(), e);
        }
    }
}