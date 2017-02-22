package org.asteriskjava.pbx.activities;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.EndPoint;

public interface ParkActivity extends Activity
{

	/**
	 * Returns the endpoint (parking lot) that the call was park on.
	 * 
	 * @return
	 */
	EndPoint getParkingLot();

}
