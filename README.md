Simple Script Engine
-

Overview
- 
1. Supported 6 types of `operator` listed belows
    - `EQUAL`
    - `NOT_EQUAL`
    - `LESS_THAN`
    - `NOT_MORE_THAN`
    - `MORE_THAN`
    - `NOT_LESS_THAN`
2. `script` format must be in `${`key`}` `operator` `${`key`}` and so on.
3. Supported basic `operation` listed belows
    - `ADDITION`
    - `SUBTRACTION`
    - `MULTIPLICATION`
    - `DIVISION`
4. `Compare` `empty` string in refer to `no.3` in **Sample** section.

Sample
-
1. `"${PARAM_1} + ${PARAM_2} == ${PARAM_3}"`
2. `"${PARAM_4} == 20", "${PARAM_3} == 10"`
3. `"${PARAM_5} == "`
4. `"${PARAM_1} + ${PARAM_2} + ${PARAM_3} == 20"`
5. `"${PARAM_1} * ${PARAM_2} + ${PARAM_3} + ${PARAM_4} / ${PARAM_1} == 11"`

