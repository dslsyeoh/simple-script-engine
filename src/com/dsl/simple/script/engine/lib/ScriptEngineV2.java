/*
 * Author Steven Yeoh
 * Copyright (c) 2019. All rights reserved
 */

package com.dsl.simple.script.engine.lib;

import com.dsl.simple.script.engine.utils.Convert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ScriptEngineV2
{
    private static final String OPERATOR_REGEX = "[+-/*]";
    private static final String EXPRESSION_REGEX = "[+-/<>=*]";
    private static Map<String, String> data;

    private ScriptEngineV2() {}

    public static void init(Map<String, String> data)
    {
        ScriptEngineV2.data = data;
    }

    public static void eval(String script)
    {
        Map<String, String> map = new HashMap<>();
        String formalizedScript = formalizeScript(script);
        String trimmedFormalizedScript = Arrays.stream(formalizedScript.split("")).filter(value -> !value.matches("[\\s]")).collect(Collectors.joining());
        List<String> values = Arrays.stream(trimmedFormalizedScript.split("")).collect(Collectors.toList());
        long count = values.stream().filter(value -> value.matches(OPERATOR_REGEX)).count();
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

        if(trimmedFormalizedScript.indexOf('=') != -1 && Objects.equals(values.get(trimmedFormalizedScript.indexOf('=') + 1), "="))
        {
            System.out.println(Objects.equals(trimmedFormalizedScript.substring(trimmedFormalizedScript.lastIndexOf('=') + 1), map.get(String.valueOf(count - 1))));
        }
        else if(trimmedFormalizedScript.indexOf('<') != -1)
        {
            int evaluatedValue = Convert.toPrimitiveInt(map.get(String.valueOf(count - 1)));
            if(Objects.equals(values.get(trimmedFormalizedScript.indexOf('<') + 1), "="))
            {
                System.out.println(Convert.toPrimitiveInt(trimmedFormalizedScript.substring(trimmedFormalizedScript.lastIndexOf('=') + 1)) <= evaluatedValue);
            }
            else
            {
                System.out.println(Convert.toPrimitiveInt(trimmedFormalizedScript.substring(trimmedFormalizedScript.lastIndexOf('<') + 1)) < evaluatedValue);
            }
        }
        else if(trimmedFormalizedScript.indexOf('>') != -1)
        {
            int evaluatedValue = Convert.toPrimitiveInt(map.get(String.valueOf(count - 1)));
            if(Objects.equals(values.get(trimmedFormalizedScript.indexOf('>') + 1), "="))
            {
                System.out.println(Convert.toPrimitiveInt(trimmedFormalizedScript.substring(trimmedFormalizedScript.lastIndexOf('=') + 1)) >= evaluatedValue);
            }
            else
            {
                System.out.println(Convert.toPrimitiveInt(trimmedFormalizedScript.substring(trimmedFormalizedScript.lastIndexOf('>') + 1)) > evaluatedValue);
            }
        }
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
        return Stream.of(script.split(EXPRESSION_REGEX)).map(String::trim).filter(s -> s.startsWith("${")).collect(Collectors.toList());
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
        return IntStream.range(start, values.size()).filter(index -> values.get(index).matches(OPERATOR_REGEX)).findFirst().orElse(-1);
    }

}
