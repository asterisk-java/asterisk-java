package org.asteriskjava.manager.internal;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.util.ReflectionUtil;
import org.asteriskjava.util.AstUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import java.util.Map;
import java.util.Set;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

/**
 * Abstract base class for reflection based builders. 
 */
abstract class AbstractBuilder
{
    protected final Log logger = LogFactory.getLog(getClass());

    @SuppressWarnings("unchecked")
    protected void setAttributes(Object target, Map<String, String> attributes, Set<String> ignoredAttributes)
    {
        Map<String, Method> setters;

        setters = ReflectionUtil.getSetters(target.getClass());
        for (Map.Entry<String, String> entry : attributes.entrySet())
        {
            Object value;
            final Class dataType;
            final Method setter;

            if (ignoredAttributes != null && ignoredAttributes.contains(entry.getKey()))
            {
                continue;
            }

            /*
             * The source property needs special handling as it is already
             * defined in java.util.EventObject (the base class of
             * ManagerEvent), so we have to translate it.
             */
            if ("source".equals(entry.getKey()))
            {
                setter = setters.get("src");
            }
            else
            {
                setter = setters.get(ReflectionUtil.stripIllegalCharacters(entry.getKey()));
            }

            // it seems silly to warn if it's a user event -- maybe it was intentional
            if (setter == null && !(target instanceof UserEvent))
            {
                logger.warn("Unable to set property '" + entry.getKey() + "' to '" + entry.getValue() + "' on "
                        + target.getClass().getName() + ": no setter. Please report at http://jira.reucon.org/browse/AJ");
            }

            if (setter == null)
            {
                continue;
            }

            dataType = setter.getParameterTypes()[0];

            if (dataType == Boolean.class)
            {
                value = AstUtil.isTrue(entry.getValue());
            }
            else if (dataType.isAssignableFrom(String.class))
            {
                value = entry.getValue();
                if (AstUtil.isNull((String) value))
                {
                    value = null;
                }
            }
            else
            {
                try
                {
                    Constructor constructor = dataType.getConstructor(new Class[]{String.class});
                    value = constructor.newInstance(entry.getValue());
                }
                catch (Exception e)
                {
                    logger.error("Unable to convert value '" + entry.getValue() + "' of property '" + entry.getKey() + "' on "
                            + target.getClass().getName() + " to required type " + dataType, e);
                    continue;
                }
            }

            try
            {
                setter.invoke(target, value);
            }
            catch (Exception e)
            {
                logger.error("Unable to set property '" + entry.getKey() + "' to '" + entry.getValue() + "' on "
                        + target.getClass().getName(), e);
            }
        }
    }
}
