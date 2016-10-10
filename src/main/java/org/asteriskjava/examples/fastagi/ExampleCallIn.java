package org.asteriskjava.examples.fastagi;

/* Cloudvox - answer and control a phone call with Java
Place and receive phone calls via open API: http://cloudvox.com/
Learn about call scripting, Asterisk/AGI, voice apps: http://help.cloudvox.com/ 
Added to the project and modified by Tropo: http://tropo.com */
 
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