package org.asteriskjava.manager.action;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Send a custom SIP message to the specified peer. Available since Asterisk 11
 *
 * @author Michael Will
 * @version $Id$
 * @since 1.0.0
 */
public class MessageSendAction extends AbstractManagerAction
{

    private static final long serialVersionUID = -6554282385166411723L;
    private String to;
    private String from;
    private String body;
    private String base64body;
    private Map<String, String> variables;

    @Override
    public String getAction()
    {
        return "MessageSend";
    }

    /**
     * Returns the variables to set on the originated call.
     *
     * @return a Map containing the variable names as key and their values as
     * value.
     * @since 1.0.0
     */
    public Map<String, String> getVariables()
    {
        return variables;
    }

    /**
     * Sets an variable on the originated call.
     *
     * @param name the name of the variable to set.
     * @param value the value of the variable to set.
     * @since 1.0.0
     */
    public void setVariable(String name, String value)
    {
        if (variables == null)
        {
            variables = new LinkedHashMap<>();
        }

        variables.put(name, value);
    }

    /**
     * Retrives destination of message
     * 
     * @return String in format like sip:userid
     */
    public String getTo()
    {
        return to;
    }

    /**
     * Set destination of message
     * 
     * @param to String in format like sip:userid
     */
    public void setTo(String to)
    {
        this.to = to;
    }

    /**
     * get From String
     * 
     * @return String in form like  sip:asterisk@server-ip
     */
    public String getFrom()
    {
        return from;
    }

    /**
     * set From String 
     * @param String from in form like  sip:asterisk@server-ip
     */
    public void setFrom(String from)
    {
        this.from = from;
    }

    /**
     * Content of the message in plain text ascii
     * Please consider to use getBase64Body instead to avoid encoding problems
     * 
     * @return String with Message content
     */
    public String getBody()
    {
        return body;
    }

    /**
     * Sets the content of the message
     * Please consider to use setBase64Body instead to avoid encoding problems
     * 
     * @param body String with plain ascii content
     */
    public void setBody(String body)
    {
        this.body = body;
    }

    /**
     * Retrieves content encoded in base64
     * 
     * @code new String(Base64.decode(getBase64body(), Formatting (for ex "UTF-8"))
     * @return Base64 encoded String
     */
    public String getBase64body()
    {
        return base64body;
    }

    /**
     * Sets the content of the message body encode in base64
     * 
     * {@code    MessageSendAction sipSendMessage = new MessageSendAction();
     *           sipSendMessage.setTo("sip:phoneuserid");
     *           sipSendMessage.setVariable("Content-Type", "text/plain");
     *           sipSendMessage.setVariable("P-hint", "usrloc applied");
     * 
     *           sipSendMessage.setActionId(UUID.randomUUID().toString());
     *           sipSendMessage.setBase64body(new String(Base64.encodeBase64(message.getBytes("UTF-8")), "UTF-8"));
     * }
     * @param base64body 
     */
    public void setBase64body(String base64body)
    {
        this.base64body = base64body;
    }

}
