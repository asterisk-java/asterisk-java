package org.asteriskjava.pbx;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public enum TechType implements Tech
{

    //
    UNKNOWN, SIP, DAHDI, LOCAL, IAX, IAX2
    // The DIALPLAN tech is used to suppresses output of the tech
    // when obtaining the fully qualified name of the endpoint.
    // Basically if you want to call into an asterisk dialplan extension
    // then the tech needs to be suppressed.
    , DIALPLAN
    // CONSOLE is sent when a call is made from the asterisk console.
    // These type of channels are ignored.
    , CONSOLE

    // PARKING & SIPPEER are used during call parking
    , PARKING, SIPPEER;

    private static final Log logger = LogFactory.getLog(TechType.class);

    /**
     * Extracts the technology from a fully qualified endpoint string of the
     * form: TECH/NNNN
     * 
     * @param fullyQualifiedEndPoint
     * @return
     */
    public static TechType getTech(final String fullyQualifiedEndPoint)
    {
        if (!TechType.hasValidTech(fullyQualifiedEndPoint))
        {
            throw new IllegalArgumentException("The provided end point '" //$NON-NLS-1$
                    + fullyQualifiedEndPoint + "' must contain a tech prefix. e.g. SIP/100"); //$NON-NLS-1$
        }

        final String techName = fullyQualifiedEndPoint.substring(0, fullyQualifiedEndPoint.indexOf("/")); //$NON-NLS-1$
        return TechType.valueOf(techName.toUpperCase());
    }

    /**
     * returns true if the endPoint name contains a valid tech descriptor.
     * 
     * @param endPointName
     * @return
     */
    public static boolean hasValidTech(final String endPointName)
    {
        TechType tech = UNKNOWN;
        final int index = endPointName.indexOf("/"); //$NON-NLS-1$

        if (index >= 1)
        {
            final String techName = endPointName.substring(0, index);
            try
            {
                tech = TechType.valueOf(techName.toUpperCase());
            }
            catch (final IllegalArgumentException e)
            {
                TechType.logger.error("Invalid tech for endpoint:" + endPointName); //$NON-NLS-1$
            }
        }

        return tech != UNKNOWN;

    }

    /**
     * returns true if the endPoint name contains a tech descriptor even if it
     * isn't a known descriptor.
     * 
     * @param endPointName
     * @return
     */
    public static boolean hasTech(final String endPointName)
    {
        boolean hasTech = false;

        final int index = endPointName.indexOf("/"); //$NON-NLS-1$

        if (index != -1)
        {
            hasTech = true;
        }

        return hasTech;

    }

}
