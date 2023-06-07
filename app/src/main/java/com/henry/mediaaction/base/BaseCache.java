package com.henry.mediaaction.base;

import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;

import com.henry.mediaaction.utils.Logger;
import com.tencent.mmkv.MMKV;

import java.util.Random;
import java.util.Set;
import java.util.Vector;

public abstract class BaseCache {

    private static final String TAG = BaseCache.class.getSimpleName();
    private static final String defaultMMKV = "defaultMMKV";
    protected MMKV mmkv;
    private static final Vector<MMKV> whiteList = new Vector<>();//白名单
    public static final Vector<MMKV> mmkvs = new Vector<>();

    public void init(Context context) {
        if (mmkv == null) {
            synchronized (BaseCache.class) {
                if (mmkv == null) {
                    MMKV.initialize(context);
                    if (getMMkvInstance() == null) {
                        mmkv = MMKV.mmkvWithID(defaultMMKV);
                    } else {
                        mmkv = getMMkvInstance();
                    }
                    mmkvs.add(mmkv);
                }
            }
        }
    }

    /**
     * 加入到白名单
     */
    public void setWhiteList() {
        whiteList.add(mmkv);
    }
    /**
     * 设置mmkv加密密钥
     *
     * @param key
     */
    public void setEncryptWithKey(String key) {
        if (mmkv != null) {
            mmkv.reKey(key);
        }
    }

    /**
     * 子类处理具体的MMKV实例化文件名字，
     * 如果为null则会生成默认的mmkv缓存文件
     *
     * @return
     */
    public abstract MMKV getMMkvInstance();

    public void putString(String key, String obj) {
        try {
            mmkv.putString(key, obj);
        } catch (Exception e) {
            Logger.e(TAG, "putString: " + e.toString());
        }
    }

    public void putDouble(String key, double obj) {
        try {
            mmkv.encode(key, obj);
        } catch (Exception e) {
            Logger.e(TAG, "putDouble: " + e.toString());
        }
    }

    public void putInt(String key, int obj) {
        try {
            mmkv.encode(key, obj);
        } catch (Exception e) {
            Logger.e(TAG, "putInt: " + e.toString());
        }
    }

    public void putFloat(String key, float obj) {
        try {
            mmkv.encode(key, obj);
        } catch (Exception e) {
            Logger.e(TAG, "putFloat: " + e.toString());
        }
    }

    public void putBoolean(String key, boolean obj) {
        try {
            mmkv.encode(key, obj);
        } catch (Exception e) {
            Logger.e(TAG, "putBoolean: " + e.toString());
        }
    }

    public void putParcelable(String key, Parcelable obj) {
        try {
            mmkv.encode(key, obj);
        } catch (Exception e) {
            Logger.e(TAG, "putParcelable: " + e.toString());
        }
    }

    public void putBytes(String key, byte[] obj) {
        try {
            mmkv.encode(key, obj);
        } catch (Exception e) {
            Logger.e(TAG, "putBytes: " + e.toString());
        }
    }

    public void putLong(String key, long obj) {
        try {
            mmkv.encode(key, obj);
        } catch (Exception e) {
            Logger.e(TAG, "putLong: " + e.toString());
        }
    }

    public void putStringSets(String key, Set<String> obj) {
        try {
            byte[] bytes = new byte[]{'a', 'B', 'C', 'd'};
            mmkv.encode(key, obj);
        } catch (Exception e) {
            Logger.e(TAG, "putStringSets: " + e.toString());
        }
    }


    public String getString(String key) {
        String s;
        try {
            s = mmkv.getString(key, "");
            return s;
        } catch (Exception e) {
            Logger.e(TAG, "getString: " + e.toString());
        }

        return null;
    }

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    /**
     * (获取指定长度uuid)
     *
     * @return
     */
    private static String getUUID(int len) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < 32; i++) {
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return sb.toString();

    }

    /**
     * 带默认值的
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        String s;
        try {
            if (mmkv == null) {
                return defaultValue;
            }
            s = mmkv.decodeString(key);
            return s == null ? defaultValue : s;
        } catch (Exception e) {
            s = defaultValue;
            Logger.e(TAG, "getString: " + e.toString());
        }
        return s;
    }

    public int getInt(String key) {
        int value;
        try {
            if (mmkv == null) {
                return -1;
            }
            value = mmkv.decodeInt(key, -1);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getInt: " + e.toString());
        }

        return -1;
    }

    public int getInt(String key, int defaultValue) {
        int value;
        try {
            if (mmkv == null) {
                return defaultValue;
            }
            value = mmkv.decodeInt(key, defaultValue);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getInt: " + e.toString());
        }

        return -1;
    }

    public boolean getBoolean(String key) {
        boolean value;
        try {
            if (mmkv == null) {
                return false;
            }
            value = mmkv.decodeBool(key, false);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getBoolean: " + e.toString());
        }

        return false;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        boolean value;
        try {
            if (mmkv == null) {
                return defaultValue;
            }
            value = mmkv.decodeBool(key, defaultValue);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getBoolean: " + e.toString());
        }

        return false;
    }

    public double getDouble(String key) {
        double value;
        try {
            value = mmkv.decodeDouble(key);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getDouble: " + e.toString());
        }
        return 0.0;
    }

    public float getFloat(String key) {
        float value;
        try {
            value = mmkv.decodeFloat(key);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getFloat: " + e.toString());
        }
        return 0.0f;
    }

    public long getLong(String key) {
        long value;
        try {
            value = mmkv.decodeLong(key);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getBoolean: " + e.toString());
        }
        return 0L;
    }

    public <T extends Parcelable> T getParcelable(String key, Class<T> tClass) {
        T value;
        try {
            value = mmkv.decodeParcelable(key, tClass);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getParcelable: " + e.toString());
        }
        return null;
    }

    public byte[] getBytes(String key) {
        byte[] value;
        try {
            value = mmkv.decodeBytes(key);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getBytes: " + e.toString());
        }
        return null;
    }

    public Set<String> getStringSets(String key) {
        Set<String> value;
        try {
            value = mmkv.decodeStringSet(key);
            return value;
        } catch (Exception e) {
            Logger.e(TAG, "getStringSets: " + e.toString());
        }
        return null;
    }

    public void removeValueForKey(String key) {
        if (mmkv != null) {
            mmkv.removeValueForKey(key);
        }
    }

    public boolean containsKey(String key) {
        if (mmkv == null) {
            return false;
        }
        return mmkv.containsKey(key);
    }


}
