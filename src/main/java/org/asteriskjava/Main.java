/*
 * Copyright  2004-2005 Stefan Reuter
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
package org.asteriskjava;

import org.asteriskjava.fastagi.AGIServer;
import org.asteriskjava.fastagi.DefaultAGIServer;

/**
 * Starts the DefaultAGIServer.
 * 
 * @author srt
 * @version $Id: Main.java,v 1.1 2005/03/12 09:57:32 srt Exp $
 */
public class Main
{
    public static void main(String[] args) throws Exception
    {
        AGIServer agiServer;
        
        agiServer = new DefaultAGIServer();
        agiServer.startup();
    }
}
