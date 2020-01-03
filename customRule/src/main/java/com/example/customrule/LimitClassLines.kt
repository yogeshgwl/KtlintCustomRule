package com.example.customrule

import com.github.shyiko.ktlint.core.Rule
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes
import org.jetbrains.kotlin.utils.addToStdlib.flattenTo
import java.util.*
import kotlin.collections.HashSet

/**
 * @author GWL
 */
class LimitClassLines : Rule("limit-class-rule") {
    companion object {
        const val MAX_LIMIT: Int = 250
    }

    private val classToLinesCache = IdentityHashMap<KtClassOrObject, Int>()
    private val nestedClassTracking = IdentityHashMap<KtClassOrObject, HashSet<KtClassOrObject>>()

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType == KtStubElementTypes.CLASS) {
            val importDirective = node.psi as KtClass
            calcFunctions(importDirective)
            for ((clazz, lines) in classToLinesCache) {
                if (lines > MAX_LIMIT) {
                    emit(
                        node.startOffset,
                        "Class ${importDirective.name} contains $lines,this should not use more than lines $MAX_LIMIT lines.",
                        false
                    )
                }
            }
        }
    }

    private fun calcFunctions(classOrObject: KtClassOrObject) {
        classToLinesCache.clear()
        nestedClassTracking.clear()
        val lines = classOrObject.linesOfCode()
        classToLinesCache[classOrObject] = lines
        classOrObject.getStrictParentOfType<KtClassOrObject>()
            ?.let { nestedClassTracking.getOrPut(it) { HashSet() }.add(classOrObject) }
        findAllNestedClasses(classOrObject)
            .fold(0) { acc, next -> acc + (classToLinesCache[next] ?: 0) }
            .takeIf { it > 0 }
            ?.let { classToLinesCache[classOrObject] = lines - it }
    }

    private fun findAllNestedClasses(startClass: KtClassOrObject): Sequence<KtClassOrObject> =
        sequence {
            var nestedClasses = nestedClassTracking[startClass]
            while (!nestedClasses.isNullOrEmpty()) {
                yieldAll(nestedClasses)
                nestedClasses =
                    nestedClasses.mapNotNull { nestedClassTracking[it] }.flattenTo(HashSet())
            }
        }

}
