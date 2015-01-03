package com.raizlabs.android.singleton;

import android.content.Context;

/**
 * Author: andrewgrosner
 * Description: This is a simple class that holds an instance of an object in a singleton. It does not require
 * static allocation and can be called from anywhere if you wish. Just call new Singleton(MyClass.class).
 * When you want to access the object call {@link #getInstance()} and when you want to release reference from both this instance
 * and the {@link com.raizlabs.android.singleton.SingletonManager}, just call {@link #release()}.
 */
public class Singleton<DataClass> {

    private static Context mContext;

    public static Context getContext() {
        if (mContext == null) {
            throw new IllegalStateException("Context for Singleton must not be null");
        }
        return mContext;
    }

    public static void init(Context context) {
        mContext = context;
    }

    private SingletonInfo<DataClass> mSingletonInfo;

    /**
     * Constructs an empty instance of the singleton. It does not create or retrieve the instance of the data
     * until you call {@link #getInstance()}. It will by default not persist the object until you {@link #save()}
     *
     * @param dataClass The class we will use to retrieve an instance from
     *                  the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    public Singleton(Class<DataClass> dataClass) {
        this(dataClass, false);
    }

    /**
     * Constructs an empty instance of the singleton. It does not create or retrieve the instance of the data
     * until you call {@link #getInstance()}. It will by default not persist the object until you {@link #save()}
     *
     * @param tag       The unique tag for this class to use when held in the manager.
     * @param dataClass The class we will use to retrieve an instance from
     *                  the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    public Singleton(String tag, Class<DataClass> dataClass) {
        this(tag, dataClass, false);
    }


    /**
     * Constructs an empty instance of the singleton. It does not create or retrieve the instance of the data
     * until you call {@link #getInstance()}
     *
     * @param tag       The unique tag for this class to use when held in the manager.
     * @param dataClass The class we will use to retrieve an instance from
     *                  the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    public Singleton(String tag, Class<DataClass> dataClass, boolean persists) {
        mSingletonInfo = SingletonManager.getInstance().singleton(tag, dataClass, persists);
    }

    /**
     * Constructs an empty instance of the singleton. It does not create or retrieve the instance of the data
     * until you call {@link #getInstance()}
     *
     * @param dataClass The class we will use to retrieve an instance from
     *                  the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    public Singleton(Class<DataClass> dataClass, boolean persists) {
        this(dataClass.getSimpleName(), dataClass, persists);
    }


    /**
     * Constructs an instance of the singleton with the specified object.
     * It will by default not persist the object until you {@link #save()}
     *
     * @param instance The object that we will turn into a singleton.
     */
    public Singleton(DataClass instance) {
        this(instance.getClass().getSimpleName(), instance, false);
    }

    /**
     * Constructs an instance of the singleton with the specified object.
     * It will by default not persist the object until you {@link #save()}
     *
     * @param tag      The unique tag for this class to use when held in the manager.
     * @param instance The object that we will turn into a singleton.
     */
    public Singleton(String tag, DataClass instance) {
        this(tag, instance, false);
    }

    /**
     * Constructs an instance of the singleton with the specified object and persistence.
     *
     * @param instance The object that we will turn into a singleton.
     * @param persists Whether we want it on disk or not.
     */
    public Singleton(DataClass instance, boolean persists) {
        this(instance.getClass().getSimpleName(), instance, persists);
    }

    /**
     * Constructs an instance of the singleton with the specified object and persistence.
     *
     * @param tag      The unique tag for this class to use when held in the manager.
     * @param instance The object that we will turn into a singleton.
     * @param persists Whether we want it on disk or not.
     */
    public Singleton(String tag, DataClass instance, boolean persists) {
        mSingletonInfo = SingletonManager.getInstance().makeSingleton(tag, instance, persists);
    }

    /**
     * Creates (if necessary, using the default constructor) and returns the singleton.
     *
     * @return
     */
    public DataClass getInstance() {
        return mSingletonInfo.getInstance();
    }

    /**
     * @return true if the singleton still has an instance of the object or false if it is not
     * created yet.
     */
    public boolean hasInstance() {
        return mSingletonInfo.hasInstance();
    }

    /**
     * @return true if the singleton is saved on disk. Will always return false if this instance is not
     * persistent.
     */
    public boolean isOnDisk() {
        return mSingletonInfo.isOnDisk();
    }

    /**
     * Saves the object into persistent storage.
     *
     * @return The saved instance
     */
    public DataClass save() {
        mSingletonInfo.save();
        return mSingletonInfo.getInstance();
    }

    /**
     * Destroys the singleton from the {@link com.raizlabs.android.singleton.SingletonManager} and
     * releases reference from this class. If this is persistent, it will delete it from internal storage.
     *
     * @return The item that was removed.
     */
    public DataClass delete() {
        return mSingletonInfo.delete();
    }

    /**
     * Releases the reference to the singleton in the {@link com.raizlabs.android.singleton.SingletonManager}
     */
    public void release() {
        mSingletonInfo.release();
    }

    /**
     * @return The tag that this singleton uses
     */
    public String getTag() {
        return mSingletonInfo.mTag;
    }

    /**
     * @return True if this class persists
     */
    public boolean persists() {
        return mSingletonInfo.isPersists();
    }

    /**
     * Aids in constructing a singleton
     */
    public static class Builder<DataClass> {

        /**
         * The class to construct using the default constructor
         */
        private Class<DataClass> mDataClass;

        /**
         * A pre-created instance we will keep in memory and/or on disk
         */
        private DataClass mDataInstance;

        /**
         * The tag to assign to it if we wish to have multiple of the same class. Default will be the
         * simpleName of the {@link DataClass}
         */
        private String mTag;

        /**
         * If true, it will be stored immediately to disk and on all future {@link #save()} calls
         */
        private boolean persists;

        /**
         * Sets a pre-created instance for this builder and if a tag has not already been set, it will
         * set the tag to be the simple name of the class.
         *
         * @param instance The not-null instance to store
         * @return This builder
         */
        public Builder<DataClass> instance(DataClass instance) {
            mDataInstance = instance;
            if (mTag == null || mTag.length() == 0) {
                tag(mDataInstance.getClass().getSimpleName());
            }
            return this;
        }

        /**
         * Sets the type to create for this singleton using the default constructor. If a tag has not already
         * been set, it will use the simple name of the class.
         *
         * @param type The class of the singleton to construct
         * @return This type
         */
        public Builder<DataClass> type(Class<DataClass> type) {
            mDataClass = type;
            if (mTag == null || mTag.length() == 0) {
                tag(mDataClass.getSimpleName());
            }
            return this;
        }

        /**
         * The tag to assign to this singleton. If not unique, it may overwrite another singleton.
         *
         * @param tag The unique tag for this class to use when held in the manager.
         * @return This type
         */
        public Builder<DataClass> tag(String tag) {
            mTag = tag;
            return this;
        }

        /**
         * Sets this singleton to persist. If true, it must be a serializable object otherwise an exception
         * will be thrown.
         *
         * @param persists
         * @return
         */
        public Builder<DataClass> persists(boolean persists) {
            this.persists = persists;
            return this;
        }

        /**
         * Will build and create the {@link com.raizlabs.android.singleton.Singleton}
         *
         * @return A constructed {@link com.raizlabs.android.singleton.Singleton} from the specified data.
         */
        public Singleton<DataClass> build() {
            Singleton<DataClass> singleton;
            if (mDataInstance != null) {
                singleton = new Singleton<>(mTag, mDataInstance, persists);
            } else {
                singleton = new Singleton<>(mTag, mDataClass, persists);
            }
            return singleton;
        }
    }
}
