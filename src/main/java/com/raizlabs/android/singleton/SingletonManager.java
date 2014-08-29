package com.raizlabs.android.singleton;

import android.content.Context;

import com.raizlabs.android.core.AppContext;

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

    protected Map<Class, SingletonInfo> mSingletonMap;

    /**
     * Constructs and instantiates this singleton
     */
    public SingletonManager() {
        this.mSingletonMap = new HashMap<Class, SingletonInfo>();
    }

    /**
     * Puts this {@link DataClass} into the map. Warning, this will override any previous entry in the Map.
     *
     * @param singletonType The object put into the map
     */
    @SuppressWarnings("unchecked")
    public <DataClass> SingletonInfo<DataClass> makeSingleton(DataClass singletonType, boolean persists) {
        SingletonInfo<DataClass> singletonInfo = mSingletonMap.put(singletonType.getClass(),
                new SingletonInfo<DataClass>(singletonType).setPersists(persists));
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
    public <DataClass> SingletonInfo<DataClass> singleton(Class<DataClass> typeClass, boolean persists) {
        SingletonInfo<DataClass> singleTon = (SingletonInfo<DataClass>) mSingletonMap.get(typeClass);
        if (singleTon == null) {
            try {
                singleTon = new SingletonInfo<DataClass>(typeClass).setPersists(persists);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(singleTon != null) {
                mSingletonMap.put(typeClass, singleTon);
            }
        }

        return singleTon;
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
                    FileOutputStream file = AppContext.getInstance().openFileOutput("singleton/" +
                                    serializable.getClass().getName().hashCode(),
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
     * Removes the singleton from the map and returns it for any other residual cleanup.
     *
     * @param typeClass       The class of the singleton we want to destroy
     * @return
     */
    @SuppressWarnings("unchecked")
    public <DataClass> SingletonInfo<DataClass> destroySingleton(Class<DataClass> typeClass) {
        SingletonInfo<DataClass> singletonInfo = mSingletonMap.remove(typeClass);
        deleteSingleton(singletonInfo);
        return singletonInfo;
    }


    public <DataClass> void deleteSingleton(SingletonInfo<DataClass> singletonInfo) {
        if (singletonInfo != null && singletonInfo.isPersists()) {
            DataClass serializable = singletonInfo.getInstance();
            if (serializable != null) {
                AppContext.getInstance().deleteFile("singleton/" + serializable.hashCode() + "");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <DataClass extends Serializable> DataClass load(SingletonInfo<DataClass> singletonInfo) {
        String fName = "singleton/" +singletonInfo.getDataClass().getName().hashCode();
        DataClass data = null;
        if(AppContext.getInstance().getFileStreamPath(fName).exists()) {
            try {
                FileInputStream file = AppContext.getInstance().openFileInput(fName);
                ObjectInputStream objectOutputStream = new ObjectInputStream(file);
                data = (DataClass) objectOutputStream.readObject();
                singletonInfo.setInstance(data);
                objectOutputStream.close();
                file.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            data = singletonInfo.getInstance();
            save(singletonInfo);
        }
        return data;
    }
}
