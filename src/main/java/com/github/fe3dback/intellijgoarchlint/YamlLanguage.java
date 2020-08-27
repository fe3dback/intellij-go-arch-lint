package com.github.fe3dback.intellijgoarchlint;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

public class YamlLanguage extends Language {
    // singleton
    public static final YamlLanguage INSTANCE = new YamlLanguage();
    public static final String MIME_TYPE1 = "application/x-yaml";
    public static final String MIME_TYPE2 = "application/yaml";
    public static final String MIME_TYPE_ARCH = "go-arch-lint/yaml";

    public YamlLanguage() {
        super("yamlGoArch", MIME_TYPE1, MIME_TYPE2, MIME_TYPE_ARCH);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "YAML/GoArch";
    }
}
