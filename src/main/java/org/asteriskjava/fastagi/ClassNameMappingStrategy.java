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

import java.util.HashMap;
import java.util.Map;

/**
 * A MappingStrategy that determines the AGIScript based on the fully
 * qualified class name given in the AGI URL.<br>
 * To use this ClassNameMappingStrategy the calls to your AGIScript in
 * your dialplan should look like this:
 * <pre>
 * exten => 123,1,AGI(agi://your.server.com/com.example.agi.MyScript)
 * </pre>
 * Where com.example.agi.MyScript is the fully qualified name of your
 * AGIScript.
 * 
 * @author srt
 * @version $Id: ClassNameMappingStrategy.java,v 1.2 2006/01/12 11:49:33 srt Exp $
 */
public class ClassNameMappingStrategy extends AbstractMappingStrategy
{
    private Map mappings;

    /**
     * Creates a new ClassNameMappingStrategy.
     */
    public ClassNameMappingStrategy()
    {
        this.mappings = new HashMap();
    }

    public AGIScript determineScript(AGIRequest request)
    {
        if (mappings.get(request.getScript()) != null)
        {
            return (AGIScript) mappings.get(request.getScript());
        }

        AGIScript script;
        script = createAGIScriptInstance(request.getScript());
        if (script == null)
        {
            return null;
        }

        mappings.put(request.getScript(), script);
        return script;
    }
}
