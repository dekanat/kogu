# Kogu ⚙

**Kogu** is a Java compiler plugin, introducing exhaustive matching over **enum**-s. The enhancements of `switch` statements were [proposed](https://mail.openjdk.java.net/pipermail/amber-dev/2017-December/002412.html) in December, 2017 and made available as a [feature](https://openjdk.java.net/jeps/361) since Java 13.

Early thoughts by [Brian Goetz](https://www.linkedin.com/in/briangoetz) behind these changes can be found [here](https://cr.openjdk.java.net/~briangoetz/amber/pattern-match.html).

## Motivation

In CS, there is this idea of [Algebraic Data Types](https://en.wikipedia.org/wiki/Algebraic_data_type) that branches into [Product Types](https://en.wikipedia.org/wiki/Product_type) and [Sum Types](https://en.wikipedia.org/wiki/Sum_type). [Enumeration Types](https://en.wikipedia.org/wiki/Enumerated_type) are a special case of [Sum Types](https://en.wikipedia.org/wiki/Sum_type).

I won't go into much detail of what these types are and why they are useful. Instead, I'll briefly introduce the idea and provide some reading material on the matter, leaving the deeper investigation to the reader.

Enumerations are special in the sense of having exactly one value per type. This makes more sense, when we look at an enumeration of days of the week. In Java, it may look Like this:

```java
enum WeekDays {
  Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
}
```

It is obvious that there can be only one _(instance of)_ `Monday` or any other week day. In fact, the singleton nature of an enumeration type is guaranteed by Java itself.

Now, let's talk a bit about exhaustiveness in `switch` statements. By exhaustiveness, we mean that our `switch` statement considers all the cases the switch subject may represent. One way to achieve this is to provide a corresponding `case` for each of them, however this may not work for switches over `String`-s or `Integer`-s. After all, in most cases we are not interested in _all_ the numbers or strings in the universe, but a rather small subset of those. This holds true for all subjects that may represent infinitely many concrete cases. In such cases we can make the `switch` exhaustive by simply specifying the `default` case.

While this approach is the only conceivable way of dealing with strings and numbers, the enumerations are different, since for each enumeration we have a finite list of well defined cases. This means we can provide a `case` for each of them. Let us first discuss, what happens when we use the `default` with enums. Consider the following code:

```java
public class A {
  {
    E e = E.E1;

    switch (e) {
      case E1:
        System.out.println("Matched E1");
        break;
      default:
        System.out.println("Matched default");
        break;
    }
  }
}

enum E {
  E1;
}
```

This compiles just fine with

> $ javac A.java

But what happens when we add `E2` to our `E` enumeration like so?

```java
public class A { ... }

enum E {
  E1, E2;
}
```

Again, the code will compile and the `switch` will execute the `default` branch when the value of `e` is `E2`. This is the expected behaviour, according to Java specs, but is it the desirable one? Such a behaviour is, most of the times, undesirable, since the decision of handling `E2` as a `default` branch was made by the language, not the programmer. This is something we can cope with by carefully checking all our switches over enums, but inattentive code refactoring is a famous source of errors in programming.

We get a similar picture when we get rid of the `default` case in the switch over our `E` with two values like so:

```java
public class A {
  {
    E e = E.E1;

    switch (e) {
      case E1:
        System.out.println("Matched E1");
        break;
    }
  }
}

enum E {
  E1, E2;
}
```

In this scenario, the `E2` will not be handled at all, and, in both cases, this potential source of error will pass silently.

**Kogu** solves this issue by raising an error when an inexhaustive match is detected. When we compile the latter code with Kogu like so:

> $ javac -Xplugin:Kogu -cp *<path_to_kogu_jar>* A.java  

it will produce the following output:

```text
/full/path/to/A.java:5,4  
warning: Inexhaustive match detected over enum E  
  
     switch (e) {  
     ^  
  You are missing the following members [ E2 ]. You may also fix this by introducing the "default" case

1 warning
```

**Kogu** helps fixing such issues by reporting the locations of inexhaustive switches and the missing enumeration values. Alternatively, one can fix the issue by adding a `default` case. Either way, now the decision of ignoring a specific enumeration value or handling it is made explicitly by the programmer.

## Implementation

Some parts of the implementation are based on non-public APIs of `javac` compiler. Other parts of the implementation are based on `javac` public API which is documented to be different for different versions of Java. Naturally, a plugin built for one version of Java _may_ not work for a different version.

Since this feature is available from Java 13 on, Kogu is implemented for Java 8, 9, 10, 11, and 12. The implementation for Java 9, 10 and 11 is the same, hence there will be three branches for each different implementation:

* **j8** for Java 8
* **j11** as an umbrella branch for Java 9, 10 and 11
* **j12** for Java 12

## Build

To build the plugin, simply checkout the corresponding Java version branch and do:

> $ ./gradlew clean build

This should produce a `kogu-1.0.jar` jar file. Also make sure that the Java version you are building for is available on your path.

## Usage

### Reporting mode

Kogu supports two reporting modes: **strict** and **default**
In **strict** mode, all inexhaustive matches will be reported as errors, whereas in **default** mode they will be reported as warnings. Reporting mode is on when `-XDkogu.strict` option is specified.

### javac

To incorporate the plugin into your compilation, tell `javac` to use the plugin via `-Xplugin` option and make the plugin jar available on your classpath. It should look something this:

> $ javac -Xplugin:Kogu -cp *<path_to_kogu_jar>* *<the_rest_of_your_compilation_command>* [-XDkogu.strict]

### Gradle

To integrate the plugin in strict mode into your gradle build, you can specify it in `option`-s for `compileJava` tasks:

```groovy
compileJava {
  ...
  options.compilerArgs << "-Xplugin:Kogu -cp <path_to_kogu_jar> -XDkogu.strict"
  ...
}
```

## Supported Java versions

| Java version   | 8  |  9  | 10 | 11  | 12  |
|:---------------|:--:|:---:|:--:|:---:|:---:|
| **Supported**  | ✔️ | ✔️ | ✔️ | ✔️ | ❌ |
