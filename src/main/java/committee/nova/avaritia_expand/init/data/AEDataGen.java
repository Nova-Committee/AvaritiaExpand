package committee.nova.avaritia_expand.init.data;

import committee.nova.avaritia_expand.init.data.provider.AEBlockTags;
import committee.nova.avaritia_expand.init.data.provider.AERecipes;
import committee.nova.avaritia_expand.init.data.provider.loot.AELootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class AEDataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        if (event.includeClient()) {

        }
        if (event.includeServer()) {
            generator.addProvider(true, new AERecipes(output, lookupProvider));
            generator.addProvider(true, new AELootTables(output, lookupProvider));
            generator.addProvider(true, new AEBlockTags(output, lookupProvider, helper));
        }
    }
}
