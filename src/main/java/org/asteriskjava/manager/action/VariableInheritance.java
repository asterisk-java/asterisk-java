package org.asteriskjava.manager.action;

public enum VariableInheritance
{
    NONE(""), SINGLE("_"), DOUBLE("__");

    private String prefix;

    VariableInheritance(String prefix)
    {
        this.prefix = prefix;
    }

    String getPrefix()
    {
        return prefix;
    }
}
