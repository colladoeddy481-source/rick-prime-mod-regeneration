// Detector de eventos del juego en el lado del cliente
    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ForgeClientEvents {
        @SubscribeEvent
        public static void onPlayerHurt(LivingHurtEvent event) {
            if (Minecraft.getInstance().player != null && event.getEntity().getId() == Minecraft.getInstance().player.getId()) {
                float health = Minecraft.getInstance().player.getHealth();
                float maxHealth = Minecraft.getInstance().player.getMaxHealth();
                float healthPercent = health / maxHealth;

                // Selecciona la animación exacta según la gravedad del daño o estado
                if (healthPercent <= 0.2f) {
                    // Daño crítico / Estado abatido (animaciones finales como animation10 o animation9)
                    playAnimation("animation10");
                } else if (healthPercent <= 0.5f) {
                    // Daño moderado / Tropiezo (animation7 u animation8)
                    playAnimation("animation8");
                } else {
                    // Golpes rápidos o reacciones de extremidades (animation, animation2, animation3, etc.)
                    // Puedes alternar entre las animaciones de brazos y piernas
                    String[] lightHits = {"animation", "animation2", "animation3", "animation4", "animation5"};
                    int randomIdx = (int) (Math.random() * lightHits.length);
                    playAnimation(lightHits[randomIdx]);
                }
            }
        }
    }
```[cite: 12]

### Verificación:
* **Con este cambio:** Cuando recibas un golpe leve, el mod elegirá al azar una de las animaciones individuales de extremidades (`animation` a `animation5`). Si tu vida baja a la mitad, activará las posturas de tropiezo (`animation7` u `animation8`), y si estás a punto de morir, activará la postura completa en el suelo (`animation10`)[cite: 3, 4, 5, 8].
* Sube esta actualización a tu repositorio de GitHub para que la compilación automática en **Actions** genere el `.jar` con el comportamiento mapeado[cite: 11, 13].
