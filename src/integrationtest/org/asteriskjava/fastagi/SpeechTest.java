package org.asteriskjava.fastagi;


import org.apache.log4j.Logger;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.fastagi.reply.AgiReply;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

public class SpeechTest extends BaseAgiScript
{
    private Logger logger = Logger.getLogger(SpeechTest.class);

    public void service(AgiRequest request, AgiChannel channel) throws AgiException
    {
        try
        {
            answer();
            speechCreate();

            while(true)
            {
                doService(request, channel);
            }
        }
        finally
        {
            try
            {
                speechDestroy();
            }
            catch (Exception e)
            {
                // swallow
            }
        }
    }

    public void doService(AgiRequest request, AgiChannel channel) throws AgiException
    {
        SpeechRecognitionResult result;

        speechLoadGrammar("digits", "/etc/asterisk/grammars/test.gram");
        speechActivateGrammar("digits");
        result = speechRecognize("tt-allbusy", 10);
        AgiReply reply = channel.getLastReply();
        System.out.println(reply);
        System.out.println(reply.getFirstLine());
        System.out.println(result);

        String text = result.getText();
        if (text != null)
        {
            System.out.println("Saying " + text);
            sayDigits(text);
        }
    }

    public static void main(String[] args) throws IOException
    {
        AgiServerThread agiServerThread = new AgiServerThread();
        agiServerThread.setAgiServer(new DefaultAgiServer());
        agiServerThread.setDaemon(false);
        agiServerThread.startup();
    }
}