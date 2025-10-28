package me.dexrn.rrdiscordbridge.fabric.plugins;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.fabric.multiversion.FabricMods;
import me.dexrn.rrdiscordbridge.fabric.multiversion.FabricModsFactory;

import net.fabricmc.loader.api.FabricLoader;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VersionMixinPlugin implements IMixinConfigPlugin {
    private Semver version;
    private FabricMods mod;

    @Override
    public void onLoad(String mixinPackage) {
        FabricLoader.getInstance()
                .getModContainer("minecraft")
                .ifPresent(
                        mc -> {
                            version =
                                    new Semver(
                                            mc.getMetadata().getVersion().getFriendlyString(),
                                            Semver.SemverType.LOOSE);

                            FabricModsFactory factory = new FabricModsFactory();
                            mod = factory.getFabricMods(version);

                            RRDiscordBridge.logger.info(
                                    "[RRDiscordBridge/VersionMixinPlugin] Using MC version %s (%s) for conditional mixins",
                                    version, mod.name());
                        });
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        for (String mixin : mod.getSupportedMixins())
            if (mixinClassName.endsWith(mixin)) return true;

        return false;
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
