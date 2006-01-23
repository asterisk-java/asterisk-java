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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeMappingStrategy implements MappingStrategy
{
    private List strategies;

    public CompositeMappingStrategy()
    {

    }

    public CompositeMappingStrategy(MappingStrategy strategy1,
            MappingStrategy strategy2)
    {
        strategies = new ArrayList();
        strategies.add(strategy1);
        strategies.add(strategy2);
    }

    public void addStrategy(MappingStrategy strategy)
    {
        if (strategies == null)
        {
            strategies = new ArrayList();
        }
        strategies.add(strategy);
    }

    public void setStrategies(List strategies)
    {
        this.strategies = strategies;
    }

    public AGIScript determineScript(AGIRequest request)
    {
        if (strategies == null)
        {
            return null;
        }

        Iterator i = strategies.iterator();
        while (i.hasNext())
        {
            MappingStrategy strategy = (MappingStrategy) i.next();
            AGIScript script = strategy.determineScript(request);
            if (script != null)
            {
                return script;
            }
        }

        return null;
    }

}
