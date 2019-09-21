/*
 * Author Steven Yeoh
 * Copyright (c) 2019. All rights reserved
 */

package com.dsl.simple.script.engine.constants;

import java.util.Arrays;
import java.util.Objects;

public enum MathOperator
{
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/");

    private String symbol;

    MathOperator(String symbol)
    {
        this.symbol = symbol;
    }

    public static MathOperator parse(String value)
    {
        return Arrays.stream(values()).filter(mathOperator -> Objects.equals(mathOperator.symbol, value)).findFirst().orElse(null);
    }
}
