package com.github.fe3dback.intellijgoarchlint.file;

import com.intellij.openapi.fileTypes.ExactFileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class YamlFileTypeFactory extends FileTypeFactory {
    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        ExactFileNameMatcher m1 = new ExactFileNameMatcher(".go-arch-lint.yml");
        ExactFileNameMatcher m2 = new ExactFileNameMatcher(".go-arch-lint.yaml");

        consumer.consume(YamlFileType.INSTANCE, m1, m2);
    }
}
