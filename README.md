# `gttp` - simple HTTP server in Groovy

Inspired by http://melix.github.io/blog/2019/03/simple-http-server-graal.html

## Install Groovy

```
$ sdk install groovy 3.0.5
```

## Install GraalVM 20.2 (JDK 11)

```
$ sdk install java 20.2.0.r11-grl

$ sdk use java 20.2.0.r11-grl

$ gu install native-image
```

## Compile `gttp`

```
$ groovyc --compile-static gttp.groovy
```

## Run `gttp` with `native-image-agent` 

```
$ java -agentlib:native-image-agent=config-output-dir=conf/ -cp ".:$GROOVY_HOME/lib/groovy-3.0.5.jar" gttp 
```

## Build native image

```
$ native-image --allow-incomplete-classpath \
  --report-unsupported-elements-at-runtime \
  --initialize-at-build-time \
  --initialize-at-run-time=org.codehaus.groovy.control.XStreamUtils \
  --no-fallback \
  --no-server \
  -H:ConfigurationFileDirectories=conf/ \
  -cp ".:$GROOVY_HOME/lib/groovy-3.0.5.jar" \ 
  gttp
```

## Run `gttp` as a standalone executable file

```
$ ./gttp
Listening at http://localhost:8080/
```

### Change default port

```
$ ./gttp 9000
Listening at http://localhost:9000/
```

### Change default base directory to `/tmp`

```
$ ./gttp 9000 /tmp
Listening at http://localhost:9000/
```

## Alternative: setup environment & build in a docker container

You can also use attached `Dockerfile` to install all required components, compile Groovy script to Java bytecode,
and generate standalone executable file using `native-image` tool.

```
$ sh ./build-with-docker.sh
```