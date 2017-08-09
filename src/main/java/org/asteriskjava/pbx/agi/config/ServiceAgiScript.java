package org.asteriskjava.pbx.agi.config;

import org.asteriskjava.fastagi.AgiScript;

public interface ServiceAgiScript extends AgiScript
{

    String getScriptName();

    String[] getParameters();

}
