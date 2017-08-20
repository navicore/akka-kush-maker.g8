[![Build Status](https://travis-ci.org/navicore/akka-kush-maker.g8.svg?branch=master)](https://travis-ci.org/navicore/akka-kush-maker.g8)

A [g8] Template for AKKA Kush Maker
---

The template creates a working demo of the AKKA actor programming model
using the old joke from the 40s involving Navy fake work.

The generated system can be used as a starter kit for several use cases - 
notably an IOT system that maintains models of the real world and issues
predictions on future states.

## PREREQ

  * sbt >= 13.16

## USAGE

Interactively prompt for details like your project name, package name, and
dependency versions:

```console
sbt new navicore/akka-kush-maker.g8 
```

Or oneshot via cli:

  * install giter8
    * via [g8 setup]
    * or just `brew install giter8`

```console
g8 git@github.com:navicore/akka-kush-maker.g8.git --name=YOUR_PROJECT_NAME  --package=YOUR.PACKAGE.NAME
```

[g8]: http://www.foundweekends.org/giter8/
[g8 setup]: http://www.foundweekends.org/giter8/setup.html 

