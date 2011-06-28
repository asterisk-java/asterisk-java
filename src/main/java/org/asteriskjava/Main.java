package org.asteriskjava;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.OriginateCallback;

public class Main {
	public static void main(String[] args) throws Exception {
		AsteriskServer server = new DefaultAsteriskServer("192.168.2.22",
				"manager", "pa55w0rd");
		String channel = "DGV/g1/2560";
		String context = "send-fax";
		String exten = "89300";
		int priority = 1;
		long timeout = 30000l;
		CallerId callerId = new CallerId("FAX", "1193907180");
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("FAXFILE",
				"/var/spool/asterisk/tmp/960720100712140455.tif");
		OriginateCallback callback = new OriginateCallback() {
			@Override
			public void onSuccess(AsteriskChannel channel) {
				System.out.println("onSuccess");
			}

			@Override
			public void onNoAnswer(AsteriskChannel channel) {
				System.out.println("onNoAnswer");
			}

			@Override
			public void onFailure(LiveException cause) {
				System.out.println("onFailure");
			}

			@Override
			public void onDialing(AsteriskChannel channel) {
				System.out.println("onDialing");
			}

			@Override
			public void onBusy(AsteriskChannel channel) {
				System.out.println("onBusy");
			}
		};
		server.originateToExtensionAsync(channel, context, exten, priority,
				timeout, callerId, variables, callback);
		while (true)
			Thread.sleep(10);
	}

}
