package com.wtz.java.gson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author tiezhu
 * @date 2022/2/22
 */
public class _01_Json_Parse {

    private static final Logger LOG = LoggerFactory.getLogger(_01_Json_Parse.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Gson GSON = new Gson();

    public static void main(String[] args) throws IOException {
        String path = "/Users/wtz/temp.txt";

        String str = readFile(path);

        JsonNode jsonNode = OBJECT_MAPPER.readTree(str);
        System.out.println(jsonNode.toString());
    }

    public static String readFile(String sqlPath) {
        try {
            byte[] array = Files.readAllBytes(Paths.get(sqlPath));
            return new String(array, StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            LOG.error("Can not get the job info !!!", ioe);
            throw new RuntimeException(ioe);
        }
    }
}
