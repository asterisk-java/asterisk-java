package org.asteriskjava.pbx.internal.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Set;

import org.asteriskjava.pbx.asterisk.wrap.actions.ManagerAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ResponseEvent;
import org.asteriskjava.pbx.asterisk.wrap.response.CommandResponse;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerError;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.asteriskjava.util.ReflectionUtil;

/**
 * This class maps asterisk-java events to our internal events that use iChannel
 * rather than raw channel names.
 * 
 * @author bsutton
 */
@SuppressWarnings({"unchecked"})
public class CoherentEventFactory
{
    private static final Log logger = LogFactory.getLog(CoherentEventFactory.class);

    // Events
    static Hashtable<Class<org.asteriskjava.manager.event.ManagerEvent>, Class<ManagerEvent>> mapEvents = new Hashtable<>();

    // Response
    static Hashtable<Class< ? extends org.asteriskjava.manager.event.ResponseEvent>, Class< ? extends ResponseEvent>> mapResponses = new Hashtable<>();

    // static initialiser
    static
    {

        Set<Class<ManagerEvent>> knownClasses = ReflectionUtil.loadClasses("org.asteriskjava.pbx.asterisk.wrap.events",
                ManagerEvent.class);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (Class< ? > known : knownClasses)
        {
            Class< ? > clazz = null;
            try
            {
                clazz = classLoader.loadClass("org.asteriskjava.manager.event" + "." + known.getSimpleName());

                if (!Modifier.isAbstract(clazz.getModifiers()))
                {
                    if (known.getConstructor(clazz) != null)
                    {
                        if (ResponseEvent.class.isAssignableFrom(known))
                        {
                            CoherentEventFactory.mapResponses.put(
                                    (Class<org.asteriskjava.manager.event.ResponseEvent>) clazz,
                                    (Class<ResponseEvent>) known);
                            logger.info("Response Event Added " + clazz + " --> " + known);

                        }
                        else
                        {
                            CoherentEventFactory.mapEvents.put((Class<org.asteriskjava.manager.event.ManagerEvent>) clazz,
                                    (Class<ManagerEvent>) known);
                            logger.info("Manager Event Added " + clazz + " --> " + known);
                        }
                    }
                    else
                    {
                        logger.warn("Skipping class " + clazz + " it doesn't have a public constructor with one arg of type "
                                + known);
                    }
                }
                else
                {
                    logger.info("Skipping abstract class " + clazz);
                }
            }
            catch (ClassNotFoundException e)
            {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
            catch (NoSuchMethodException e)
            {
                logger.error(clazz.getCanonicalName() + " doesn't have an appropriate event constructor");
            }
            catch (SecurityException e)
            {
                logger.error(e, e);
            }
        }

        CoherentEventFactory.mapResponses.put(org.asteriskjava.manager.event.ResponseEvent.class, ResponseEvent.class);

    }

    public static Class< ? extends ManagerEvent> getShadowEvent(org.asteriskjava.manager.event.ManagerEvent event)
    {
        Class< ? extends ManagerEvent> result = CoherentEventFactory.mapEvents.get(event.getClass());
        if (result == null)
        {
            Class< ? extends ResponseEvent> response = CoherentEventFactory.mapResponses.get(event.getClass());
            result = response;
        }

        return result;

    }

    public static ManagerEvent build(final org.asteriskjava.manager.event.ManagerEvent event)
    {
        ManagerEvent iEvent = null;

        Class< ? extends ManagerEvent> target = null;

        if (event instanceof org.asteriskjava.manager.event.ResponseEvent)
            target = CoherentEventFactory.mapResponses.get(event.getClass());
        else
            target = CoherentEventFactory.mapEvents.get(event.getClass());

        if (target == null)
        {
            logger.warn("The given event " + event.getClass().getName() + " is not supported "); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {

            try
            {
                iEvent = target.getDeclaredConstructor(event.getClass()).newInstance(event);
            }
            catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e)
            {
                CoherentEventFactory.logger.error(e, e);

            }
        }
        return iEvent;
    }

    public static ResponseEvent build(org.asteriskjava.manager.event.ResponseEvent event)
    {
        ResponseEvent response = null;

        final Class< ? extends ResponseEvent> target = CoherentEventFactory.mapResponses.get(event.getClass());

        if (target == null)
        {
            logger.warn("The given event " + event.getClass().getName() + " is not supported "); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {

            try
            {
                final Constructor< ? extends ResponseEvent> declaredConstructor = target
                        .getDeclaredConstructor(event.getClass());
                response = declaredConstructor.newInstance(event);
            }
            catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e)
            {
                CoherentEventFactory.logger.error(e, e);

            }
        }
        return response;
    }

    public static ManagerResponse build(org.asteriskjava.manager.response.ManagerResponse response)
    {
        ManagerResponse result;
        if (response instanceof org.asteriskjava.manager.response.CommandResponse)
            result = new CommandResponse((org.asteriskjava.manager.response.CommandResponse) response);
        else if (response instanceof org.asteriskjava.manager.response.ManagerError)
            result = new ManagerError((org.asteriskjava.manager.response.ManagerError) response);
        else
            result = new ManagerResponse(response);
        return result;

    }

    public static org.asteriskjava.manager.action.ManagerAction build(ManagerAction action)
    {
        org.asteriskjava.manager.action.ManagerAction result = null;

        // final Class<? extends org.asteriskjava.manager.action.ManagerAction>
        // target = CoherentEventFactory.mapActions.get(action.getClass());

        if (logger.isDebugEnabled())
            logger.debug("Action " + action); //$NON-NLS-1$

        // if (target == null)
        // {
        // logger.warn("The given action " + action.getClass().getName() + " is
        // not supported "); //$NON-NLS-1$ //$NON-NLS-2$
        // }
        // else
        {
            result = action.getAJAction();
        }
        return result;

    }

}
