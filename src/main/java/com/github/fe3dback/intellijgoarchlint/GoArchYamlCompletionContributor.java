package com.github.fe3dback.intellijgoarchlint;

import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLLanguage;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Arrays;

public class GoArchYamlCompletionContributor extends CompletionContributor {

    private static final String keyWordDeps = "deps";
        private static final String keyWordVersion = "version";
        private static final String keyWordAllow = "allow";
        private static final String keyWordExclude = "exclude";
        private static final String keyWordExcludeFiles = "excludeFiles";
        private static final String keyWordComponents = "components";
        private static final String keyWordVendors = "vendors";
        private static final String keyWordCommonComponents = "commonComponents";
        private static final String keyWordCommonVendors = "commonVendors";

    private static String[] keyWordsTopLevel = {
            keyWordVersion,
            keyWordAllow,
            keyWordExclude,
            keyWordExcludeFiles,
            keyWordComponents,
            keyWordVendors,
            keyWordCommonComponents,
            keyWordCommonVendors,
            keyWordDeps,
    };

    public GoArchYamlCompletionContributor() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(YAMLLanguage.INSTANCE),
                new Provider()
        );
    }

    private static LookupElementBuilder createKeyLookupElement(
            @NotNull final String propertyName
    ) {
        return LookupElementBuilder
                .create(propertyName)
                .withIcon(GoArchIcons.FILETYPE_ICON);
    }

    private static boolean isTopLevelMapping(final YAMLKeyValue keyValue) {
        return keyValue.getParent() instanceof YAMLDocument;
    }

    /** The main actor in generating completion suggestions. */
    private static class Provider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(
                @NotNull final CompletionParameters completionParameters,
                final ProcessingContext processingContext,
                @NotNull final CompletionResultSet resultSet
        ) {
            final PsiElement element = completionParameters.getPosition();
            if (!GoArchPsiUtils.isGoArchFile(element)) {
                return;
            }

            final YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(element, YAMLKeyValue.class);

            // We must be at the very top level if there is no enclosing keyValue
            if (keyValue == null) {
                Arrays.stream(keyWordsTopLevel).forEachOrdered(s -> resultSet.addElement(createKeyLookupElement(s)));
                return;
            }


        }
    }
}
