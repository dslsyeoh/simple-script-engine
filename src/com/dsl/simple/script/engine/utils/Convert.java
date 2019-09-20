/*
 * Author Steven Yeoh
 * Copyright (c) 2019. All rights reserved
 */

package com.dsl.simple.script.engine.utils;

import java.util.Objects;

public class Convert
{
    private Convert() {}

    public static int toPrimitiveInt(String value)
    {
        return toPrimitiveInt(value, 0);
    }

    public static int toPrimitiveInt(String value, int def)
    {
        return Objects.nonNull(value) ? Integer.parseInt(value) : def;
    }


}
