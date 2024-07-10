package com.hrznstudio.emojiful;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.hrznstudio.emojiful.datapack.EmojiRecipe;
import com.hrznstudio.emojiful.datapack.EmojiRecipeSerializer;
import com.hrznstudio.emojiful.platform.ForgeConfigHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


@Mod(Constants.MOD_ID)
public class EmojifulNeoForge {

    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);
    public static final DeferredHolder<RecipeSerializer<?>, EmojiRecipeSerializer> EMOJI_RECIPE_SERIALIZER = RECIPE_SER.register("emoji_recipe", EmojiRecipeSerializer::new);

    public static DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MOD_ID);
    public static final DeferredHolder<RecipeType<?>, RecipeType<EmojiRecipe>> EMOJI_RECIPE_TYPE = RECIPE_TYPE.register("emoji_recipe_type", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "emoji_recipe_type")));

    public EmojifulNeoForge(Dist dist, IEventBus modBus, ModContainer container) {
        RECIPE_SER.register(modBus);
        RECIPE_TYPE.register(modBus);
        createAndLoadConfigs(container, ModConfig.Type.CLIENT, ForgeConfigHelper.setup(new ModConfigSpec.Builder()), "emojiful-client.toml");
        modBus.addListener(this::handleClientSetup);
    }

    private void handleClientSetup(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(ForgeClientHandler::onRecipesUpdated);
        NeoForge.EVENT_BUS.addListener(ForgeClientHandler::hijackScreen);
    }

    private static void createAndLoadConfigs(ModContainer modContainer, ModConfig.Type type, ModConfigSpec spec, String path) {
        modContainer.registerConfig(type, spec, path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(path))
                .preserveInsertionOrder()
                .autoreload()
                .writingMode(WritingMode.REPLACE)
                .sync()
                .build();

        configData.load();
        spec.setConfig(configData);
    }

}