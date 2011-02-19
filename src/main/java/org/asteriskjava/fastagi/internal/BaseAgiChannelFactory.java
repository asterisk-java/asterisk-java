package org.asteriskjava.fastagi.internal;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.util.SocketConnectionFacade;

/**
 * This is a possible base class for AgiChannelFactories. It overrides one
 * of the needed Create methods by delegating the request to the
 * implementation, that must be provided by the child. 
 */
public abstract class BaseAgiChannelFactory implements AgiChannelFactory {

	@Override
	public AgiChannel Create(AgiRequest request, SocketConnectionFacade socket) {
		return this.Create(request,new FastAgiWriter(socket),new FastAgiReader(socket));
	}

}
