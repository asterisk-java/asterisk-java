/*
 *  Copyright 2018 CallShaper, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.manager.action;

import org.asteriskjava.manager.ExpectedResponse;
import org.asteriskjava.manager.response.MixMonitorResponse;

/**
 * Start a MixMonitor on the specified channel.
 *
 * Available since Asterisk 11
 *
 * @see org.asteriskjava.manager.response.MixMonitorResponse
 * @see StopMixMonitorAction
 * @see PauseMixMonitorAction
 * @see MixMonitorMuteAction
 */
@ExpectedResponse(MixMonitorResponse.class)
public class MixMonitorAction extends AbstractManagerAction
{
    private static final long serialVersionUID = 1L;
    private String channel;
    private String file;
    private String options;
    private String command;

    /**
     * Creates a new empty MixMonitorAction
     */
    public MixMonitorAction()
    {
        super();
    }

    /**
     * Creates a new MixMonitorAction that starts monitoring the given channel
     * using a default filename.
     *
     * @param channel	the name of the channel to monitor
     */
    public MixMonitorAction(String channel)
    {
        this(channel, null, null, null);
    }

    /**
     * Creates a new MixMonitorAction that starts monitoring the given channel
     * with the specified filename.
     *
     * @param channel	the name of the channel to monitor
     * @param filename	the filename to use for the recording
     */
    public MixMonitorAction(String channel, String filename)
    {
        this(channel, filename, null, null);
    }

    /**
     * Creates a new MixMonitorAction with options that starts monitoring the
     * given channel with the specified filename.
     *
     * @param channel	the name of the channel to monitor
     * @param filename	the filename to use for the recording
     * @param options	the MixMonitor options to use when starting the recording
     */
    public MixMonitorAction(String channel, String filename, String options)
    {
        this(channel, filename, options, null);
    }

    /**
     * Creates a new MixMonitorAction with options that starts monitoring the
     * given channel with the specified filename. Also takes a command to
     * execute when the recording has ended.
     *
     * The {@code command} option has been available since Asterisk 12
     *
     * @param channel	the name of the channel to monitor
     * @param filename	the filename to use for the recording
     * @param options	the MixMonitor options to use when starting the recording
     * @param command	the command to execute when the recording is complete
     */
    public MixMonitorAction(String channel, String filename, String options, String command)
    {
        this.channel = channel;
        this.file = filename;
        this.options = options;
        this.command = command;
    }

    /**
     * Returns the name of this action, i.e. "MixMonitor"
     *
     * @return the name of the AMI action that this class implements
     */
    @Override
    public String getAction()
    {
        return "MixMonitor";
    }

    /**
     * Returns the name of the Asterisk channel to monitor.
     *
     * @return the Asterisk channel name
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the Asterisk channel to the given value.
     *
     * @param channel the Asterisk channel name
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the filename that Asterisk will use when saving the recording.
     *
     * @return the recording filename
     */
    public String getFile()
    {
        return file;
    }

    /**
     * Sets the filename that Asterisk will use when saving the recording.
     *
     * @param filename the recording filename
     */
    public void setFile(String filename)
    {
        this.file = filename;
    }

    /**
     * Gets the options that are passed to MixMonitor.
     *
     * @return the MixMonitor options
     */
    public String getOptions()
    {
        return options;
    }

    /**
     * Sets the options that are passed to MixMonitor. These should be
     * specified in the same format you would use if you were invoking
     * the MixMonitor dialplan application.
     *
     * @param options the MixMonitor options
     */
    public void setOptions(String options)
    {
        this.options = options;
    }

    /**
     * Get the command to be executed when the MixMonitor recording has
     * ended.
     *
     * Available since Asterisk 12
     *
     * @return the command to execute
     */
    public String getCommand()
    {
        return command;
    }

    /**
     * Sets the command to be executed when the MixMonitor recording has
     * ended.
     *
     * Available since Asterisk 12
     *
     * @param command the command to execute
     */
    public void setCommand(String command)
    {
        this.command = command;
    }
}
