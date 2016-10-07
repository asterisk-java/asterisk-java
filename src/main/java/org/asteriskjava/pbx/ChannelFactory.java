package org.asteriskjava.pbx;

import org.apache.log4j.Logger;

/**
 * Currently this factories only purposes is to serve up globally unique channel
 * id's which every new channel must be assigned.
 * 
 * @author bsutton
 * 
 */
public class ChannelFactory
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ChannelFactory.class);

	private static long nextChannelId = 0;

	public static synchronized long getNextChannelId()
	{
		return ++ChannelFactory.nextChannelId;
	}
}
