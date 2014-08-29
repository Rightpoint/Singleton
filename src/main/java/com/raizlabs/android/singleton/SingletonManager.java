package com.raizlabs.android.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: This class eliminates the need for "getInstance" of singleton type classes.
 * <br />
 * It enables <i>any</i> class to become a singleton instance.
 */
public class SingletonManager<DataClass> {

    private static SingletonManager singletonManager;

    /**
     * The one singleton to rule them all. It will hold onto a map of Class-to-Objects statically
     * so we do not need to worry about for each singleton to have its own static "getInstance".
     * This method is not intended to be used as means to modify this class
     * as the {@link #retrieve(Class)} and {@link #remove(Class)} provide what you need.
     *
     * @return
     */
    public static SingletonManager getInstance() {
        if (singletonManager == null) {
            singletonManager = new SingletonManager();
        }
        return singletonManager;
    }

    /**
     * Returns a singular object that's created if it does not exist in the singleton.
     * It invokes the default constructor of the {@link SingletonType} and returns a new instance if not in the map.
     *
     * @param typeClass       The class of the singleton we want to retrieve
     * @param <SingletonType> the return type param of the singleton
     * @return
     * @see {@link #singleton(Class)}
     */
    @SuppressWarnings("unchecked")
    public static <SingletonType> SingletonType retrieve(Class<SingletonType> typeClass) {
        return (SingletonType) getInstance().singleton(typeClass);
    }

    /**
     * Removes the singleton from the map and returns it for any other residual cleanup.
     *
     * @param typeClass       The class of the singleton we want to destroy
     * @param <SingletonType> the return type param of the singleton
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <SingletonType> SingletonType remove(Class<SingletonType> typeClass) {
        return (SingletonType) getInstance().destroySingleton(typeClass);
    }

    protected Map<Class, DataClass> mSingletonMap;

    /**
     * Constructs and instantiates this singleton
     */
    public SingletonManager() {
        this.mSingletonMap = new HashMap<Class, DataClass>();
    }

    /**
     * Returns a singular object that's created if it does not exist in the singleton.
     * It invokes the default constructor of the {@link SingletonType} and returns a new instance if not in the map.
     *
     * @param typeClass       The class of the singleton we want to retrieve
     * @param <SingletonType> the return type param of the singleton
     * @return
     */
    @SuppressWarnings("unchecked")
    public <SingletonType extends DataClass> SingletonType singleton(Class<SingletonType> typeClass) {
        SingletonType singleTon = (SingletonType) mSingletonMap.get(typeClass);
        if (singleTon == null) {
            try {
                singleTon = typeClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSingletonMap.put(typeClass, singleTon);
        }
        return singleTon;
    }

    /**
     * Removes the singleton from the map and returns it for any other residual cleanup.
     *
     * @param typeClass       The class of the singleton we want to destroy
     * @param <SingletonType> the return type param of the singleton
     * @return
     */
    @SuppressWarnings("unchecked")
    public <SingletonType extends DataClass> SingletonType destroySingleton(Class<SingletonType> typeClass) {
        return (SingletonType) mSingletonMap.remove(typeClass);
    }

    /**
     * Puts this {@link SingletonType} into the map. Warning, this will override any previous entry in the Map.
     * @param singletonType The object put into the map
     * @param <SingletonType> the type param of the singleton
     */
    @SuppressWarnings("unchecked")
    public <SingletonType extends DataClass> SingletonType makeSingleton(SingletonType singletonType) {
        return (SingletonType) mSingletonMap.put(singletonType.getClass(), singletonType);
    }

}
