package com.raizlabs.android.singleton;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: This class eliminates the need for "getInstance" of singleton type classes.
 * <br />
 * It enables <i>any</i> class to become a singleton instance.
 */
class SingletonManager {

    private static SingletonManager singletonManager;

    /**
     * The one singleton to rule them all. It will hold onto a map of Class-to-Objects statically
     * so we do not need to worry about for each singleton to have its own static "getInstance".
     * This method is not intended to be used as means to modify this class directly.
     *
     * @return
     */
    public static SingletonManager getInstance() {
        if (singletonManager == null) {
            singletonManager = new SingletonManager();
        }
        return singletonManager;
    }

    protected Map<Class, Map<String, SingletonInfo>> mSingletonMap;

    /**
     * Constructs and instantiates this singleton
     */
    public SingletonManager() {
        this.mSingletonMap = new HashMap<Class, Map<String, SingletonInfo>>();
    }

    private String getSingletonFileName(String key, Class clazz) {
        return "singleton" + File.pathSeparator + key + "_" + clazz.getName().replaceAll("\\.", "_");
    }

    /**
     * Puts this {@link DataClass} into the map. Warning, this will override any previous entry in the Map.
     *
     * @param singletonType The object put into the map
     */
    @SuppressWarnings("unchecked")
    public <DataClass> SingletonInfo<DataClass> makeSingleton(String key, DataClass singletonType, boolean persists) {
        SingletonInfo<DataClass> singletonInfo = new SingletonInfo<DataClass>(singletonType).setPersists(persists);
        getClassMap(singletonInfo.mDataClass).put(key, singletonInfo);
        if (persists) {
            save((SingletonInfo<? extends Serializable>) singletonInfo);
        }
        return singletonInfo;
    }

    /**
     * Returns a singular object that's created if it does not exist in the singleton.
     * It invokes the default constructor of the {@link DataClass} and returns a new instance if not in the map.
     *
     * @param typeClass The class of the singleton we want to retrieve
     * @return
     */
    @SuppressWarnings("unchecked")
    public <DataClass> SingletonInfo<DataClass> singleton(String key, Class<DataClass> typeClass, boolean persists) {
        SingletonInfo<DataClass> singleTon = (SingletonInfo<DataClass>) getClassMap(typeClass).get(key);
        if (singleTon == null) {
            try {
                singleTon = new SingletonInfo<DataClass>(typeClass).setPersists(persists).setTag(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(singleTon != null) {
                getClassMap(singleTon.mDataClass).put(key, singleTon);
            }
        }

        return singleTon;
    }

    /**
     * Retrieves and creates (if needed) the map of keys to a {@link com.raizlabs.android.singleton.SingletonInfo} class.
     * @param typeClass
     * @return
     */
    Map<String, SingletonInfo> getClassMap(Class typeClass) {
        Map<String, SingletonInfo> classMap = mSingletonMap.get(typeClass);
        if(classMap == null) {
            classMap = new HashMap<String, SingletonInfo>();
            mSingletonMap.put(typeClass, classMap);
        }
        return classMap;
    }

    /**
     * Saves the specified singleton to the local application data directory under "singleton/"
     *
     * @param singletonInfo The object singleton we want to save.
     */
    public void save(SingletonInfo<? extends Serializable> singletonInfo) {
        if (singletonInfo != null) {
            Object serializable = singletonInfo.getInstance();
            if (serializable != null) {
                try {
                    FileOutputStream file = Singleton.getContext()
                            .openFileOutput(getSingletonFileName(singletonInfo.mTag, serializable.getClass()),
                            Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
                    objectOutputStream.writeObject(serializable);
                    objectOutputStream.close();
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * It will delete the singleton file on disk
     * @param singletonInfo
     * @param <DataClass>
     */
    public <DataClass> void removePersistence(SingletonInfo<DataClass> singletonInfo) {
        if (singletonInfo != null && singletonInfo.isPersists()) {
            Singleton.getContext().deleteFile(getSingletonFileName(singletonInfo.mTag, singletonInfo.mDataClass));
        }
    }

    @SuppressWarnings("unchecked")
    public <DataClass extends Serializable> DataClass load(SingletonInfo<DataClass> singletonInfo) {
        String fName = getSingletonFileName(singletonInfo.mTag, singletonInfo.mDataClass);
        DataClass data = null;
        if(Singleton.getContext().getFileStreamPath(fName).exists()) {
            try {
                FileInputStream file = Singleton.getContext().openFileInput(fName);
                ObjectInputStream objectOutputStream = new ObjectInputStream(file);
                data = (DataClass) objectOutputStream.readObject();
                singletonInfo.setInstance(data);
                objectOutputStream.close();
                file.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            data = singletonInfo.newInstance();
            save(singletonInfo);
        }
        return data;
    }
}
