/*
 *  Copyright 2004-2006 Stefan Reuter
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
package org.asteriskjava.fastagi.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExecCommandTest
{
    private ExecCommand execCommand;

    @Test
    public void testDefault()
    {
        execCommand = new ExecCommand("DIAL");
        assertEquals("EXEC \"DIAL\" \"\"", execCommand.buildCommand());
    }

    @Test
    public void testWithSingleOption()
    {
        execCommand = new ExecCommand("DIAL", "SIP/1234");
        assertEquals("EXEC \"DIAL\" \"SIP/1234\"", execCommand.buildCommand());
    }

    @Test
    public void testWithMultipleOptionsSingleParameterPipeSeparated()
    {
        execCommand = new ExecCommand("DIAL", "SIP/1234|30");
        assertEquals("EXEC \"DIAL\" \"SIP/1234|30\"", execCommand.buildCommand());
    }

    @Test
    public void testWithMultipleOptionsSingleParameterCommaSeparated()
    {
        execCommand = new ExecCommand("DIAL", "SIP/1234,30");
        assertEquals("EXEC \"DIAL\" \"SIP/1234,30\"", execCommand.buildCommand());
    }

    @Test
    public void testWithMultipleOptionsMultipleParameters()
    {
        execCommand = new ExecCommand("DIAL", "SIP/1234", "30");
        assertEquals("EXEC \"DIAL\" \"SIP/1234,30\"", execCommand.buildCommand());
    }
}
