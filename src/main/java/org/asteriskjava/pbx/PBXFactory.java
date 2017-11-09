package org.asteriskjava.pbx;

import java.util.concurrent.atomic.AtomicReference;

import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class PBXFactory
{

    public static PBX getActivePBX()
    {
        return AsteriskPBX.SELF;

    }

    final static AtomicReference<AsteriskSettings> profile = new AtomicReference<>();

    public static void init(AsteriskSettings newProfile)
    {
        profile.set(newProfile);
        getActivePBX().performPostCreationTasks();

    }

    public static AsteriskSettings getActiveProfile()
    {
        AsteriskSettings activeProfile = profile.get();
        if (activeProfile == null)
        {
            throw new RuntimeException(
                    "you must call setAsteriskSettings() before getActiveProfile() is called the first time");
        }

        return activeProfile;
    }

}
