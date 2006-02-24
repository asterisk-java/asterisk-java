README for Asterisk-Java
========================

The Asterisk-Java package consists of a set of Java classes that allow you to
easily build Java applications that interact with an Asterisk PBX Server.
Asterisk-Java supports both interfaces that Asterisk provides for this
scenario: The FastAGI  protocol and the Manager API.

The FastAGI implementation supports all commands currently available from
Asterisk.

The Manager API implementation supports receiving events from the Asterisk
server (e.g. call progess, registered peers, channel state) and sending actions
to Asterisk (e.g. originate call, agent login/logoff, start/stop voice
recording).

A complete list of the available events and actions is available in the
javadocs.

See docs/tutorial.html for examples.