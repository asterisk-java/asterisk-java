/*
 * Copyright 2004-2022 Asterisk-Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.fastagi.command;

/**
 * AGI Command: <b>SEND IMAGE</b>
 * <p>
 * Sends the given image on a channel.<br>
 * Most channels do not support the transmission of images.<br>
 * Returns 0 if image is sent, or if the channel does not support image transmission. Returns -1 only on error/hangup.<br>
 * Image names should not include extensions.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_send+image">AGI Command SEND IMAGE (Asterisk 18)</a>
 *
 * @author srt
 */
public class SendImageCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3904959746380281145L;

    /**
     * The name of the image to send.
     */
    private String image;

    /**
     * Creates a new SendImageCommand.
     *
     * @param image the image to send, should not include extension.
     */
    public SendImageCommand(String image) {
        super();
        this.image = image;
    }

    /**
     * Returns the image to send.
     *
     * @return the image to send.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image to send.
     *
     * @param image the image to send, should not include extension.
     */
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String buildCommand() {
        return "SEND IMAGE " + escapeAndQuote(image);
    }
}
