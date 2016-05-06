## Important Notice
This repository is slated for deletion, perhaps as soon as June 1, 2016.  Please find other solutions.  
There will be no future updates or support. 


[![Raizlabs Repository](http://img.shields.io/badge/Raizlabs%20Repository-1.0.1-blue.svg?style=flat)](https://github.com/Raizlabs/maven-releases)
[![Android-Libs](https://img.shields.io/badge/Android--Libs-Singleton-orange.svg?style=flat)](http://android-libs.com/lib/singleton)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Singleton-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1294)
[![Android Weekly](http://img.shields.io/badge/Android%20Weekly-%23134-2CB3E5.svg?style=flat)](http://androidweekly.net/issues/issue-134)

# Singleton

A very, very compact library that enables you to create __on-demand__ singletons within your application and easily store them to disk. Utilizing a dead-simple API, this library makes creating singletons and persisting data much more fun!

## Changlog

### 1.0.1
  1. Added the ```Singleton.Builder``` class
  2. Added ```persists()``` to see if a ```Singleton``` is set to exist
  3. Added ```getTag()``` to retrieve a ```Singleton``` tag

## Getting Started

Add the maven repo url to your build.gradle:

```groovy

  repositories {
        maven { url "https://raw.github.com/Raizlabs/maven-releases/master/releases" }
  }

```

Add the library to the project-level build.gradle, using the
[AARLinkSources](https://github.com/xujiaao/AARLinkSources) plugin::

```groovy

  dependencies {
    compile 'com.raizlabs.android:Singleton:1.0.1'
    aarLinkSources 'com.raizlabs.android:Singleton:1.0.1:sources@jar'
  }

```

## Usage

We want to use a Singleton. It should be easy, statically referenced, and **only** created when we need it (lazy instantiation).

### Using the Singleton Class

#### Configuration

In your ```Application``` class, call:

```java
 
  Singleton.init(this);

```

#### Retrieval

Features:
  1. Will create a singleton for you using the default constructor of the object if it does not exist already. 
  2. **Tagging**: enables multiple different singletons in memory or on disk that belong to the same class.
  3. Disk objects must be serializable
  4. On-demand memory usage so you can release an object onto disk and retrieve it when you need it!

```java

public void someMethod() {
  
  // Retrieves the singleton instance into a local variable!
  MyClass myObject = new Singleton(MyClass.class).getInstance();

  // Retrieve with a tag
  MyClass myObject = new Singleton("Tag", MyClass.class).getInstance();
  
}

```

#### Removal

Use the singleton to free up or delete memory.

```java

  // Deletes from persistent storage if saved, and released from the map and current singleton referent
  mySingleton.delete();

  // Removes the grasp on the contained reference and sets it to null. 
  // Good for freeing up persistent data
  mySingleton.release();

```

#### Storing

Will automatically convert the ```Singleton``` to persistent as long as it's ```Serializable```, otherwise an exception will be logged.

```java
  
   // Saves the object to persistent storage
   mySingleton.save();

```

#### Builder

The ```Singleton.Builder``` class is an easier way to create ```Singleton```:

```java
      Singleton<TestObject> testObjectSingleton = new Singleton.Builder<>()
        .tag("Test")
        .type(TestObject.class)
        .persists(true)
        .build();
```
