#!/bin/bash
echo "Compiling Application"
/cygdrive/c/Program\ Files\ \(x86\)/typesafe-stack/bin/scalac -classpath akka-actor-1.2.jar src/*.scala
echo "Running Application"
/cygdrive/c/Program\ Files\ \(x86\)/typesafe-stack/bin/scala -classpath akka-actor-1.2.jar Main
