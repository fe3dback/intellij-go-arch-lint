package com.github.fe3dback.intellijgoarchlint.file;

import com.github.fe3dback.intellijgoarchlint.Yaml;
import com.github.fe3dback.intellijgoarchlint.YamlIcons;
import com.github.fe3dback.intellijgoarchlint.YamlLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.StubVirtualFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class YamlFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {
    public static final YamlFileType INSTANCE = new YamlFileType();

    public static final String DEFAULT_EXTENSION = "yml";

    /** Number of bytes to read when guessing for the file type based on content. */
    private static final int BYTES_TO_READ = 4096;

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger(YamlFileType.class);

    protected YamlFileType() {
        super(YamlLanguage.INSTANCE);
    }

    @NotNull
    public String getName() {
        return Yaml.LANGUAGE_NAME;
    }

    @NotNull
    public String getDescription() {
        return Yaml.LANGUAGE_DESCRIPTION;
    }

    @NotNull
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @NotNull
    public Icon getIcon() {
        return YamlIcons.FILETYPE_ICON;
    }


    @Override
    public boolean isMyFileType(@NotNull final VirtualFile file) {
        if (file instanceof StubVirtualFile) {
            return false; // Helps New -> File get correct file type
        }

        if (!file.isValid()) {
            return false;
        }

        final String extension = file.getExtension();
        if (!("yml".equalsIgnoreCase(extension) || "yaml".equalsIgnoreCase(extension))) {
            return false;
        }

        try (InputStream inputStream = file.getInputStream()) {
            final byte[] bytes = new byte[BYTES_TO_READ];
            final int n = inputStream.read(bytes, 0, BYTES_TO_READ);
            return n > 0 && isGoArchYaml(bytes);
        } catch (final IOException e) {
            logger.info("Error while determining file type.", e);
        }

        return false;
    }

    /**
     * Guess whether the file is a GoArch YAML file from a subset of the file content.
     *
     * @param bytes the bytes to check.
     * @return true if the file is a GoArch YAML file, otherwise, false.
     */
    private boolean isGoArchYaml(final byte[] bytes) {
        boolean HasComponentsTag = false;
        boolean HasDependenciesTag = false;
        boolean HasAllowTag = false;
        boolean HasDependOnVendorTag = false;

        try (Scanner scanner = new Scanner(new String(bytes, StandardCharsets.UTF_8))) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                if (line.startsWith("allow:")) {
                    HasAllowTag = true;
                    continue;
                }

                if (line.contains("depOnAnyVendor:")) {
                    HasDependOnVendorTag = true;
                    continue;
                }

                if (line.startsWith("components:")) {
                    HasComponentsTag = true;
                    continue;
                }

                if (line.startsWith("deps:")) {
                    HasDependenciesTag = true;
                    continue;
                }

                if (HasComponentsTag && HasDependenciesTag) {
                    break;
                }

                if (HasAllowTag && HasDependOnVendorTag) {
                    break;
                }
            }
        }

        return (HasAllowTag && HasDependOnVendorTag) || (HasComponentsTag && HasDependenciesTag);
    }
}
