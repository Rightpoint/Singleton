
[![Raizlabs Repository](http://img.shields.io/badge/Raizlabs%20Repository-1.1.0-blue.svg?style=flat)](https://github.com/Raizlabs/maven-releases)

# Singleton


### Enables a very centralized and easy way to manage singletons within an application.

## Getting Started

Add the maven repo url to your build.gradle:

```groovy

  repositories {
        maven { url "https://raw.github.com/Raizlabs/maven-releases/master/releases" }
  }

```

Add the library to the project-level build.gradle, using the [apt plugin](https://bitbucket.org/hvisser/android-apt) and the 
[AARLinkSources](https://github.com/xujiaao/AARLinkSources) plugin::

```groovy

  dependencies {
    compile 'com.raizlabs.android:Singleton:1.0.0'
    aarLinkSources 'com.raizlabs.android:Singleton:1.1.0:sources@jar'
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


```java

public void someMethod() {
  
  // Retrieves the singleton instance into a local variable!
  MyClass myObject = new Singleton(MyClass.class).getInstance();

}

```

#### Removal

```java

  // Deletes from persistent storage if saved, and released from the map and current singleton referent
  mySingleton.delete();

  // Removes the grasp on the contained reference and sets it to null. 
  // Good for freeing up persistent data
  mySingleton.release();

```

#### More

```java
  
   // Saves the object to persistent storage
   mySingleton.save();

```
