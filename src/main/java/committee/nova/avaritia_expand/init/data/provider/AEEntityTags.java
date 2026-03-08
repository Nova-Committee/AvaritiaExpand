package committee.nova.avaritia_expand.init.data.provider;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.mods.avaritia.init.registry.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AEEntityTags extends EntityTypeTagsProvider {
    public AEEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, AvaritiaExpand.MOD_ID, existingFileHelper);
    }

    public @NotNull String getName() {
        return "AvaritiaExpand Entity Type Tags";
    }

    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES)
                .add(AEEntities.BLAZE_WIND_CHARGE.get())
                .add(AEEntities.CRYSTAL_WIND_CHARGE.get())
                .add(AEEntities.EXTREME_WITHER_SKULL.get())
                .add(ModEntities.BLADE_SLASH.get())
                .add(ModEntities.HEAVEN_ARROW.get())
                .add(ModEntities.BURNING_ARROW.get())
                .add(ModEntities.TRACE_ARROW.get())

        ;
    }
}
