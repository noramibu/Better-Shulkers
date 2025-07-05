package me.noramibu.mixin;

import com.google.common.base.Suppliers;
import com.google.common.reflect.ClassPath;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class BetterShulkersMixinPlugin implements IMixinConfigPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger("BetterShulkersMixinPlugin");
    private static final String MCVER_DESC = "Lme/noramibu/mixin/annotation/MCVer;";

    private final Supplier<Version> gameVersion = Suppliers.memoize(() ->
            FabricLoader.getInstance().getModContainer("minecraft")
                    .map(mod -> mod.getMetadata().getVersion())
                    .orElse(null)
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            ClassNode mixinClassNode = getClassNode(mixinClassName);
            if (mixinClassNode.visibleAnnotations == null) {
                LOGGER.info("Applying mixin " + mixinClassName + " because it has no version annotations.");
                return true;
            }

            for (AnnotationNode annotation : mixinClassNode.visibleAnnotations) {
                if (MCVER_DESC.equals(annotation.desc)) {
                    String min = null;
                    String max = null;

                    if (annotation.values != null) {
                        for (int i = 0; i < annotation.values.size(); i += 2) {
                            String key = (String) annotation.values.get(i);
                            Object value = annotation.values.get(i + 1);
                            if ("min".equals(key)) {
                                min = (String) value;
                            } else if ("max".equals(key)) {
                                max = (String) value;
                            }
                        }
                    }

                    Version currentVersion = gameVersion.get();
                    if (currentVersion == null) return false;

                    boolean minOk = true;
                    if (min != null && !min.isEmpty()) {
                        minOk = currentVersion.compareTo(Version.parse(min)) >= 0;
                    }

                    boolean maxOk = true;
                    if (max != null && !max.isEmpty()) {
                        maxOk = currentVersion.compareTo(Version.parse(max)) <= 0;
                    }

                    boolean shouldApply = minOk && maxOk;
                    LOGGER.info("Mixin " + mixinClassName + ": min=" + min + ", max=" + max + ". Current version " + currentVersion + ". Apply? " + shouldApply);
                    return shouldApply;
                }
            }

        } catch (IOException | VersionParsingException e) {
            e.printStackTrace();
            return false;
        }
        LOGGER.info("Applying mixin " + mixinClassName + " because it has no @MCVer annotation.");
        return true;
    }

    private ClassNode getClassNode(String mixinClassName) throws IOException {
        String classPath = mixinClassName.replace('.', '/') + ".class";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(classPath)) {
            if (is == null) {
                throw new IOException("Could not find class file for " + mixinClassName);
            }
            ClassReader reader = new ClassReader(is);
            ClassNode node = new ClassNode();
            reader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            return node;
        }
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
} 