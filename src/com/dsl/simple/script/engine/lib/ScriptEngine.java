/*
 * Author Steven Yeoh
 * Copyright (c) 2019. All rights reserved
 */

package com.dsl.simple.script.engine.lib;

import com.dsl.simple.script.engine.constants.MathOperator;
import com.dsl.simple.script.engine.constants.Operator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.dsl.simple.script.engine.constants.Operator.*;
import static com.dsl.simple.script.engine.utils.Convert.toPrimitiveInt;

public class ScriptEngine implements ScriptEngineManager
{
    private static final String BASIC_MATH_OPERATOR_REGEX = "[+-/*]";
    private static final String OPERATOR_REGEX = "[!+-/<>=*]";
    private Map<String, String> data;
    private Map<String, String> map;

    @Override
    public void init(Map<String, String> data)
    {
        this.data = data;
    }

    @Override
    public boolean eval(String script)
    {
        map = new HashMap<>();
        String formalizedScript = formalizeScript(script);
        String trimmedFormalizedScript = Arrays.stream(formalizedScript.split("")).filter(value -> !value.matches("[\\s]")).collect(Collectors.joining());
        List<String> values = Arrays.stream(trimmedFormalizedScript.split("")).collect(Collectors.toList());
        List<String> scriptValues = Arrays.stream(trimmedFormalizedScript.split(OPERATOR_REGEX)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        long count = values.stream().filter(value -> value.matches(BASIC_MATH_OPERATOR_REGEX)).count();
        int index = findIndex(0, values);

        for(int i = 0; i < count; i++)
        {
            if(i == 0)
            {
                map.put(String.valueOf(i), calculate(values.get(index - 1), scriptValues.get(index), getMathOperator(values.get(index))));
            }
            else
            {
                index = findIndex(index + 1, values);
                map.put(String.valueOf(i), calculate(map.get(String.valueOf(i - 1)), scriptValues.get(i + 1), getMathOperator(values.get(index))));
            }
        }

        if(count == 0)
        {
            String value = trimmedFormalizedScript.substring(0, trimmedFormalizedScript.indexOf('='));
            return eval(trimmedFormalizedScript, value, getOperator(trimmedFormalizedScript, values));
        }
        return eval(trimmedFormalizedScript, getEvaluatedResult(count), getOperator(trimmedFormalizedScript, values));
    }

    private String getEvaluatedResult(long count)
    {
        return count == 0 ? map.get(String.valueOf(count)) : map.get(String.valueOf(count - 1));
    }

    private boolean eval(String script, String evaluatedResult, Operator operator)
    {
        if(Objects.nonNull(operator))
        {
            switch (operator)
            {
                case EQUAL:
                    return Objects.equals(getSubStringAfter(script, '='), evaluatedResult);
                case LESS_THAN:
                    return toPrimitiveInt(getSubStringAfter(script, '<')) < toPrimitiveInt(evaluatedResult);
                case NOT_MORE_THAN:
                    return toPrimitiveInt(getSubStringAfter(script, '=')) <= toPrimitiveInt(evaluatedResult);
                case MORE_THAN:
                    return toPrimitiveInt(getSubStringAfter(script, '>')) > toPrimitiveInt(evaluatedResult);
                case NOT_LESS_THAN:
                    return toPrimitiveInt(getSubStringAfter(script, '=')) >= toPrimitiveInt(evaluatedResult);
                default:
                    return true;
            }
        }
        return true;
    }

    private String formalizeScript(String script)
    {
        return formalizeScript(script, getParameters(script));
    }

    private String formalizeScript(String script, List<String> parameters)
    {
        String formalizedScript = script;

        for(String parameter : parameters)
        {
            String key = parameter.substring(parameter.indexOf('{') + 1, parameter.indexOf('}'));
            formalizedScript = formalizedScript.replace(parameter, data.get(key));
        }

        return formalizedScript;
    }

    private List<String> getParameters(String script)
    {
        return Stream.of(script.split(OPERATOR_REGEX)).map(String::trim).filter(s -> s.startsWith("${")).collect(Collectors.toList());
    }

    private String calculate(String first, String second, MathOperator mathOperator)
    {
        switch (mathOperator)
        {
            case ADDITION:
                return String.valueOf(toPrimitiveInt(first) + toPrimitiveInt(second));
            case SUBTRACTION:
                return String.valueOf(toPrimitiveInt(first) - toPrimitiveInt(second));
            case MULTIPLICATION:
                return String.valueOf(toPrimitiveInt(first) * toPrimitiveInt(second));
            case DIVISION:
                return String.valueOf(toPrimitiveInt(first) / toPrimitiveInt(second));
        }
        return "";
    }

    private int findIndex(int start, List<String> values)
    {
        return IntStream.range(start, values.size()).filter(index -> values.get(index).matches(BASIC_MATH_OPERATOR_REGEX)).findFirst().orElse(-1);
    }

    private MathOperator getMathOperator(String value)
    {
        return MathOperator.parse(value);
    }

    private Operator getOperator(String trimmedFormalizedScript, List<String> values)
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

    private static String getSubStringAfter(String script, char ch)
    {
        return script.substring(script.lastIndexOf(ch) + 1);
    }
}
