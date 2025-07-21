package io.github.dexrnzacattack.rrdiscordbridge.plugins;

import com.vdurmont.semver4j.Semver;

import net.fabricmc.loader.api.FabricLoader;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VersionMixinPlugin implements IMixinConfigPlugin {

    private String version = "";

    @Override
    public void onLoad(String mixinPackage) {
        FabricLoader.getInstance()
                .getModContainer("minecraft")
                .ifPresent(
                        mc -> {
                            version = mc.getMetadata().getVersion().getFriendlyString();
                            System.out.println(
                                    "[RRDiscordBridge] Using MC version "
                                            + version
                                            + " for conditional mixins");
                        });
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith("OnAdvancementAwardMixin")) {
            return new Semver("1.20.1", Semver.SemverType.LOOSE).isLowerThan(version);
        }

        if (mixinClassName.endsWith("On1201AdvancementAwardMixin")
                || mixinClassName.endsWith("OnPlayerCommandMixin")
                || mixinClassName.endsWith("OnChatMessageMixin")
                || mixinClassName.endsWith("OnConsoleCommandMixin")) {
            return new Semver("1.20.2", Semver.SemverType.LOOSE).isGreaterThan(version);
        }

        return true;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(
            String targetClassName,
            ClassNode targetClass,
            String mixinClassName,
            IMixinInfo mixinInfo) {}

    @Override
    public void postApply(
            String targetClassName,
            ClassNode targetClass,
            String mixinClassName,
            IMixinInfo mixinInfo) {}

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
}
