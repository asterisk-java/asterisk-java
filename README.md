# Asterisk-Java

## Introduction

The Asterisk-Java package consists of a set of Java classes that allow you to
easily build Java applications that interact with
an [Asterisk PBX Server](https://www.asterisk.org/).
Asterisk-Java supports both interfaces that Asterisk provides for this
scenario:
The [FastAGI](https://docs.asterisk.org/Latest_API/API_Documentation/Dialplan_Applications/AGI/)
protocol and
the [Manager API](https://docs.asterisk.org/Configuration/Interfaces/Asterisk-Manager-Interface-AMI/The-Asterisk-Manager-TCP-IP-API).

You can find the Java docs for the latest version here:
[Javadoc](https://javadoc.io/doc/org.asteriskjava/asterisk-java/latest)

### FastAGI

FastAGI lets you create a service that manages a call in a similar way to how
a webserver handles an HTTP request. FastAGI can be (and should be) used to
replace dialplan. FastAGI is thousands of times faster than dialplan, easier to
debug and lets you do call control in a language that you are familiar with.

The FastAGI implementation supports all commands currently available from
Asterisk.

### Manager API

The Manager API implementation supports receiving events from the Asterisk
server (e.g. call progress, registered peers, channel state) and sending actions
to Asterisk (e.g. originate call, agent login/logoff, start/stop voice
recording).
If you like, the Manager API allows you to start and manipulate calls. A
complete list of the available events and actions is available in the
[documentation](https://javadoc.io/doc/org.asteriskjava/asterisk-java/latest).

### Activities

Activities are new to asterisk-java 2.0. The aim of Activities is to provide a
high level interface to interactions with Asterisk. Whist Activities use both
FastAGI and the Manager API you would normally consider Activities as a
replacement for the Manager API.

Activities provide a simple and consistent method of interaction with Asterisk
without having to worry about issues such as connection management and without
having to understand the intricacies of Asterisk Manager Actions and Events.

[Getting Started](https://github.com/asterisk-java/asterisk-java/wiki/Getting-Started)

[Tutorial](https://github.com/asterisk-java/asterisk-java/wiki/Tutorial)

[Activities](https://github.com/asterisk-java/asterisk-java/wiki/Activities)

[Examples](https://github.com/asterisk-java/asterisk-java/wiki/Examples)

---

## Getting Asterisk-Java

Asterisk-Java is available
[on GitHub](https://github.com/asterisk-java/asterisk-java/releases).

### Maven

Asterisk-Java 3.x (Java 1.8 and Asterisk Version 10 thru 23) (master)

```xml

<dependency>
    <groupId>org.asteriskjava</groupId>
    <artifactId>asterisk-java</artifactId>
    <version>3.39.0</version>
</dependency>
```

### Gradle

```text
implementation group: 'org.asteriskjava', name: 'asterisk-java', version: '3.39.0'
```

### Installation from source

```text
git clone https://github.com/asterisk-java/asterisk-java.git
cd asterisk-java
mvn install
```

After the build is complete, the jar will then be built as
`target/asterisk-java.jar` in the `asterisk-java` directory.

## Example

The file `examples/ExampleCallIn.java` will answer the call and playback the
audio file 'tt-monkeys'.

```java
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

/* Example incoming call handler. Answer call, speak message */
public class ExampleCallIn extends BaseAgiScript {
    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        answer();
        exec("Playback", "tt-monkeys");
        hangup();
    }
}
```

The file `examples/fastagi.properties` maps your Asterisk dialplan context to
the class you would like to invoke above.

```text
callin.agi = ExampleCallIn
```

To compile and run:

```text
javac -cp asterisk-java.jar ExampleCallIn.java
java -cp asterisk-java.jar org.asteriskjava.fastagi.DefaultAgiServer
```

## System Requirements

Asterisk-Java needs a Java Virtual Machine of at least version
1.8 ([Java SE 8.0](https://docs.oracle.com/javase/8/)).
If you want to build the jar from source, you will also
need [Maven](https://maven.apache.org/).

## Legal

Asterisk-Java is subject to the terms detailed in the license agreement
accompanying it.
