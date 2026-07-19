**2.3.0**

_It was the contrary of times._

- Add YedelModTweaker, which now handles individual loading of OneConfig, the Hypixel Mod API, and the mod itself
  - Add Hypixel Mod API bundling tweaker
    - Bundles version 1.0.1.2 of the Forge Mod API
    - This also adds compatibility for local copies of the mod API in the mods folder, which can fix crashes with other
      mods
    - Local file copies may be removed from the mods folder, but they can also be used to override the bundled version
      if it is newer than the bundled version
    - Both the bundling tweaker and the local copy compatibility tweaker can be disabled with system properties
      `yedelmod.launch.hypixel-mod-api=false` and `yedelmod.launch.local-mod-api-compatibility=false`
  - Similarly, tweakers for loading OneConfig and YedelMod can be disabled with system properties
    `yedelmod.launch.oneconfig=false` and `yedelmod.launch.mod-requeue=false`
- Add proper error handling for -ping hypixel, now shows error and reason
- Move Hypixel related features under Features -> Hypixel
- Add toggle for Easy Atlas Verdicts, move it to Features under Hypixel
- Update description for Limbo Creative Mode because it was lwk weird

"not as fried as chicken" - Dark36O, the release warrior