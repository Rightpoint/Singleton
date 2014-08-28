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

There are a few ways in this library to retrieve a singleton, but this is the suggested way.

```java

public void someMethod() {
  
  // Retrieves the singleton instance into a local variable!
  MyClass myObject = new Singleton(MyClass.class).retrieve();

  // Statically get the object
  MyClass myObject = SingletonManager.retrieve(MyClass.class);

  // Statically get the object nonstatically
  MyClass myObject = SingletonManager.getInstance().singleton(MyClass.class);
}

```

#### Removal

```java

  myObject.remove();

  // or
  SingletonManager.remove(MyClass.class);

  // or
  SingletonManager.getInstance().destroySingleton(MyClass.class);

```
