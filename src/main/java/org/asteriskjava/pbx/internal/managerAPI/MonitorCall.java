package org.asteriskjava.pbx.internal.managerAPI;

import java.util.concurrent.atomic.AtomicLong;

import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.MonitorAction;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class MonitorCall
{
    private static final Log logger = LogFactory.getLog(MonitorCall.class);

    private static final AtomicLong id = new AtomicLong();

    private String file = null;

    public MonitorCall(final Call call)
    {
        super();

        try
        {
            this.setFile();
            final MonitorAction monitorAction = new MonitorAction(call.getRemoteParty(), this.file, "gsm", true);

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
        return this.file + ".gsm";
    }

    private void setFile()
    {
        long no = System.currentTimeMillis();
        no = no / 1000;
        final String unq = Long.toHexString(no);

        this.file = "njr" + unq + "-" + MonitorCall.id.incrementAndGet();

    }

}
