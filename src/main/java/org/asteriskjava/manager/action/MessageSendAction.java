package org.asteriskjava.manager.action;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Send a custom SIP message to the specified peer.
 * Available since Asterisk 11
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
  public String getAction ()
  {
    return "MessageSend";
  }
  
  /**
   * Returns the variables to set on the originated call.
   *
   * @return a Map containing the variable names as key and their
   *         values as value.
   * @since 1.0.0
   */
  public Map<String, String> getVariables()
  {
      return variables;
  }

  /**
   * Sets an variable on the originated call.
   *
   * @param name  the name of the variable to set.
   * @param value the value of the variable to set.
   * @since 1.0.0
   */
  public void setVariable(String name, String value)
  {
      if (variables == null)
      {
          variables = new LinkedHashMap<String, String>();
      }

      variables.put(name, value);
  }
  
  public String getTo ()
  {
    return to;
  }

  public void setTo (String to)
  {
    this.to = to;
  }

  public String getFrom ()
  {
    return from;
  }

  public void setFrom (String from)
  {
    this.from = from;
  }

  public String getBody ()
  {
    return body;
  }

  public void setBody (String body)
  {
    this.body = body;
  }

  public String getBase64body ()
  {
    return base64body;
  }

  public void setBase64body (String base64body)
  {
    this.base64body = base64body;
  }

}
