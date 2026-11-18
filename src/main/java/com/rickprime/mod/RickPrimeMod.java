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

    // Configuración inicial del mod (Capa de animación)
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

    // Detector de eventos del juego en el lado del cliente (como recibir daño)
    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ForgeClientEvents {
        @SubscribeEvent
        public static void onPlayerHurt(LivingHurtEvent event) {
            // Verifica si el entity que recibe daño es el jugador local
            if (Minecraft.getInstance().player != null && event.getEntity().getId() == Minecraft.getInstance().player.getId()) {
                // Reproduce automáticamente la animación de daño o impacto
                playAnimation("animation10");
            }
        }
    }

    // Método reutilizable para activar cualquier animación por nombre
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
```[cite: 14]

### Qué cambia con esto:
1. Se añade la clase `ForgeClientEvents` con la anotación de eventos de Forge en el cliente.
2. El evento `LivingHurtEvent` se dispara exactamente en el momento en que el jugador sufre cualquier tipo de daño en el juego.
3. Al detectarlo, llama de inmediato a `playAnimation("animation10")`, haciendo que Rick Prime reaccione con la animación que diseñaste en Blockbench sin que tengas que presionar ninguna tecla. 

Si actualizas este código en tu repositorio de GitHub, la siguiente compilación incluirá la activación automática por daño.
