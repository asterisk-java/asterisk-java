README for Asterisk-Java
========================

INTRODUCTION
------------

The Asterisk-Java package consists of a set of Java classes that allow you to easily build Java applications that interact with an [Asterisk PBX Server](http://asterisk.org). Asterisk-Java supports both interfaces that Asterisk provides for this scenario: The [FastAGI](https://wiki.asterisk.org/wiki/display/AST/Application_AGI) protocol and the [Manager API](https://wiki.asterisk.org/wiki/display/AST/The+Asterisk+Manager+TCP+IP+API).

You can find the Java docs for the lastest version here:
[JavaDoc](https://javadoc.io/doc/org.asteriskjava/asterisk-java/2.0.2)

FastAGI
-------
FastAGI lets you create a service that manages a call in a similar way to what a webserver handles a http request. FastAGI can be (and should be) used to replace dialplan. FastAGI is thousands of times faster than dialplan, easier to debug and lets you do call control in a language that you are familiar with.

The FastAGI implementation supports all commands currently available from Asterisk.

Manager API
-----------
The Manager API implementation supports receiving events from the Asterisk server (e.g. call progess, registered peers, channel state) and sending actions to Asterisk (e.g. originate call, agent login/logoff, start/stop voice recording).
If you like, the Manager API allows you start and manipulate calls.
A complete list of the available events and actions is available in the javadocs.

Activities
----------
Activities are new to asterisk-java 2.0. The aim of Activities is to provide a high level interface to interactions with Asterisk. Whist Activities use both FastAGI and the Manager API you would normally consider Activities as a replacement for the Manager API.

Activities provide a simple and consistent method of interaction with Asterisk without having to worry about issues such as connection management and without having to understand the intricacies of Asterisk Manager Actions and Events.

[Getting Started](https://github.com/asterisk-java/asterisk-java/wiki/Getting-Started)

[Tutorial](https://github.com/asterisk-java/asterisk-java/wiki/Tutorial)

[Activities](https://github.com/asterisk-java/asterisk-java/wiki/Activities)

[Examples](https://github.com/asterisk-java/asterisk-java/wiki/Examples)


GETTING ASTERISK-JAVA
---------------------

Asterisk-Java is available from [here](https://github.com/asterisk-java/asterisk-java/releases)

Maven Dependency
----------------
Asterisk-Java 2.0.3

	<repositories>
		<repository>
			<id>asterisk-java-mvn-repo</id>
            <url>https://raw.githubusercontent.com/asterisk-java/asterisk-java/mvn-repo</url>
 			<releases>
				<enabled>true</enabled>
			</releases>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</repository>
	</repositories>

	<dependency>
	  <groupId>org.asteriskjava</groupId>
	  <artifactId>asterisk-java</artifactId>
	  <version>2.0.3</version>
	</dependency>


Asterisk-Java 2.0

	<dependency>
	  <groupId>org.asteriskjava</groupId>
	  <artifactId>asterisk-java</artifactId>
	  <version>2.0.2</version>
	</dependency>

Asterisk-Java 1.0

	<dependency>
	  <groupId>org.asteriskjava</groupId>
	  <artifactId>asterisk-java</artifactId>
	  <version>1.0.0-final</version>
	</dependency>

INSTALLATION FROM SOURCE
------------------------

	git clone https://github.com/asterisk-java/asterisk-java.git
	cd asterisk-java
	mvn install

After the build is complete, the jar will then be built as target/asterisk-java.jar in the asterisk-java directory.

EXAMPLE
-------

The file 'examples/ExampleCallIn.java' will answer the call and playback the audio file 'tt-monkeys'.

	import org.asteriskjava.fastagi.AgiChannel;
	import org.asteriskjava.fastagi.AgiException;
	import org.asteriskjava.fastagi.AgiRequest;
	import org.asteriskjava.fastagi.BaseAgiScript;
	/* Example incoming call handler
	Answer call, speak message */
	public class ExampleCallIn extends BaseAgiScript {
	  public void service(AgiRequest request, AgiChannel channel) throws AgiException {
	    answer();
	    exec("Playback", "tt-monkeys"); 
	    hangup();
	  }
	}

The file 'examples/fastagi.properties' maps your Asterisk diaplan context to the class you would like to invoke above.

	callin.agi = ExampleCallIn

To compile and run do:

	javac -cp asterisk-java.jar ExampleCallIn.java
	java -cp asterisk-java.jar org.asteriskjava.fastagi.DefaultAgiServer

SYSTEM REQUIREMENTS
-------------------

Asterisk-Java needs a Java Virtual Machine of at least version 1.7 ([Java SE 7.0](http://www.oracle.com/technetwork/java/javase/downloads/index.html)). If you want to build the jar from source, you will
also need [Maven](http://maven.apache.org/).

LEGAL
-----

Asterisk-Java is subject to the terms detailed in the license agreement accompanying it.
