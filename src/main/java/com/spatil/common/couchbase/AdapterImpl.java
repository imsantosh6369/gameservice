package com.spatil.common.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.StringDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatil.common.service.GameService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class AdapterImpl implements Adapter{

    private Bucket gamesBucket;
    private static final ObjectMapper jacksonMapper = new CustomObjectMapper();
    final static Logger logger = Logger.getLogger(GameService.class);

    public AdapterImpl() {
        Cluster cluster = CouchbaseCluster.create();
        cluster.authenticate("Administrator","password");
        gamesBucket=cluster.openBucket("GameBucket");
    }

    public GameService get(String key) {
        StringDocument doc = gamesBucket.get(key, StringDocument.class);
        if (doc != null && !StringUtils.isBlank(doc.content())) {
            return getObjectFromJson(doc.content(), GameService.class);
        }
        logger.warn("VoiceRequest: CBContainer No document found for CBKey=" + key);
        return null;
    }

    public void set(String key, GameService value, int timeToLive) {
        String val = getObjectToJSONString(value);
        StringDocument doc = StringDocument.create(key, timeToLive * 60, val);
        gamesBucket.upsert(doc);
    }

    public static <T> T getObjectFromJson(String json, Class<T> clazz) {
        if (!StringUtils.isBlank(json)) {
            int idex=json.indexOf("currentGameStatus");
            json=json.substring(0,idex-3);
            json=json+"}}";
            try {
                return jacksonMapper.readValue(json, clazz);
            } catch (Exception e) {
                logger.error("Unable to convert json to clazz " + clazz.getSimpleName());
            }
        }
        return null;
    }

    public static String getObjectToJSONString(Object obj) {

        try {
            return jacksonMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("Unable to parse Object to String");
        }
        return null;

    }
}
