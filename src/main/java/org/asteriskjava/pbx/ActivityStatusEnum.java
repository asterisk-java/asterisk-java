package org.asteriskjava.pbx;

public enum ActivityStatusEnum
{
    START("Starting"), PROGRESS("Progress"), SUCCESS("Success"), FAILURE("Failure");

    String defaultMessage;

    ActivityStatusEnum(String defaultMessage)
    {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage()
    {
        return defaultMessage;
    }
}
