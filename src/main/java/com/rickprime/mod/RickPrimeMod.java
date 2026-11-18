package com.rickprime.mod;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(RickPrimeMod.MODID)
public class RickPrimeMod {
    public static final String MODID = "rickprime";

    public RickPrimeMod() {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                new ResourceLocation(MODID, "animation_layer"),
                42,
                RickPrimeMod::registerPlayerAnimation
            );
        }
    }

    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        return new ModifierLayer<>();
    }

    /**
     * Llama a este metodo pasando el nombre exacto de cualquiera de tus archivos:
     * "animation", "animation2", "animation3", "animation4", "animation5",
     * "animation7", "animation8", "animation9", "animation10"
     */
    public static void playAnimation(String animName) {
        if (Minecraft.getInstance().player == null) return;
        var animationContainer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player)
                .get(new ResourceLocation(MODID, "animation_layer"));

        if (animationContainer != null) {
            var anim = PlayerAnimationRegistry.getAnimation(new ResourceLocation(MODID, animName));
            if (anim != null) {
                animationContainer.setAnimation(new KeyframeAnimationPlayer(anim));
            }
        }
    }
}
