/*
 * Author Steven Yeoh
 * Copyright (c) 2019. All rights reserved
 */

package com.dsl.simple.script.engine.lib;

import com.dsl.simple.script.engine.Operator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dsl.simple.script.engine.Operator.*;

public class ScriptEngine
{
    private static final String OPERATOR_REGEX = "[+*-/=]";
    private static Map<String, String> data;

    private ScriptEngine() {}

    public static void init(Map<String, String> data)
    {
        ScriptEngine.data = data;
    }

    public static boolean eval(String script)
    {
        return evaluateExpression(formalizeScript(script));
    }

    private static boolean evaluateExpression(String formalizedScript)
    {
        int total = 0;
        String[] expressionValues = formalizedScript.split("\\s");

        int index = 0;
        for(String expressionValue : expressionValues)
        {
            long count = Stream.of(expressionValues).filter(s -> s.matches(OPERATOR_REGEX)).count();
            Operator operator = Operator.parse(expressionValue);
            if(Objects.nonNull(operator))
            {
                if(operator.equals(ADDITION))
                {
                    total += (total == 0 ? Integer.parseInt(expressionValues[index - 1]) + Integer.parseInt(expressionValues[index + 1]) : Integer.parseInt(expressionValues[index + 1])) ;
                }
                else if(operator.equals(SUBTRACTION))
                {
                    total -= (total == 0 ? Integer.parseInt(expressionValues[index - 1]) - Integer.parseInt(expressionValues[index + 1]) : Integer.parseInt(expressionValues[index + 1])) ;
                }
                else if(operator.equals(MULTIPLICATION))
                {
                    if(total == 0) total = 1;
                    total *= (total == 1 ? Integer.parseInt(expressionValues[index - 1]) * Integer.parseInt(expressionValues[index + 1]) : Integer.parseInt(expressionValues[index + 1])) ;
                }
                else if(operator.equals(DIVISION))
                {
                    if(total == 0) total = 1;
                    total /= (total == 1 ? Integer.parseInt(expressionValues[index - 1]) / Integer.parseInt(expressionValues[index + 1]) : Integer.parseInt(expressionValues[index + 1])) ;
                }
                else if(operator.equals(EQUAL))
                {
                    if(count == 0)
                    {
                        return Objects.equals(expressionValues[0], Objects.equals(operator.getSymbol(), expressionValues[expressionValues.length - 1]) ? "" : expressionValues[0]);
                    }
                    return Objects.equals(expressionValues[expressionValues.length - 1], String.valueOf(total));
                }
            }
            index++;
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
}
