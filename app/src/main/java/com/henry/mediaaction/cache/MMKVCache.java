package com.henry.mediaaction.cache;

import com.henry.mediaaction.base.BaseCache;
import com.tencent.mmkv.MMKV;

public class MMKVCache extends BaseCache {
    private static volatile MMKVCache mmkvCache;

    private MMKVCache() {
    }
    public static MMKVCache getInstance() {
        if (null == mmkvCache) {
            synchronized (MMKVCache.class) {
                if (null == mmkvCache) {
                    mmkvCache = new MMKVCache();
                }
            }
        }
        return mmkvCache;
    }
    /**
     * 默认的MMKV文件实例，单进程
     * @return
     */
    @Override
    public MMKV getMMkvInstance() {
        return MMKV.defaultMMKV();
    }

}
