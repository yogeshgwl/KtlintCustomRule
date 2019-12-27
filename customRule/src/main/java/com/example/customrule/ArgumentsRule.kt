package com.example.customrule

import com.github.shyiko.ktlint.core.Rule
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes

// * Created on 27/12/19.
/**
 * @author GWL
 */
class ArgumentsRule : Rule("argument-limit-rule") {
    companion object {
        const val MAX_LIMIT: Int = 4
    }

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType == KtStubElementTypes.FUNCTION) {
            val importDirective = node.psi as KtNamedFunction
            if (importDirective.valueParameters.size > MAX_LIMIT)
                emit(node.startOffset, "function ${importDirective.name} should use only $MAX_LIMIT arguments.", false)
        }
    }
}