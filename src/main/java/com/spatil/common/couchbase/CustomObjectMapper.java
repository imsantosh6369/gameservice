package com.spatil.common.couchbase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 2686298573056737140L;

    public CustomObjectMapper() {
        super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}