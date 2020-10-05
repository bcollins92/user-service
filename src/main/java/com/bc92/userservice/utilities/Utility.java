package com.bc92.userservice.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {

  private final static Logger logger = LoggerFactory.getLogger(Utility.class);

  private static ObjectMapper mapper = new ObjectMapper();

  public static <T> T jsonToObject(final String jsonString, final Class<T> clazz) {
    try {
      return mapper.readValue(jsonString, clazz);
    } catch (JsonProcessingException e) {
      logger.error("Failed to read Json to class {}", clazz.getCanonicalName(), e);
    }
    return null;
  }

}
