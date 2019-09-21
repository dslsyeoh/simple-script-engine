/*
 * Author Steven Yeoh
 * Copyright (c) 2019. All rights reserved
 */

package com.dsl.simple.script.engine.lib;

import java.util.Map;

public interface ScriptEngineManager
{
    void init(Map<String, String> data);

    boolean eval(String script);
}
