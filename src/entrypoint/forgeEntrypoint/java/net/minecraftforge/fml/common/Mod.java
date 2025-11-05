package net.minecraftforge.fml.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** stub to allow for 1.12 forge to load the mod (thanks tater for helping) */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Mod {
    String value();

    String modid();

    String name();

    String version();

    String acceptableRemoteVersions();

    boolean serverSideOnly();

    @interface EventHandler {}
}
