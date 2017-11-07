package org.asteriskjava.pbx;

public interface AsteriskSettings
{

    /**
     * The port no. that the asterisk manager api listens on. This should be
     * 5060 unless you have re-configured the manager api.
     * 
     * @return the port no.
     */
    int getManagerPortNo();

    /**
     * This password MUST match the password (secret=) in manager.conf
     * 
     * @return
     */
    String getManagerPassword();

    /**
     * This MUST match the section header (e.g. '[myconnection]') in
     * manager.conf
     * 
     * @return
     */
    String getManagerUsername();

    /**
     * The IP address of FQND of the Asterisk PBX
     * 
     * @return IP address or FQDN.
     */
    String getAsteriskIP();

    /**
     * Asterisk-Java uses conference rooms for a variety of purposes. Just make
     * certain that the base address doesn't overlap any conference rooms you
     * are currently running on your pbx. You should probably allow 200
     * conference rooms here e.g. (3000-3200) Actions like transfers (and
     * conference calls) use a conference room.
     * 
     * @return meetme base address
     */
    Integer getMeetmeBaseAddress();

    /**
     * Return true if you Asterisk version doesn't support bridging channels.
     * All version after 1.8 support bridging so normally you should returm
     * false;
     * 
     * @return false if you asterisk version supports bridging channels.
     */
    boolean getDisableBridge();

    /**
     * This is an asterisk dialplan context. Simply pick a context which doesn't
     * already exist and use AsteriskPBX.createAgiEntryPoint() to inject the
     * necessary dialplan used by asterisk-java
     * 
     * @return an unused asterisk dialplan context. e.g. "asteriskjava"
     */
    String getManagementContext();

    /**
     * Return an asterisk dialplan extension (within the above Management
     * Context) that will be used to park calls. Just ensure that its a unique
     * asterisk dialplan extension name.
     */
    String getExtensionPark();

    /**
     * The time (in seconds) to wait for a dial attempt to be answered before
     * giving up. Recommend a value of 30.
     * 
     * @return
     */
    int getDialTimeout();

    /**
     * Return the SIP header to be used to force a handset to auto-answer when
     * dialed. e.g. For Yealink/Snom use return "Call-Info:\; answer-after=0";
     * 
     * @return
     */
    String getAutoAnswer();

    /**
     * Return an asterisk dialplan extension (within the Management Context)
     * that will be used to inject the agi dialplan entry point used by
     * Activites.. Just ensure that its a unique asterisk dialplan extension
     * name.
     * 
     * @return "asterisk-java-agi"
     */
    String getAgiExtension();

    /**
     * Return true if the telephony tech you are dialing from can reliably
     * detect hangups. (for SIP set to true, POTS set false).
     * 
     * @return
     */
    boolean getCanDetectHangup();

    /**
     * The IP address of FQDN of the asterisk-java application.
     * Currenly only used if you are doing 'activities' but its
     * safer to just set this.
     * @return
     */
    String getAgiHost();

}
