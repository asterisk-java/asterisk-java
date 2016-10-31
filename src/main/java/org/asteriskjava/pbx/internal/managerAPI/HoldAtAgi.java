package org.asteriskjava.pbx.internal.managerAPI;

import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.TechType;

public class HoldAtAgi implements EndPoint
{

    @Override
    public int compareTo(EndPoint arg0)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isSame(EndPoint rhs)
    {
        return rhs instanceof HoldAtAgi;
    }

    @Override
    public boolean isLocal()
    {
        return false;
    }

    @Override
    public boolean isSIP()
    {
       return false;
    }

    @Override
    public String getFullyQualifiedName()
    {
        return "";
    }

    @Override
    public String getSimpleName()
    {
        return "";
    }

    @Override
    public String getSIPSimpleName()
    {
        return "";
    }

    @Override
    public boolean isUnknown()
    {
        return false;
    }

    @Override
    public TechType getTech()
    {
        return null;
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }
    // Logger logger = LogManager.getLogger();
}
