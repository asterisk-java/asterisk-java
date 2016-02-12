README for Asterisk-Java
========================

INTRODUCTION
------------

The Asterisk-Java package consists of a set of Java classes that allow you to easily build Java applications that interact with an [Asterisk PBX Server](http://asterisk.org). Asterisk-Java supports both interfaces that Asterisk provides for this scenario: The [FastAGI](https://wiki.asterisk.org/wiki/display/AST/Application_AGI) protocol and the [Manager API](https://wiki.asterisk.org/wiki/display/AST/The+Asterisk+Manager+TCP+IP+API).

The FastAGI implementation supports all commands currently available from Asterisk.

The Manager API implementation supports receiving events from the Asterisk server (e.g. call progess, registered peers, channel state) and sending actions to Asterisk (e.g. originate call, agent login/logoff, start/stop voice recording).

A complete list of the available events and actions is available in the javadocs.

See docs/tutorial.html for examples.

GETTING ASTERISK-JAVA
---------------------

Asterisk-Java is available from [here](https://github.com/asterisk-java/asterisk-java/releases)

Maven Dependency
----------------

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

The file 'examples/fastagi-mapping.properties' maps your Asterisk diaplan context to the class you would like to invoke above.

	callin.agi = ExampleCallIn

To compile and run do:

	javac -cp asterisk-java.jar ExampleCallIn.java
	java -cp asterisk-java.jar org.asteriskjavafastagi.DefaultAgiServer

SYSTEM REQUIREMENTS
-------------------

Asterisk-Java needs a Java Virtual Machine of at least version 1.7 ([Java SE 7.0](http://www.oracle.com/technetwork/java/javase/downloads/index.html)). If you want to build the jar from source, you will
also need [Maven](http://maven.apache.org/).

LEGAL
-----

Asterisk-Java is subject to the terms detailed in the license agreement accompanying it.