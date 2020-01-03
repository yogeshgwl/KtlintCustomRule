package com.example.customrule

import com.github.shyiko.ktlint.core.Rule
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes
import org.jetbrains.kotlin.utils.addToStdlib.flattenTo
import java.util.*
import kotlin.collections.HashSet

/**
 * @author GWL
 */
class LimitFunctionLines : Rule("limit-method-rule") {
    companion object {
        const val MAX_LIMIT: Int = 25
    }

    private val functionToLinesCache = HashMap<KtNamedFunction, Int>()
    private val nestedFunctionTracking =
        IdentityHashMap<KtNamedFunction, HashSet<KtNamedFunction>>()

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType == KtStubElementTypes.FUNCTION) {
            val importDirective = node.psi as KtNamedFunction
            calcFunctions(importDirective)
            for ((clazz, lines) in functionToLinesCache) {
                if (lines > MAX_LIMIT) {
                    emit(
                        node.startOffset,
                        "Function ${importDirective.name} contains $lines,this should not use more than $MAX_LIMIT lines.",
                        false
                    )
                }
            }
        }
    }

    private fun calcFunctions(function: KtNamedFunction) {
        functionToLinesCache.clear()
        nestedFunctionTracking.clear()
        val lines = function.linesOfCode()
        functionToLinesCache[function] = lines
        function.getStrictParentOfType<KtNamedFunction>()
            ?.let { nestedFunctionTracking.getOrPut(it) { HashSet() }.add(function) }
        findAllNestedFunctions(function)
            .fold(0) { acc, next -> acc + (functionToLinesCache[next] ?: 0) }
            .takeIf { it > 0 }
            ?.let { functionToLinesCache[function] = lines - it }
    }

    private fun findAllNestedFunctions(startFunction: KtNamedFunction): Sequence<KtNamedFunction> =
        sequence {
            var nestedFunctions = nestedFunctionTracking[startFunction]
            while (!nestedFunctions.isNullOrEmpty()) {
                yieldAll(nestedFunctions)
                nestedFunctions =
                    nestedFunctions.mapNotNull { nestedFunctionTracking[it] }.flattenTo(HashSet())
            }
        }
}
