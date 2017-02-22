package org.asteriskjava.fastagi.internal;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiChannelFactory;
import org.asteriskjava.fastagi.AgiReader;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiWriter;

/**
 * If no other factory  is passed to DefaultAgiServer's constructor,
 * an instance of this one will be used. The DefaultAgiChannelFactory
 * creates AgiChannelImpl instances, that are passed to the agi scripts.
 */
public class DefaultAgiChannelFactory implements AgiChannelFactory
{
    @Override
    public AgiChannel createAgiChannel(AgiRequest request, AgiWriter agiWriter, AgiReader agiReader)
    {
        return new AgiChannelImpl(request, agiWriter, agiReader);
    }
}
