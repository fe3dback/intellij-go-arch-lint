package com.github.fe3dback.intellijgoarchlint.file;

import com.github.fe3dback.intellijgoarchlint.YamlLanguage;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.yaml.YAMLParserDefinition;

public class YamlParserDefinition extends YAMLParserDefinition {
    private static final IFileElementType FILE = new IFileElementType(YamlLanguage.INSTANCE);

    public IFileElementType getFileNodeType() {
        return FILE;
    }

    public PsiFile createFile(final FileViewProvider viewProvider) {
        return new YamlImpl(viewProvider);
    }
}
