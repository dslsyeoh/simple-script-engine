/*
 * Author Steven Yeoh
 * Copyright (c) 2019. All rights reserved
 */

package com.dsl.simple.script.engine;

import com.dsl.simple.script.engine.data.DataStorage;
import com.dsl.simple.script.engine.lib.ScriptEngine;
import com.dsl.simple.script.engine.lib.ScriptEngineV2;

import java.util.Arrays;
import java.util.List;

public class TestScriptEngine
{
    public static void main(String[] args)
    {
        DataStorage dataStorage = new DataStorage();
        dataStorage.mockData().forEach(dataStorage::update);

//        ScriptEngine.init(dataStorage.getData());
//        List<String> scripts = Arrays.asList("${PARAM_1} + ${PARAM_2} == ${PARAM_3}", "${PARAM_4} == 20", "${PARAM_3} == 10", "${PARAM_5} == ", "${PARAM_1} + ${PARAM_2} + ${PARAM_3} == 20",
//                "${PARAM_1} - ${PARAM_2} == 0", "${PARAM_1} * ${PARAM_2} == 25", "${PARAM_1} * ${PARAM_2} + ${PARAM_4} == 45", "${PARAM_1} / ${PARAM_2} == 1",
//                "${PARAM_1} * ${PARAM_2} + ${PARAM_3} + ${PARAM_4} / ${PARAM_1} == 11");
//        System.out.println(scripts.stream().allMatch(ScriptEngine::eval));
//
//        scripts = Arrays.asList("${PARAM_1} + ${PARAM_2} == ${PARAM_3}", "${PARAM_4} == 20");
//        System.out.println(scripts.stream().allMatch(ScriptEngine::eval));

        ScriptEngineV2.init(dataStorage.getData());
        System.out.println(ScriptEngineV2.eval("${PARAM_1} + ${PARAM_2} == ${PARAM_3}"));
        System.out.println(ScriptEngineV2.eval("${PARAM_1} + ${PARAM_2} <= ${PARAM_3}"));
        System.out.println(ScriptEngineV2.eval("${PARAM_1} + ${PARAM_2} < ${PARAM_3}"));
        System.out.println(ScriptEngineV2.eval("${PARAM_1} + ${PARAM_2} >= ${PARAM_3}"));
        System.out.println(ScriptEngineV2.eval("${PARAM_1} + ${PARAM_2} > ${PARAM_3}"));
        System.out.println(ScriptEngineV2.eval("${PARAM_1} + ${PARAM_2} != ${PARAM_4}"));
    }
}
