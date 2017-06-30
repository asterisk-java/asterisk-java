package org.asteriskjava.pbx.internal.managerAPI;

import java.util.Date;

import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.MonitorAction;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class MonitorCall
{
    private static final Log logger = LogFactory.getLog(MonitorCall.class);

    static long id = 1;

    private String file = null;

    public MonitorCall(final Call call)
    {
        super();

        try
        {
            this.setFile();
            final MonitorAction monitorAction = new MonitorAction(call.getRemoteParty(), this.file, "gsm", true); //$NON-NLS-1$

            AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

            pbx.sendAction(monitorAction, 1000);

        }
        catch (final Exception e)
        {
            MonitorCall.logger.error(e, e);
        }
    }

    public String getFilename()
    {
        return this.file + ".gsm"; //$NON-NLS-1$
    }

    private synchronized void setFile()
    {
        long no = (new Date()).getTime();
        no = no / 1000;
        final String unq = Long.toHexString(no);

        this.file = "njr" + unq + "-" + MonitorCall.id; //$NON-NLS-1$ //$NON-NLS-2$
        MonitorCall.id++;

    }

}
