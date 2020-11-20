package com.github.fe3dback.intellijgoarchlint;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLFile;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Collection;
import java.util.stream.Stream;

public final class GoArchPsiUtils {
    private GoArchPsiUtils() {
        // no constructor
    }

    public static boolean isGoArchFile(final PsiElement element) {
        final PsiFile file = element.getContainingFile();
        if (file instanceof YAMLFile) {
            final Collection<YAMLKeyValue> keys = YAMLUtil.getTopLevelKeys((YAMLFile) file);

            return
                keys.stream().map(YAMLKeyValue::getKeyText).anyMatch("components"::equals) &&
                keys.stream().map(YAMLKeyValue::getKeyText).anyMatch("deps"::equals)
            ;
        }

        return false;
    }
}
