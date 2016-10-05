package org.asteriskjava.fastagi;

/**
 * An AgiChannelFactory creates instances of AgiChannels,
 * that are passed to agi scripts.
 * <p/>
 * An instance of the AgiChannelFactory can be passed to the
 * DefaultAgiServer's constructor.
 *
 * @since 1.0.0
 */
public interface AgiChannelFactory
{
    /**
     * Creates a new AgiChannel.
     *
     * @param request   the request to build the channel for.
     * @param agiWriter the writer.
     * @param agiReader the reader.
     * @return the created channel.
     */
    AgiChannel createAgiChannel(AgiRequest request, AgiWriter agiWriter, AgiReader agiReader);
}
