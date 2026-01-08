package committee.nova.avaritia_expand.init.registry;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class AEConfig {

    public static final ModConfigSpec COMMON;

    public static final ModConfigSpec.IntValue infinityTntMaxExplosionTime;
    public static final ModConfigSpec.LongValue infinityTntMaxRange;

    public static final ModConfigSpec.IntValue neutronSwordMaxRange;



    static {
        final var common = new ModConfigSpec.Builder();
        common.comment("Avaritia Expand Common Config");
        common.push("blocks");
        infinityTntMaxExplosionTime = buildInt(common, "infinity_tnt_max_explosion_time", 8000, 0, Integer.MAX_VALUE, "Recommended setting is 2000-8000");
        infinityTntMaxRange = buildLong(common, "infinity_tnt_max_range", 256, 0, Long.MAX_VALUE, "The maximum range of the Infinity TNT");
        common.pop();
        neutronSwordMaxRange = buildInt(common, "neutron_sword_max_range", 64, 0, 64, "The maximum teleportation distance of the Neutronium Sword");
        common.push("tools");

        common.pop();
        COMMON = common.build();
    }




    public static void register(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, AEConfig.COMMON);
    }


    private static ModConfigSpec.BooleanValue buildBoolean(ModConfigSpec.Builder builder, String name, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ModConfigSpec.IntValue buildInt(ModConfigSpec.Builder builder, String name, int defaultValue, int min, int max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ModConfigSpec.DoubleValue buildDouble(ModConfigSpec.Builder builder, String name, double defaultValue, double min, double max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ModConfigSpec.LongValue buildLong(ModConfigSpec.Builder builder, String name, long defaultValue, long min, long max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
