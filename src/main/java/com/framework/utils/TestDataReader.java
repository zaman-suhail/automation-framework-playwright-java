package com.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import com.framework.config.ConfigReader;

/**
 * TestDataReader - Data-Driven Pattern
 * Reads test data from JSON files
 */
public class TestDataReader {

    private static final Logger log =
            LogManager.getLogger(TestDataReader.class);

    private static final ObjectMapper mapper =
            new ObjectMapper();

    private TestDataReader() {}

    // Read JSON file
    public static JsonNode readJson(String fileName) {

        try {

            String dataDir =
                    ConfigReader.getInstance()
                            .getLogsFolder()
                            .replace("logs/", "testdata/");

            File file =
                    new File(
                            "src/test/resources/testdata/"
                                    + fileName
                    );

            JsonNode node =
                    mapper.readTree(file);

            log.info(
                    "Test data loaded: {}",
                    fileName
            );

            return node;

        } catch (Exception e) {

            log.error(
                    "Could not read file: {}",
                    fileName
            );

            throw new RuntimeException(
                    "Test data load failed: " + fileName,
                    e
            );
        }
    }

    // Valid users
    public static Object[][] getValidUsers() {

        try {

            JsonNode users =
                    readJson("loginData.json")
                            .get("validUsers");

            Object[][] data =
                    new Object[users.size()][3];

            for (int i = 0; i < users.size(); i++) {

                data[i][0] =
                        users.get(i)
                                .get("username")
                                .asText();

                data[i][1] =
                        users.get(i)
                                .get("password")
                                .asText();

                data[i][2] =
                        users.get(i)
                                .get("description")
                                .asText();
            }

            log.info(
                    "Valid users loaded: {} rows",
                    users.size()
            );

            return data;

        } catch (Exception e) {

            log.error("Could not load valid users");

            throw new RuntimeException(e);
        }
    }

    // Invalid users
    public static Object[][] getInvalidUsers() {

        try {

            JsonNode users =
                    readJson("loginData.json")
                            .get("invalidUsers");

            Object[][] data =
                    new Object[users.size()][3];

            for (int i = 0; i < users.size(); i++) {

                data[i][0] =
                        users.get(i)
                                .get("username")
                                .asText();

                data[i][1] =
                        users.get(i)
                                .get("password")
                                .asText();

                data[i][2] =
                        users.get(i)
                                .get("expectedError")
                                .asText();
            }

            log.info(
                    "Invalid users loaded: {} rows",
                    users.size()
            );

            return data;

        } catch (Exception e) {

            log.error("Could not load invalid users");

            throw new RuntimeException(e);
        }
    }
}

