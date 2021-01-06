package com.github.fe3dback.intellijgoarchlint.reference.pattern

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.YAMLTokenTypes
import org.jetbrains.yaml.psi.*

object GoArchPsiPattern {
    /**
     * at root:
     * ---                      // mapping
     * commonComponents:        // keyValue
     *                          // sequence (all items in list)
     *   - game
     *   - utils                // sequence item
     *     ^^^^^
     *       ^ scalar text
     * ---
     */
    val onCommonComponents: PsiElementPattern.Capture<PsiElement> =
        PlatformPatterns.psiElement()
            .withElementType(YAMLTokenTypes.SCALAR_TEXT)
//            .withSuperParent(1, YAMLSequence::class.java)
//            .withSuperParent(2, YAMLSequenceItem::class.java)
//            .withSuperParent(3, YAMLKeyValue::class.java)
//            .withSuperParent(4, YAMLMapping::class.java)
            .withLanguage(YAMLLanguage.INSTANCE)
}