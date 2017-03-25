package com.jarvislin.isitrainingnow.base;

import com.jarvislin.isitrainingnow.network.NetworkService;

/**
 * Created by JarvisLin on 2017/1/7.
 */

public class BaseRepository {
    protected final NetworkService networkService;
    public BaseRepository(NetworkService networkService) {
        this.networkService = networkService;
    }
}
