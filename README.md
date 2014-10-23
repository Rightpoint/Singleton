# Singleton


### Enables a very centralized and easy way to manage singletons within an application.

## Getting Started

Add this line to your build.gradle (assuming you're using the RaizLibraryPlugin): 

```groovy

  dependency "Singleton"

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