package org.asteriskjava.pbx;

import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class PBXFactory
{

    public static PBX getActivePBX()
    {
        return AsteriskPBX.SELF;

    }

}
