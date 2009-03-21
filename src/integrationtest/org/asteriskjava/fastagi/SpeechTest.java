package org.asteriskjava.fastagi;


import org.asteriskjava.fastagi.reply.AgiReply;

import java.io.IOException;

public class SpeechTest extends BaseAgiScript
{
    public void service(AgiRequest request, AgiChannel channel) throws AgiException
    {
        try
        {
            answer();
            speechCreate();

            while (true)
            {
                doService(request, channel);
            }
        }
        finally
        {
            try
            {
                // make sure to destroy the speech object otherwise licenses might leak.
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
        final String grammarLabel = "test";
        final String grammarPath = "/etc/asterisk/grammars/test.gram";

        speechLoadGrammar(grammarLabel, grammarPath);
        speechActivateGrammar(grammarLabel);
        SpeechRecognitionResult result = speechRecognize("tt-allbusy", 10);
        AgiReply reply = channel.getLastReply();
        speechDeactivateGrammar(grammarLabel);

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