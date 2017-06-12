package com.opsgenie.playground.scenarioGenerator.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Serhat Can
 * @version 03/06/17
 */
public class JSON {

    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();
    private static final ObjectMapper FAIL_IGNORE_MAPPER = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static <T extends String, U> Map<T, U> parse(String json) throws IOException {
        JsonParser parser = DEFAULT_MAPPER.getFactory().createParser(json);
        Map<T, U> result = _parse(parser);
        parser.close();
        return result;
    }

    public static <T, U> Map<T, U> parse(byte[] json) throws IOException {
        JsonParser parser = DEFAULT_MAPPER.getFactory().createParser(json);
        Map<T, U> result = _parse(parser);
        parser.close();
        return result;
    }

    private static <T, U> Map<T, U> _parse(JsonParser parser) throws IOException {
        TypeReference<HashMap<T, U>> typeRef = new TypeReference<HashMap<T, U>>() {
        };
        ObjectReader reader = DEFAULT_MAPPER.reader(typeRef);
        Map<T, U> result = reader.readValue(parser);
        if (result == null) {
            result = new HashMap<>();
        }
        return result;
    }

    public static <T> List<T> parseAsList(String json, Class<T> clazz) throws IOException {
        JavaType type = DEFAULT_MAPPER.getTypeFactory().
                constructCollectionType(List.class, clazz);

        return DEFAULT_MAPPER.readValue(json, type);
    }

    public static <T> T convertValue(Object object, TypeReference<T> typeRef) {
        return DEFAULT_MAPPER.convertValue(object, typeRef);
    }

    public static <T> T parse(String json, Class<T> klass) throws IOException {
        ObjectReader reader = DEFAULT_MAPPER.reader(klass);
        return reader.readValue(json);
    }

    public static <T> T parse(byte[] json, Class<T> klass) throws IOException {
        ObjectReader reader = DEFAULT_MAPPER.reader(klass);
        return reader.readValue(json);
    }

    public static String toJson(Object jsonContent) throws IOException {
        ObjectWriter writer = DEFAULT_MAPPER.writer();
        return writer.writeValueAsString(jsonContent);
    }


    public static Map toMap(Object object) {
        return DEFAULT_MAPPER.convertValue(object, Map.class);
    }

    public static Map toMapIgnoreErrors(Object object) {
        return FAIL_IGNORE_MAPPER.convertValue(object, Map.class);
    }

    public static String toJsonIgnoreErrors(Object jsonContent) throws IOException {
        ObjectWriter writer = FAIL_IGNORE_MAPPER.writer();
        return writer.writeValueAsString(jsonContent);
    }

    public static String toPrettyJsonIgnoreErrors(Object jsonContent) throws IOException {
        ObjectWriter writer = FAIL_IGNORE_MAPPER.writer();
        return writer.withDefaultPrettyPrinter().writeValueAsString(jsonContent);
    }

    public static String toPrettyJson(Object jsonContent) throws IOException {
        ObjectWriter writer = DEFAULT_MAPPER.writer();
        return writer.withDefaultPrettyPrinter().writeValueAsString(jsonContent);
    }

    public static byte[] toJsonAsBytes(Object jsonContent) throws IOException {
        ObjectWriter writer = DEFAULT_MAPPER.writer();
        return writer.writeValueAsBytes(jsonContent);
    }

}
