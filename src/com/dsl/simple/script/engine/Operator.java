package com.dsl.simple.script.engine;

import java.util.Objects;
import java.util.stream.Stream;

public enum Operator
{
    ADDITION("+"),
    SUBTRACTION("-" ),
    MULTIPLICATION("*"),
    DIVISION("/"),
    EQUAL("==");

    private String symbol;

    Operator(String symbol)
    {
        this.symbol = symbol;
    }

    public static Operator parse(String value)
    {
        return Stream.of(values()).filter(operator -> Objects.equals(operator.getSymbol(), value)).findFirst().orElse(null);
    }

    public String getSymbol()
    {
        return symbol;
    }
}
