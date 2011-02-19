package org.asteriskjava.fastagi.internal;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.util.SocketConnectionFacade;

/**
 * An AgiChannelFactory creates instances of AgiChannels, 
 * that are passed to agi scripts.
 * 
 * An instance of the AgiChannelFactory can be passed to the 
 * DefaultAgiServer's constructor. 
 */
public interface AgiChannelFactory {

	AgiChannel Create(AgiRequest request, SocketConnectionFacade socket);
	
	AgiChannel Create(AgiRequest request, AgiWriter agiWriter, AgiReader agiReader);
}
