package org.asteriskjava.pbx.agi.config;

import java.util.List;

public interface AgiConfiguration
{

    List<Class< ? extends ServiceAgiScript>> getAgiHandlers();

}
