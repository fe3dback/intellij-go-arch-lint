package com.github.fe3dback.intellijgoarchlint

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.psi.tree.TokenSet
import org.jetbrains.yaml.YAMLTokenTypes
import org.jetbrains.yaml.lexer.YAMLFlexLexer

@Deprecated("Prev")
class GoArchYamlWordScanner : DefaultWordsScanner(
        YAMLFlexLexer(),
        TokenSet.create(YAMLTokenTypes.SCALAR_KEY),
        TokenSet.create(YAMLTokenTypes.COMMENT),
        TokenSet.create(
                YAMLTokenTypes.SCALAR_STRING,
                YAMLTokenTypes.SCALAR_DSTRING,
                YAMLTokenTypes.TEXT,
        ),
)