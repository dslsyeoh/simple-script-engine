Simple Script Engine
-

Overview
-- 
1. Does not support `complicated` script
2. Supported `operators` are `ADDITION`, `SUBTRACTION`, `MULTIPLICATION`, `DIVISION` and `EQUAL`.
3. `script` format must be in `${`key`}` `operator` `${`key`}` and so on.
4. `Compare` `empty` string in refer to `no.3` in **Sample** section.

Sample
--
1. `"${PARAM_1} + ${PARAM_2} == ${PARAM_3}"`
2. `"${PARAM_4} == 20", "${PARAM_3} == 10"`
3. `"${PARAM_5} == "`
4. `"${PARAM_1} + ${PARAM_2} + ${PARAM_3} == 20"`
5. `"${PARAM_1} * ${PARAM_2} + ${PARAM_3} + ${PARAM_4} / ${PARAM_1} == 11"`

21/09/2019
--
Introduce `ScriptEngineV2` to support 6 types of `operator` list belows
1. `EQUAL`
2. `NOT_EQUAL`
3. `LESS_THAN`
4. `NOT_MORE_THAN`
5. `MORE_THAN`
6. `NOT_LESS_THAN`
