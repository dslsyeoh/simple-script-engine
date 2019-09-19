Simple Script Engine
-

Overview
-- 
1. Does not support `complicated` script
2. Supported `operators` are `ADDITION`, `SUBTRACTION`, `MULTIPLICATION`, `DIVISION` and `EQUAL`.
3. Accept `list` only.
4. `script` format must be in `${`key`}` `operator` `${`key`}` and so on.
5. `Compare` `empty` string in refer to `no.3` in **Sample** section.

Sample
--
1. `"${PARAM_1} + ${PARAM_2} == ${PARAM_3}"`
2. `"${PARAM_4} == 20", "${PARAM_3} == 10"`
3. `"${PARAM_5} == "`
4. `"${PARAM_1} + ${PARAM_2} + ${PARAM_3} == 20"`
5. `"${PARAM_1} * ${PARAM_2} + ${PARAM_3} + ${PARAM_4} / ${PARAM_1} == 11"`
