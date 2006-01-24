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
package org.asteriskjava.fastagi;

import org.asteriskjava.fastagi.AGIChannel;
import org.asteriskjava.fastagi.AGIRequest;
import org.asteriskjava.fastagi.AGIScript;

/**
 * Test script for use with the ResourceBundleMappingStrategyTest.
 * 
 * @author srt
 * @version $Id: HelloAGIScript.java,v 1.2 2005/03/10 23:57:07 srt Exp $
 */
public class HelloAGIScript implements AGIScript
{
    public HelloAGIScript()
    {

    }

    public void service(AGIRequest request, AGIChannel channel)
    {
        return;
    }
}
