package com.hrznstudio.emojiful.util;

import com.hrznstudio.emojiful.api.Emoji;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;

public class EmojiUtil {

    public static RenderType createRenderType(Emoji emoji) {
        RenderType.State state = RenderType.State.getBuilder().texture(new RenderState.TextureState(emoji.getResourceLocationForBinding(), false, false)).transparency(new RenderState.TransparencyState("translucent_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }, () -> {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            RenderSystem.disableBlend();
        })).build(true);
        return RenderType.makeType("portal_render", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, false, true, state);
    }

    public static float renderEmoji(Emoji emoji, float x, float y, Matrix4f matrix, IRenderTypeBuffer buffer, int packedLight) {
        float textureSize = 16;
        float textureX = 0 / textureSize;
        float textureY = 0 / textureSize;
        float textureOffset = 16.0F / textureSize;
        float size = 10f;
        float offsetY = 1.0F;
        float offsetX = 0.0F;

        IVertexBuilder builder = buffer.getBuffer(createRenderType(emoji));

        builder.pos(matrix, x - offsetX, y - offsetY, 0.0f).color(255, 255, 255, 255).tex(textureX, textureY).lightmap(packedLight).endVertex();
        builder.pos(matrix, x - offsetX, y + size - offsetY, 0.0F).color(255, 255, 255, 255).tex(textureX, textureY + textureOffset).lightmap(packedLight).endVertex();
        builder.pos(matrix, x - offsetX + size, y + size - offsetY, 0.0F).color(255, 255, 255, 255).tex(textureX + textureOffset, textureY + textureOffset).lightmap(packedLight).endVertex();
        builder.pos(matrix, x - offsetX + size, y - offsetY, 0.0F).color(255, 255, 255, 255).tex(textureX + textureOffset, textureY / textureSize).lightmap(packedLight).endVertex();
        return 10f;
    }

}
