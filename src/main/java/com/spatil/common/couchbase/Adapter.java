package com.spatil.common.couchbase;

import com.spatil.common.service.GameService;

public interface Adapter {
     void set(String key, GameService value,int ttl);
     GameService get(String key);
}
