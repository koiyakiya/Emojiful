package com.hrznstudio.emojiful.datapack;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Objects;

public class EmojiRecipeSerializer implements RecipeSerializer<EmojiRecipe> {

    private final StreamCodec<RegistryFriendlyByteBuf, EmojiRecipe> codec;
    private final MapCodec<EmojiRecipe> mapCodec;

    public EmojiRecipeSerializer() {
        this.codec = new StreamCodec<RegistryFriendlyByteBuf, EmojiRecipe>() {
            @Override
            public EmojiRecipe decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
                return null;
            }

            @Override
            public void encode(RegistryFriendlyByteBuf o, EmojiRecipe emojiRecipe) {

            }
        };
        this.mapCodec = RecordCodecBuilder.mapCodec(instance -> {
            var test = instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(Recipe::getGroup),
                    Codec.STRING.fieldOf("category").forGetter(EmojiRecipe::getCategory),
                    Codec.STRING.fieldOf("name").forGetter(EmojiRecipe::getName),
                    Codec.STRING.fieldOf("url").forGetter(EmojiRecipe::getUrl));
            return test.apply(instance, (s, s2, s3, s4) -> new EmojiRecipe(s2, s3, s4));
        });
    }


    @Override
    public MapCodec<EmojiRecipe> codec() {
        return mapCodec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, EmojiRecipe> streamCodec() {
        return this.codec;
    }



}
