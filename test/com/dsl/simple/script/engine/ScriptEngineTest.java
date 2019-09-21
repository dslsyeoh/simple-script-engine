/*
 * Author Steven Yeoh
 * Copyright (c) 2019. All rights reserved
 */

package com.dsl.simple.script.engine;

import com.dsl.simple.script.engine.data.DataStorage;
import com.dsl.simple.script.engine.lib.ScriptEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class ScriptEngineTest
{
    private ScriptEngine scriptEngine;

    @BeforeAll
    public void initialize()
    {
        DataStorage dataStorage = new DataStorage();
        scriptEngine = new ScriptEngine();
        scriptEngine.init(dataStorage.mockData());

    }

    @Test
    public void testParam1AddParam2EqualParam3()
    {
        assertTrue(scriptEngine.eval("${PARAM_1} + ${PARAM_2} == ${PARAM_3}"));
    }

    @Test
    public void testParam1AddParam2LessThanParam3()
    {
        Assertions.assertFalse(scriptEngine.eval("${PARAM_1} + ${PARAM_2} < ${PARAM_3}"));
    }

    @Test
    public void testParam1AddParam2NotMoreThanParam3()
    {
        assertTrue(scriptEngine.eval("${PARAM_1} + ${PARAM_2} <= ${PARAM_3}"));
    }

    @Test
    public void testParam1AddParam2MoreThanParam3()
    {
        Assertions.assertFalse(scriptEngine.eval("${PARAM_1} + ${PARAM_2} > ${PARAM_3}"));
    }

    @Test
    public void testParam1AddParam2NotLessThanParam3()
    {
        assertTrue(scriptEngine.eval("${PARAM_1} + ${PARAM_2} >= ${PARAM_3}"));
    }

    @Test
    public void testParam1AddParam2NotEqualParam4()
    {
        assertTrue(scriptEngine.eval("${PARAM_1} + ${PARAM_2} != ${PARAM_4}"));
    }

    @Test
    public void testAnyMatchExpression()
    {
        List<String> scripts = Arrays.asList("${PARAM_1} + ${PARAM_2} == ${PARAM_3}", "${PARAM_1} + ${PARAM_2} != ${PARAM_3}");
        assertTrue(scripts.stream().anyMatch(scriptEngine::eval));
    }

    @Test
    public void testEqualEmpty()
    {
        assertTrue(scriptEngine.eval("${PARAM_5} == "));
    }

    @Test
    public void testNotEqualEmpty()
    {
        assertTrue(scriptEngine.eval("${PARAM_2} != "));
    }

    @Test
    public void testComplexExpressionAllMatch()
    {
        List<String> scripts = Arrays.asList("${PARAM_1} + ${PARAM_2} == ${PARAM_3}", "${PARAM_4} == 20", "${PARAM_3} == 10", "${PARAM_5} == ", "${PARAM_1} + ${PARAM_2} + ${PARAM_3} == 20",
                "${PARAM_1} - ${PARAM_2} == 0", "${PARAM_1} * ${PARAM_2} == 25", "${PARAM_1} * ${PARAM_2} + ${PARAM_4} == 45", "${PARAM_1} / ${PARAM_2} == 1",
                "${PARAM_1} * ${PARAM_2} + ${PARAM_3} + ${PARAM_4} / ${PARAM_1} == 11");
        assertTrue(scripts.stream().allMatch(scriptEngine::eval));
    }
}