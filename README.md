# Cut.kt - C Unit Testing with Kotlin

Proof of concept for testing C code using Kotlin/Native.

```c
// C
int add(int x, int y)
```

```kotlin
// Kotlin
testsuite("Add") {
    test("Simple addition") {
        assertEquals(4, add(1, 3))
    }
}
```

```sh
# Your favorite terminal
$ make check
=== TESTSUITE: Add ===
PASS: Simple addition
=== Add: 1 passing ===
```

This is not production-ready by any means, and is just a proof-of-concept.

Most of the magic happens in the Makefile. Read the comments there to find out more.

In order to run this mess (which you should consider not doing), you will need:

- Linux. Windows is not worthy enough of such crap programming
- A JDK ([AdoptOpenJDK is a good start](https://adoptopenjdk.net/installation.html#linux-pkg))
- Install `libncurses5` for some reason (compilation of Kotlin stuff fails otherwise)

You do not need to have Kotlin/Native on your machine, the Makefile will download it automatically.

Then, simply run `make check`.

## Why?

I wanted an excuse for writing Kotlin code.

## Example

![Screenshot](screenshot.png)

## Architecture

* `src`: C source files
* `tests`: Kotlin test files
    * `Cut.kt`: Very simple test framework implementation
    * `Testsuite.kt`: Your tests here.
* `Makefile`: The Makefile
* `test_target.def`: Information file for KLib generation

All Cut.kt-related stuff is built into a separate `kt_build` folder. After a `make check`, the folder will contain the following:

* `kotlin-native`: Kotlin/Native binaries used to compile the stuff
* `test_target.klib-build`: `cinterop` uses that for building the `klib` with your code
* `test_target.a`: Static library with all of the code that needs to be tested
* `test_target.klib`: Kotlin/Native library (with the static library embedded) with your code
* `testsuite.kexe`: Testsuite executable
