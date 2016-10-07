package org.asteriskjava.pbx.internal.asterisk;

public interface AsteriskSettings
{

    int getManagerPortNo();

    String getManagerPassword();

    String getManagerUsername();

    String getAsteriskIP();

    Integer getMeetmeBaseAddress();

    boolean getDisableBridge();

    String getManagementContext();

    String getExtensionPark();

    int getDialTimeout();

    String getAutoAnswer();

    String getAgiExtension();

    boolean getCanDetectHangup();

}
