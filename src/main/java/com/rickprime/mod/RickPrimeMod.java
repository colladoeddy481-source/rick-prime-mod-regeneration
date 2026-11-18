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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ForgeClientEvents {
        @SubscribeEvent
        public static void onPlayerHurt(LivingHurtEvent event) {
            if (Minecraft.getInstance().player != null && event.getEntity().getId() == Minecraft.getInstance().player.getId()) {
                float health = Minecraft.getInstance().player.getHealth();
                float maxHealth = Minecraft.getInstance().player.getMaxHealth();
                float healthPercent = health / maxHealth;

                if (healthPercent <= 0.2f) {
                    playAnimation("animation10");
                } else if (healthPercent <= 0.5f) {
                    playAnimation("animation8");
                } else {
                    String[] lightHits = {"animation", "animation2", "animation3", "animation4", "animation5"};
                    int randomIdx = (int) (Math.random() * lightHits.length);
                    playAnimation(lightHits[randomIdx]);
                }
            }
        }
    }

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
