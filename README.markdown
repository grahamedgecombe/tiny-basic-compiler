Tiny BASIC Compiler
===================

Introduction
------------

A simple [Tiny BASIC][tinybasic] compiler that targets x86-64 Linux machines.

Usage
-----

The compiler can be built with Java 8 and [Apache Maven][maven]. The following
command will build it and run the unit tests:

    mvn compile test

You also need [NASM][nasm] and [GCC][gcc], which are used for assembling the
output of the compiler and linking it with the standard C library.

Example programs can be found in the `examples` folder. They can be compiled and
executed like so:

    $ cat examples/squares.tb
    10 LET X = 1
    20 PRINT X * X
    30 LET X = X + 1
    40 IF X > 10 THEN END
    50 GOTO 20
    $ ./tinybasic examples/squares.tb 
    $ ./examples/squares 
    1
    4
    9
    16
    25
    36
    49
    64
    81
    100
    $ 

License
-------

This project is available under the terms of the [ISC license][isc], which is
similar to the 2-clause BSD license. See the `LICENSE` file for the copyright
information and licensing terms.

[maven]: https://maven.apache.org/
[tinybasic]: https://en.wikipedia.org/wiki/Tiny_BASIC
[isc]: https://www.isc.org/software/license/
[gcc]: http://gcc.gnu.org/
[nasm]: http://www.nasm.us/
