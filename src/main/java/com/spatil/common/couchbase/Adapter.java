package com.spatil.common.couchbase;

import com.spatil.common.service.GameService;

public interface Adapter {
    public void set(String key, GameService value,int ttl);
    public GameService get(String key);
}
