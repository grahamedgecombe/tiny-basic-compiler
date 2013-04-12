Tiny BASIC Compiler
===================

Introduction
------------

A simple and dumb [Tiny BASIC][tinybasic] compiler that targets x86-64 Linux
machines.

Usage
-----

The compiler can be built with Java 8 and [Apache Maven][maven]. The following
command will build it and run the unit tests:

    mvn compile test

Example programs can be found in the `examples` folder. To build and run one of
them, run the following commands:

    ./tinybasic examples/hello.tb
    ./examples/hello

If this works, you should see "Hello, world!" printed to the terminal.

License
-------

This project is available under the terms of the [ISC license][isc], which is
similar to the 2-clause BSD license. See the `LICENSE` file for the copyright
information and licensing terms.

[maven]: https://maven.apache.org/
[tinybasic]: https://en.wikipedia.org/wiki/Tiny_BASIC
[isc]: https://www.isc.org/software/license/
