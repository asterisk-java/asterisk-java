package org.asteriskjava.manager.internal.backwardsCompatibility.bridge;

import org.asteriskjava.manager.event.BridgeEnterEvent;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BridgeEnterEventComparator implements Comparator<BridgeEnterEvent> {

	private static final String UNIQUE_ID_PATTERN = "([0-9]+)\\.([0-9]+)$";

	@Override
	public int compare(BridgeEnterEvent o1, BridgeEnterEvent o2) {
		Pattern uniqueIdPattern = Pattern.compile(UNIQUE_ID_PATTERN);
		Matcher uniqueId1Matcher = uniqueIdPattern.matcher(o1.getUniqueId());
		Matcher uniqueId2Matcher = uniqueIdPattern.matcher(o2.getUniqueId());

		boolean find1 = uniqueId1Matcher.find();
		boolean find2 = uniqueId2Matcher.find();

		if (find1 && find2) {
			// 1501234567.890 -> epochtime: 1501234567 | serial: 890
			long epochtime1 = Long.valueOf(uniqueId1Matcher.group(1));
			long epochtime2 = Long.valueOf(uniqueId2Matcher.group(1));
			int serial1 = Integer.valueOf(uniqueId1Matcher.group(2));
			int serial2 = Integer.valueOf(uniqueId2Matcher.group(2));

			return epochtime1 == epochtime2 ? Integer.compare(serial1, serial2) : Long.compare(epochtime1, epochtime2);
		} else if (!find1 && !find2) {
			// Both of inputs are invalid value: id1 == id2
			return 0;
		} else {
			// id1 is valid ==> 1
			return find1 ? 1 : -1;
		}
	}
}
