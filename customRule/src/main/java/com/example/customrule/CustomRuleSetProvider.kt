package com.example.customrule

import com.pinterest.ktlint.core.RuleSet
import com.pinterest.ktlint.core.RuleSetProvider
// * Created on 27/12/19.
/**
 * @author GWL
 */
class CustomRuleSetProvider : RuleSetProvider {
    override fun get() =
        RuleSet(
            "custom-ktlint-rules",
            NoInternalImportRule(),
            ArgumentsRule(),
            LimitFunctions(),
            LimitClassLines(),
            LimitFunctionLines()
        )
}
