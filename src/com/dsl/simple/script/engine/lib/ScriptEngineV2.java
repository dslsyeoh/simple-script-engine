/*
 * Author Steven Yeoh
 * Copyright (c) 2019. All rights reserved
 */

package com.dsl.simple.script.engine.lib;

import com.dsl.simple.script.engine.constants.Operator;
import com.dsl.simple.script.engine.utils.Convert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.dsl.simple.script.engine.constants.Operator.*;

public class ScriptEngineV2
{
    private static final String MATH_REGEX = "[+-/*]";
    private static final String OPERATOR_REGEX = "[!+-/<>=*]";
    private static Map<String, String> data;
    private static Map<String, String> map = new HashMap<>();

    private ScriptEngineV2() {}

    public static void init(Map<String, String> data)
    {
        ScriptEngineV2.data = data;
    }

    public static boolean eval(String script)
    {
        String formalizedScript = formalizeScript(script);
        String trimmedFormalizedScript = Arrays.stream(formalizedScript.split("")).filter(value -> !value.matches("[\\s]")).collect(Collectors.joining());
        List<String> values = Arrays.stream(trimmedFormalizedScript.split("")).collect(Collectors.toList());
        long count = values.stream().filter(value -> value.matches(MATH_REGEX)).count();
        int index = findIndex(0, values);

        for(int i = 0; i < count; i++)
        {
            if(i == 0)
            {
                map.put(String.valueOf(i), calculate(values.get(index - 1), values.get(index + 1), values.get(index)));
            }
            else
            {
                index = findIndex(index + 1, values);
                map.put(String.valueOf(i), calculate(map.get(String.valueOf(i - 1)), values.get(index + 1), values.get(index)));
            }
        }

        return eval(trimmedFormalizedScript, getEvaluatedResult(count), getExpression(trimmedFormalizedScript, values));
    }

    private static String getEvaluatedResult(long count)
    {
        return map.get(String.valueOf(count - 1));
    }

    private static boolean eval(String script, String evaluatedResult, Operator operator)
    {
        if(Objects.nonNull(operator))
        {
            switch (operator)
            {
                case EQUAL:
                    return Objects.equals(script.substring(script.lastIndexOf('=') + 1), evaluatedResult);
                case LESS_THAN:
                    return Convert.toPrimitiveInt(script.substring(script.lastIndexOf('<') + 1)) < Convert.toPrimitiveInt(evaluatedResult);
                case NOT_MORE_THAN:
                    return Convert.toPrimitiveInt(script.substring(script.lastIndexOf('=') + 1)) <= Convert.toPrimitiveInt(evaluatedResult);
                case MORE_THAN:
                    return Convert.toPrimitiveInt(script.substring(script.lastIndexOf('>') + 1)) > Convert.toPrimitiveInt(evaluatedResult);
                case NOT_LESS_THAN:
                    return Convert.toPrimitiveInt(script.substring(script.lastIndexOf('=') + 1)) >= Convert.toPrimitiveInt(evaluatedResult);
            }
        }
        return true;
    }

    private static String formalizeScript(String script)
    {
        return formalizeScript(script, getParameters(script));
    }

    private static String formalizeScript(String script, List<String> parameters)
    {
        String formalizedScript = script;

        for(String parameter : parameters)
        {
            String key = parameter.substring(parameter.indexOf('{') + 1, parameter.indexOf('}'));
            formalizedScript = formalizedScript.replace(parameter, data.get(key));
        }

        return formalizedScript;
    }

    private static List<String> getParameters(String script)
    {
        return Stream.of(script.split(OPERATOR_REGEX)).map(String::trim).filter(s -> s.startsWith("${")).collect(Collectors.toList());
    }

    private static String calculate(String first, String second, String operator)
    {
        if(Objects.equals(operator, "+"))
        {
            return String.valueOf(Convert.toPrimitiveInt(first) + Convert.toPrimitiveInt(second));
        }
        else if(Objects.equals(operator, "*"))
        {
            return String.valueOf(Convert.toPrimitiveInt(first) * Convert.toPrimitiveInt(second));
        }

        return "";
    }

    private static int findIndex(int start, List<String> values)
    {
        return IntStream.range(start, values.size()).filter(index -> values.get(index).matches(MATH_REGEX)).findFirst().orElse(-1);
    }

    private static Operator getExpression(String trimmedFormalizedScript, List<String> values)
    {
        int lt = trimmedFormalizedScript.indexOf('<');
        int gt = trimmedFormalizedScript.indexOf('>');
        int equal = trimmedFormalizedScript.lastIndexOf('=');

        if(lt != -1)
        {
            return Objects.equals(values.get(lt + 1), "=") ? NOT_MORE_THAN : LESS_THAN;
        }
        else if(gt != -1)
        {
            return Objects.equals(values.get(gt + 1), "=") ? NOT_LESS_THAN : MORE_THAN;
        }
        else if(equal != -1)
        {
            return Objects.equals(values.get(equal - 1), "!") ? NOT_EQUAL : EQUAL;
        }
        return null;
    }
}
