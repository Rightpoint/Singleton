package com.raizlabs.android.singleton.test;

import android.test.AndroidTestCase;

import com.raizlabs.android.singleton.Singleton;

import java.io.Serializable;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class SingletonTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Singleton.init(getContext());
    }

    public void testRegularSingleton() {
        // test two are same
        Singleton<String> testSingleton = new Singleton<String>("Test");
        Singleton<String> otherSingleton = new Singleton<String>(String.class);
        assertEquals(otherSingleton.getInstance(), testSingleton.getInstance());

        // test custom object works
        Singleton<TestObject> testObjectSingleton = new Singleton<TestObject>(TestObject.class);
        assertNotNull(testObjectSingleton.getInstance());

        TestObject testObject = testObjectSingleton.getInstance();
        testObject.name = "Test";
        testObject.isCool = true;

        Singleton<TestObject> testOtherSingleton = new Singleton<TestObject>(TestObject.class);
        assertNotNull(testOtherSingleton.getInstance());

        assertEquals(testOtherSingleton.getInstance(), testObjectSingleton.getInstance());

        testObjectSingleton.release();

        assertTrue(!testObjectSingleton.hasInstance());

    }


    public void testSaveableSingleton() {
        Singleton<TestObject> testObjectSingleton = new Singleton<TestObject>(TestObject.class);

        // Test to ensure we cannot save non serializeable
        boolean failed = false;
        try {
            testObjectSingleton.save();
        } catch (IllegalArgumentException i) {
            failed = true;
        }

        assertTrue(failed);

        Singleton<TestSerializeableObject> testSerializeableObjectSingleton = new Singleton<TestSerializeableObject>(TestSerializeableObject.class);
        testSerializeableObjectSingleton.save();
        assertTrue(testSerializeableObjectSingleton.isOnDisk());

        Singleton<TestSerializeableObject> otherSerializableSingleton = new Singleton<TestSerializeableObject>(TestSerializeableObject.class);
        assertEquals(testSerializeableObjectSingleton.getInstance(), otherSerializableSingleton.getInstance());

        testSerializeableObjectSingleton.delete();
        assertFalse(testSerializeableObjectSingleton.isOnDisk());
    }

    public void testTagSingletons() {
        Singleton<TestObject> objectSingleton = new Singleton<TestObject>("tag", TestObject.class);
        objectSingleton.getInstance();

        Singleton<TestObject> otherObjectSingleton = new Singleton<TestObject>("tag2", TestObject.class);
        otherObjectSingleton.getInstance();

        assertNotSame(objectSingleton.getInstance(), otherObjectSingleton.getInstance());

        objectSingleton.delete();
        otherObjectSingleton.delete();

    }

    private static class TestObject {

        private String name;

        private boolean isCool;
    }

    private static class TestSerializeableObject extends TestObject implements Serializable {

    }
}
