AKKA_HOME='c:/Program Files (x86)/typesafe-stack'
AKKA_JARS="${AKKA_HOME}"/config\;"${AKKA_HOME}"/lib/scala-library.jar\;"${AKKA_HOME}"/lib/akka/multiverse-alpha-0.6.2.jar\;"${AKKA_HOME}"/lib/akka/akka-stm-1.2.jar\;"${AKKA_HOME}"/lib/akka/akka-actor-1.2.jar
CLASSPATH=`echo "${CLASSPATH};${AKKA_JARS}" | sed 's/^;/.;/'`
export CLASSPATH
