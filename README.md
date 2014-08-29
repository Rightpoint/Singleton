# Singleton


### Enables a very centralized and easy way to manage singletons within an application.

## Getting Started

Add this line to your build.gradle (assuming you're using the RaizLibraryPlugin): 

```groovy

  raizCompile "Singleton"

```

This project depends on [Core](https://bitbucket.org/raizlabs/core).

## Usage

We want to use a Singleton. It should be easy, statically referenced, and **only** created when we need it (lazy instantiation).

### Using the Singleton Class

#### Retrieval


```java

public void someMethod() {
  
  // Retrieves the singleton instance into a local variable!
  MyClass myObject = new Singleton(MyClass.class).getInstance();

}

```

#### Removal

```java

  // Deletes from persistent storage
  mySingleton.delete();

  // Removes the grasp on the contained reference and sets it to null. 
  // Good for freeing up persistent data
  mySingleton.release();

  // Deletes, releases, and removes itself from the SingletonManager
  mySingleton.remove();


```

#### More

```java
  
   // Saves the object to persistent storage
   myObject.save();

```