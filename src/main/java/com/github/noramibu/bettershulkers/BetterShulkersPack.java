/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.FileUtil;
import org.jspecify.annotations.Nullable;

// TODO Make this its own separate cross-plat library mod

/**
 * Super fancy custom Data loader for all mod loaders
 */
public class BetterShulkersPack extends AbstractPackResources {
    private static final Set<String> KNOWN_SERVER_NAMESPACES = Set.of("minecraft", BetterShulkers.MOD_ID);

    private final ClassLoader classLoader;
    private final @Nullable Path codeSource;

    public BetterShulkersPack(PackLocationInfo locationInfo) {
        super(locationInfo);
        this.classLoader = BetterShulkers.class.getClassLoader();
        this.codeSource = findCodeSource();
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... path) {
        FileUtil.validatePath(path);
        return resourceSupplier(String.join("/", path));
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType type, Identifier location) {
        String path = type.getDirectory() + "/" + location.getNamespace() + "/" + location.getPath();
        return resourceSupplier(path);
    }

    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput output) {
        String namespaceRoot = type.getDirectory() + "/" + namespace + "/";
        String searchPath = namespaceRoot + directoryPath(path);
        Set<String> seen = new HashSet<>();

        listFromClasspath(searchPath, namespaceRoot, namespace, output, seen);
        listFromCodeSource(searchPath, namespaceRoot, namespace, output, seen);
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        Set<String> namespaces = new LinkedHashSet<>();
        String typeRoot = type.getDirectory() + "/";

        collectNamespacesFromClasspath(typeRoot, namespaces);
        collectNamespacesFromCodeSource(typeRoot, namespaces);

        if (type == PackType.SERVER_DATA) {
            namespaces.addAll(KNOWN_SERVER_NAMESPACES);
        } else if (type == PackType.CLIENT_RESOURCES) {
            namespaces.add(BetterShulkers.MOD_ID);
        }

        return namespaces;
    }

    @Override
    public void close() {}

    private @Nullable IoSupplier<InputStream> resourceSupplier(String path) {
        String resourcePath = stripLeadingSlash(path);
        if (this.classLoader.getResource(resourcePath) == null) {
            return null;
        }

        return () -> {
            InputStream stream = this.classLoader.getResourceAsStream(resourcePath);
            if (stream == null) {
                throw new FileNotFoundException(resourcePath);
            }
            return stream;
        };
    }

    private void listFromClasspath(
            String searchPath, String namespaceRoot, String namespace, ResourceOutput output, Set<String> seen) {
        try {
            Enumeration<URL> urls = this.classLoader.getResources(searchPath);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                listFromUrl(url, searchPath, namespaceRoot, namespace, output, seen);
            }
        } catch (IOException e) {
            BetterShulkers.LOGGER.warn("Failed to list Better Shulkers internal pack path {}", searchPath, e);
        }
    }

    private void listFromCodeSource(
            String searchPath, String namespaceRoot, String namespace, ResourceOutput output, Set<String> seen) {
        Path source = this.codeSource;
        if (source == null) {
            return;
        }

        if (Files.isDirectory(source)) {
            listFromDirectory(source.resolve(searchPath), searchPath, namespaceRoot, namespace, output, seen);
            return;
        }

        if (Files.isRegularFile(source)) {
            listFromZip(source, searchPath, namespaceRoot, namespace, output, seen);
        }
    }

    private void listFromUrl(
            URL url,
            String searchPath,
            String namespaceRoot,
            String namespace,
            ResourceOutput output,
            Set<String> seen) {
        try {
            if ("file".equals(url.getProtocol())) {
                listFromDirectory(Path.of(url.toURI()), searchPath, namespaceRoot, namespace, output, seen);
            } else if ("jar".equals(url.getProtocol())) {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                try (ZipFile zipFile = new ZipFile(Path.of(connection.getJarFileURL().toURI()).toFile())) {
                    listFromZip(zipFile, searchPath, namespaceRoot, namespace, output, seen);
                }
            }
        } catch (IOException | URISyntaxException e) {
            BetterShulkers.LOGGER.warn("Failed to list Better Shulkers internal pack URL {}", url, e);
        }
    }

    private void listFromDirectory(
            Path directory,
            String searchPath,
            String namespaceRoot,
            String namespace,
            ResourceOutput output,
            Set<String> seen) {
        if (!Files.isDirectory(directory)) {
            return;
        }

        try (Stream<Path> files = Files.find(directory, Integer.MAX_VALUE, (file, attributes) -> attributes.isRegularFile())) {
            files.forEach(file -> {
                String relativePath = toResourcePath(directory.relativize(file));
                acceptResource(searchPath + relativePath, namespaceRoot, namespace, output, seen);
            });
        } catch (IOException e) {
            BetterShulkers.LOGGER.warn("Failed to list Better Shulkers internal pack directory {}", directory, e);
        }
    }

    private void listFromZip(
            Path zipPath, String searchPath, String namespaceRoot, String namespace, ResourceOutput output, Set<String> seen) {
        try (ZipFile zipFile = new ZipFile(zipPath.toFile())) {
            listFromZip(zipFile, searchPath, namespaceRoot, namespace, output, seen);
        } catch (IOException e) {
            BetterShulkers.LOGGER.warn("Failed to list Better Shulkers internal pack jar {}", zipPath, e);
        }
    }

    private void listFromZip(
            ZipFile zipFile, String searchPath, String namespaceRoot, String namespace, ResourceOutput output, Set<String> seen) {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (!entry.isDirectory() && entryName.startsWith(searchPath)) {
                acceptResource(entryName, namespaceRoot, namespace, output, seen);
            }
        }
    }

    private void acceptResource(
            String resourcePath, String namespaceRoot, String namespace, ResourceOutput output, Set<String> seen) {
        if (!seen.add(resourcePath)) {
            return;
        }

        String idPath = resourcePath.substring(namespaceRoot.length());
        Identifier id = Identifier.tryBuild(namespace, idPath);
        IoSupplier<InputStream> supplier = resourceSupplier(resourcePath);
        if (id != null && supplier != null) {
            output.accept(id, supplier);
        } else if (id == null) {
            BetterShulkers.LOGGER.warn("Invalid path in Better Shulkers internal pack: {}:{}, ignoring", namespace, idPath);
        }
    }

    private void collectNamespacesFromClasspath(String typeRoot, Set<String> namespaces) {
        try {
            Enumeration<URL> urls = this.classLoader.getResources(stripTrailingSlash(typeRoot));
            while (urls.hasMoreElements()) {
                collectNamespacesFromUrl(urls.nextElement(), typeRoot, namespaces);
            }
        } catch (IOException e) {
            BetterShulkers.LOGGER.warn("Failed to list Better Shulkers internal pack namespaces under {}", typeRoot, e);
        }
    }

    private void collectNamespacesFromCodeSource(String typeRoot, Set<String> namespaces) {
        Path source = this.codeSource;
        if (source == null) {
            return;
        }

        if (Files.isDirectory(source)) {
            collectNamespacesFromDirectory(source.resolve(stripTrailingSlash(typeRoot)), namespaces);
            return;
        }

        if (Files.isRegularFile(source)) {
            try (ZipFile zipFile = new ZipFile(source.toFile())) {
                collectNamespacesFromZip(zipFile, typeRoot, namespaces);
            } catch (IOException e) {
                BetterShulkers.LOGGER.warn("Failed to list Better Shulkers internal pack namespaces in {}", source, e);
            }
        }
    }

    private void collectNamespacesFromUrl(URL url, String typeRoot, Set<String> namespaces) {
        try {
            if ("file".equals(url.getProtocol())) {
                collectNamespacesFromDirectory(Path.of(url.toURI()), namespaces);
            } else if ("jar".equals(url.getProtocol())) {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                try (ZipFile zipFile = new ZipFile(Path.of(connection.getJarFileURL().toURI()).toFile())) {
                    collectNamespacesFromZip(zipFile, typeRoot, namespaces);
                }
            }
        } catch (IOException | URISyntaxException e) {
            BetterShulkers.LOGGER.warn("Failed to list Better Shulkers internal pack namespace URL {}", url, e);
        }
    }

    private static void collectNamespacesFromDirectory(Path directory, Set<String> namespaces) {
        if (!Files.isDirectory(directory)) {
            return;
        }

        try (Stream<Path> paths = Files.list(directory)) {
            paths.filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .filter(Identifier::isValidNamespace)
                    .forEach(namespaces::add);
        } catch (IOException e) {
            BetterShulkers.LOGGER.warn("Failed to list Better Shulkers internal pack namespace directory {}", directory, e);
        }
    }

    private static void collectNamespacesFromZip(ZipFile zipFile, String typeRoot, Set<String> namespaces) {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            String entryName = entries.nextElement().getName();
            if (!entryName.startsWith(typeRoot)) {
                continue;
            }

            int namespaceEnd = entryName.indexOf('/', typeRoot.length());
            if (namespaceEnd <= typeRoot.length()) {
                continue;
            }

            String namespace = entryName.substring(typeRoot.length(), namespaceEnd);
            if (Identifier.isValidNamespace(namespace)) {
                namespaces.add(namespace);
            }
        }
    }

    private static @Nullable Path findCodeSource() {
        try {
            URL location = BetterShulkers.class.getProtectionDomain().getCodeSource().getLocation();
            return location == null || !"file".equals(location.getProtocol()) ? null : Path.of(location.toURI());
        } catch (SecurityException | URISyntaxException e) {
            BetterShulkers.LOGGER.warn("Failed to resolve Better Shulkers code source", e);
            return null;
        }
    }

    private static String directoryPath(String path) {
        if (path.isEmpty() || path.endsWith("/")) {
            return path;
        }
        return path + "/";
    }

    private static String stripLeadingSlash(String path) {
        return path.startsWith("/") ? path.substring(1) : path;
    }

    private static String stripTrailingSlash(String path) {
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }

    private static String toResourcePath(Path path) {
        return path.toString().replace('\\', '/');
    }
}
