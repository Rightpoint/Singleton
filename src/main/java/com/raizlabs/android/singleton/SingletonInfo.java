package com.raizlabs.android.singleton;

import java.io.Serializable;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: This holds the instance variable in a wrapper to mark it as persistent.
 */
class SingletonInfo<DataClass> {

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
     * The class we will use to retrieve an instance from the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    private Class<DataClass> mDataClass;


    public SingletonInfo(Class<DataClass> dataClass) {
        mDataClass = dataClass;
    }

    @SuppressWarnings("unchecked")
    public SingletonInfo(DataClass data) {
        mInstance = data;
        mDataClass = (Class<DataClass>) mInstance.getClass();
    }

    protected void checkSerializable() {
        if(!Serializable.class.isAssignableFrom(mDataClass)) {
            throw new IllegalArgumentException("DataClass from SingletonInfo must implement java.io.Serializable");
        }
    }

    /**
     * If true, this will mark the object as persisting and load the object (if exists, otherwise will save).
     * @param persists
     * @return
     */
    @SuppressWarnings("unchecked")
    public SingletonInfo setPersists(boolean persists) {
        this.persists = persists;
        if(persists) {
            getInstance();
        } else {
            SingletonManager.getInstance().removePersistence(this);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public void save() {
        if(!persists) {
            setPersists(true);
        } else {
            checkSerializable();
            SingletonManager.getInstance().save((SingletonInfo<? extends Serializable>) this);
        }
    }

    public void setInstance(DataClass instance) {
        this.mInstance = instance;
    }

    public boolean isPersists() {
        return persists;
    }

    /**
     * Creates the instance in this class if it currently has none, or loads it from disk if its persistent.
     * @return
     */
    @SuppressWarnings("unchecked")
    public DataClass getInstance() {
        if(mInstance == null) {
            if(persists) {
                checkSerializable();
                mInstance = (DataClass) SingletonManager.getInstance().load((SingletonInfo<? extends Serializable>) this);
            } else {
                try {
                    mInstance = mDataClass.newInstance();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return mInstance;
    }

    public Class<DataClass> getDataClass() {
        return mDataClass;
    }

    /**
     * Returns whether this currently has the instance object we want
     * @return
     */
    public boolean hasInstance() {
        return mInstance!=null;
    }

    /**
     * Removes the object from the {@link com.raizlabs.android.singleton.SingletonManager},
     * deletes it from persistent storage, and releases its reference.
     */
    public void remove() {
        delete();
        SingletonManager.getInstance().mSingletonMap.remove(mDataClass);
    }

    /**
     * Releases the reference to the singleton object in the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    public void release() {
        mInstance = null;
    }

    /**
     * Destroys the singleton from the {@link com.raizlabs.android.singleton.SingletonManager} and
     * releases reference from this class. If this is persistent, it will delete it from internal storage.
     * @return The item that was removed.
     */
    public DataClass delete() {
        DataClass instance = mInstance;
        release();
        SingletonManager.getInstance().removePersistence(this);
        return instance;
    }
}
