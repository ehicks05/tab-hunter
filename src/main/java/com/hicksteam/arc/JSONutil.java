package com.hicksteam.arc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JSONutil
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    final static ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object value)
    {
        try
        {
            return objectMapper.writeValueAsString(value);
        }
        catch (JsonProcessingException e)
        {
            log.error(e.getMessage(), e);
        }

        return "";
    }
}
