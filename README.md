# YedelMod

![Badge](https://img.shields.io/badge/discord-yedel-blue)

![GitHub Release](https://img.shields.io/github/v/release/Yedelo/YedelMod?label=GitHub%20version)

![Modrinth Version](https://img.shields.io/modrinth/v/oYw9EG5g?label=Modrinth%20version)

Use /yedel (/yedelmod) or the config button in the mod menu for settings and more info.
Use /yedel update or the check for update button in the settings menu to check for updates on Modrinth or GitHub.
Use /yedel yedelmessage for messages from me regarding the mod. These are usually tips or bug notices.

From version 1.2.0, this mod requires the Hypixel Mod API which can be found [here](https://modrinth.com/mod/hypixel-mod-api). If you do not have the mod, a popup window will appear with instructions to install it.

# Features

<details><summary>Auto welcome guild members</summary>

- Automatically welcomes new guild members with a customizable message.

</details>

<details><summary>Custom hit particles</summary>

- Spawns customizable particles when hitting entities.

</details>

<details><summary>Display text</summary>

- Show text which can be customized with /yedel settext and cleared with /yedel cleartext, supporting color codes with
  ampersands (&).

</details>

<details><summary>Dropper AutoGG</summary>

- AutoGG for dropper, will be removed when it is added to Sk1er's AutoGG.
- Note: This only says gg at the end of the game, not when you finish.

</details>

<details><summary>Regex chat filter</summary>

- Use a customizable regular expression to filter chat.

</details>

<details><summary>Random placeholder</summary>

- Type a customizable placeholder to replace it with a random string from a UUID.

</details>

<details><summary>SkyWars strength indicators</summary>

- Shows people's strength above their nametags with customizable colors. Accounts for Apothecary.

</details>

<details><summary>Limbo creative mode</summary>

- Automatically gives creative mode in Hypixel limbo, not bannable because the server does not listen to anything
  happening.
- Use /yedel lgmc in limbo if it doesn't work the first time.

</details>

<details><summary>Favorite server button</summary>

- Adds a button to the main menu to join a customizable server address.

</details>

### Modern Features

(Client-side) features from newer versions of the game.

<details><summary>Book background (1.14+)</summary>

- Draws the default dark background in book GUIs.

</details>

<details><summary>Keep chat history on chat clear (1.15.2+)</summary>

- When clearing your chat (F3 + D), keep your message history (from pressing up arrow key).

</details>

<details><summary>Change window title (1.15.2+)</summary>

- Changes the window title on world and server join.
- You can manually do this with /yedel settitle.

</details>

<details><summary>Damage tilt (1.19.4+)</summary>

- Allows you to customize how much your screen hurts when being damaged.

</details>

<details><summary>Hand swings (1.15+)</summary>

- Swing your hand when doing numerous actions, such as dropping items or using items like snowballs and armor.

</details>

### Tweaks

Smaller features that change the game, similar to features in Patcher.

<details><summary>Unformat chat logs</summary>

- Removes leftover formatting from chat messages in logs.

</details>

<details><summary>Hide missing signature errors</summary>

- Hides "Signature is missing from textures payload" errors from logs.

</details>

### BedWars

#### HUDs

<details><summary>XP display</summary>

- Shows your experience out of 5,000.
- Inaccurate for lower levels (0-4).

</details>

<details><summary>Magic milk display</summary>

- Shows how long your Magic Milk will last for.

</details>

#### Challenges

<details><summary>BedWars defusal helper</summary>

- Highlights redstone for the BedWars defusal challenge.

</details>

#### Chat

<details><summary>Light green token messages</summary>

- Make token messages light green instead of green (only in bedwars) to make them appear different from emerald
  messages.

</details>

<details><summary>Hide slumber ticket messages</summary>

- Hide slumber ticket messages in-game from things like kills and wins.

</details>

<details><summary>Hide item pickup messages</summary>

- Hide "_You picked up: ..._" messages.

</details>

<details><summary>Hide silver coin count</summary>

- Hide the silver coin count from item purchase messages.

</details>

<details><summary>Hide comfy pillow messages</summary>

- Hides the following messages:
  - "You are now carrying x1 Comfy Pillows, bring it back to your shop keeper!"
  - "You cannot return items to another team's Shopkeeper!"
  - "You cannot carry any more Comfy Pillows!"
  - "You died while carrying 1x Comfy Pillows!"

</details>

<details><summary>Hide dreamer's soul fragment messages</summary>

- Hide "+1 Dreamer's Soul Fragment!" messages.

</details>

### TNT Tag

<details><summary>Bounty hunting</summary>

- Adds a bounty hunting minigame to TNT Tag. This feature is complicated,
  watch [my video](https://www.youtube.com/watch?v=-z_AZR35ozI) if you need help!

</details>

# Commands

Format: - subcommand (aliases) [arguments]
All commands are under the /yedel command. For example:

- settext [text] -> /yedel settext [text]

<details><summary>/yedel (yedelmod)</summary>

- The main command, hosting all subcommands. When used with no arguments, opens the config screen.

</details>

<details><summary>-cleartext</summary>

- Clears the currently set display text.

</details>

<details><summary>-formatting</summary>

- Shows a formatting guide with color and style codes.

</details>

<details><summary>-limbo (li)</summary>

- Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some.
- No longer works on Hypixel, use /limbo instead.

</details>

<details><summary>- limbocreative (limbogmc, lgmc) </summary>

- Gives you creative mode in Hypixel's limbo, given certain checks are passed.

</details>

<details><summary>- movehud</summary>

- Opens the HUD customization screen.

</details>

<details><summary>- ping [method]</summary>

- Shows your ping to the server in chat, using several methods. Without an argument, uses the default method which can
  be customized.

</details>

<details><summary>- playtime (pt)</summary>

- Shows your total playtime (while playing on servers) in hours and minutes.

</details>

<details><summary>- setnick [nick]</summary>

- Sets your nick for Bounty Hunting to not select yourself as the target.

</details>

<details><summary>-settext [text]</summary>

- Sets the display text, supporting color codes with ampersands (&).

</details>

<details><summary>-settitle [title]</summary>

- Sets the title of the game window.

</details>

<details><summary>-simulatechat (simc) [text]</summary>

- Simulates a chat message, also supports color codes with ampersands (&).

</details>

<details><summary>-update [platform]</summary>

- Checks for mod updates. Without an argument, uses the default one (modrinth). Possible platforms are "modrinth" or "
  github".

</details>

<details><summary>- yedelmessage (message)</summary>

- Shows messages from me about the mod. These can be anything from tips to bug notices.

</details>

# Keybinds

<details><summary>Market searches</summary>

- Adds keybinds to search the auction house or bazaar for your held item.
- Note that you may need to change categories when searching on the auction house.
- Bound to L by default.

</details>

<details><summary>Easy atlas verdicts</summary>

- Adds keybinds for the two atlas verdicts in your hotbar. This automatically clicks for you, so it is use at your own
  risk.
- Insufficient Evidence: Bound to O by default.
- Evidence Without Doubt: Bound to P by default.

</details>







