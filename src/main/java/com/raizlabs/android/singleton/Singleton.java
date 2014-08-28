package com.raizlabs.android.singleton;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: This is a simple class that holds an instance of an object in a singleton. It does not require
 * static allocation and can be called from anywhere if you wish. Just call new Singleton(MyClass.class).
 * When you want to access the object call {@link #retrieve()} and when you want to release reference from both this instance
 * and the {@link com.raizlabs.android.singleton.SingletonManager}, just call {@link #remove()}.
 */
public class Singleton<DataClass> {

    /**
     * The class we will use to retrieve an instance from the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    private Class<DataClass> mDataClass;

    /**
     * The instance contained in the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    private DataClass mInstance;

    /**
     * Boolean determines whether we've determined that this class should persist. Note that this class
     * must implement {@link java.io.Serializable} for it work appropriately
     */
    private boolean persists = false;

    /**
     * Constructs an empty instance of the singleton. It does not retrieve an instance from the
     * {@link com.raizlabs.android.singleton.SingletonManager} until you call {@link #singleton()}
     * @param dataClass The class we will use to retrieve an instance from
     *                  the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    public Singleton(Class<DataClass> dataClass) {
        mDataClass = dataClass;
    }

    /**
     * Returns the singleton
     * @return
     */
    public DataClass retrieve() {
        if(mInstance == null) {
            mInstance = instantiate();
        }
        return mInstance;
    }

    /**
     * Returns the singleton and has it persist in disk storage
     * @return
     */
    @SuppressWarnings("unchecked")
    public DataClass retrievePersist() {
        if(mInstance == null) {
            persists = true;
            Class type = (Class) ((ParameterizedType)mDataClass.getGenericSuperclass()).getActualTypeArguments()[0];
            if(!Serializable.class.isAssignableFrom(type)) {
                throw new RuntimeException("Singleton must implement java.io.Serializable for it to persist");
            }
            mInstance = (DataClass) PersistentSingletonManager.getInstance().singleton((Class<Serializable>)mDataClass);
        }
        return mInstance;
    }

    /**
     * Destroys the singleton from the {@link com.raizlabs.android.singleton.SingletonManager} and
     * releases reference from this class. If this is persistent, it will delete it from the
     * {@link com.raizlabs.android.singleton.PersistentSingletonManager}
     * @return The item that was removed.
     */
    @SuppressWarnings("unchecked")
    public DataClass remove() {
        DataClass instance = null;
        if(mInstance != null) {
            if(!persists) {
                SingletonManager.remove(mDataClass);
            } else {
                PersistentSingletonManager.getInstance().destroySingleton((Class<Serializable>)mDataClass);
            }
            instance = mInstance;
            mInstance = null;
        }
        return instance;
    }

    /**
     * Creates this singleton. If you wish to override the behavior or provide some different mechanism
     * to creating it, make sure to add it the {@link com.raizlabs.android.singleton.SingletonManager} map.
     * @return The newly created instance of this object.
     */
    protected DataClass instantiate() {
        if(mInstance == null) {
            mInstance = SingletonManager.retrieve(mDataClass);
        }
        return mInstance;
    }
}
