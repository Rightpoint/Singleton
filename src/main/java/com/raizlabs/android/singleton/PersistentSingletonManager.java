package com.raizlabs.android.singleton;

import android.content.Context;

import com.raizlabs.android.core.AppContext;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: This singleton manager will persist the data contained in this map. All objects and subobjects must
 * comply with {@link java.io.Serializable}.
 */
public class PersistentSingletonManager extends SingletonManager<Serializable> {

    private static PersistentSingletonManager singletonManager;

    /**
     * The one singleton to rule them all. It will hold onto a map of Class-to-Objects statically
     * so we do not need to worry about for each singleton to have its own static "getInstance".
     * This method is not intended to be used as means to modify this class
     * as the {@link #retrieve(Class)} and {@link #remove(Class)} provide what you need.
     *
     * @return
     */
    public static PersistentSingletonManager getInstance() {
        if (singletonManager == null) {
            singletonManager = new PersistentSingletonManager();
        }
        return singletonManager;
    }

    @Override
    public <SingletonType extends Serializable> SingletonType singleton(Class<SingletonType> typeClass) {
        boolean contains = mSingletonMap.containsValue(typeClass);
        SingletonType singletonType = super.singleton(typeClass);

        // On creation of the singleton, we will save it to its own unique file based on the hashCode.
        if (!contains) {
            save(singletonType);
        }
        return singletonType;
    }

    @Override
    public <SingletonType extends Serializable> SingletonType destroySingleton(Class<SingletonType> typeClass) {
        SingletonType singletonType = super.destroySingleton(typeClass);
        delete(singletonType);
        return singletonType;
    }

    @Override
    public <SingletonType extends Serializable> SingletonType makeSingleton(SingletonType singletonType) {
        SingletonType singleton = super.makeSingleton(singletonType);
        save(singleton);
        return singleton;
    }

    /**
     * Saves the specified singleton to the local application data directory under "singleton/"
     *
     * @param saveClass The class singleton we want to save.
     */
    public void save(Class<Serializable> saveClass) {
        Serializable serializable = singleton(saveClass);
        save(serializable);
    }

    /**
     * Saves the specified singleton to the local application data directory under "singleton/"
     *
     * @param saveClass The object singleton we want to save.
     */
    public void save(Serializable serializable) {
        if (serializable != null) {
            try {
                FileOutputStream file = AppContext.getInstance().openFileOutput("singleton/" + serializable.hashCode(),
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

    /**
     * Saves all of the data contained in the map.
     */
    public void saveAll() {
        Set<Class> keys = mSingletonMap.keySet();
        for (Class key : keys) {
            save(mSingletonMap.get(key));
        }
    }

    /**
     * Deletes the specified singleton class from the data directory under "singleton/"
     *
     * @param deleteClass The class singleton we want to delete
     */
    public void delete(Class<Serializable> deleteClass) {
        Serializable serializable = singleton(deleteClass);
        delete(serializable);
    }

    /**
     * Deletes the specified singleton object from the data directory under "singleton/".
     * Does not remove the item from the map.
     *
     * @param serializable The object singleton we want to delete
     */
    public void delete(Serializable serializable) {
        if (serializable != null) {
            AppContext.getInstance().deleteFile("singleton/" + serializable.hashCode() + "");
        }
    }

    /**
     * Deletes all data from disk contained in this manager. Does not remove them from this contained map.
     */
    public void deleteAll() {
        Set<Class> keys = mSingletonMap.keySet();
        for (Class key : keys) {
            delete(mSingletonMap.get(key));
        }
    }
}
