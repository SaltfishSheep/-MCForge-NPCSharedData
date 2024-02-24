NPCSharedData mainly implements two functions:

#NPC Aspect

Shared among all players are the progress of tasks, conversation progress, and faction points.

At any given time, only one player can engage in conversation with an NPC.

Chat records from dialog boxes will be synchronized to the chat box (allowing players without ongoing dialogues to see them).

#Command Aspect (can be skipped if not needed, this is a technical note to prevent unknown bugs for developers)

There are certain issues in the command design of NPCs. When using "@a" to select a player, it will fail to select (in reality, it selects only one player, but the implementation method does not support selecting with "@a").

Therefore, NPCSharedData incidentally modified MC's matchOnePlayer method to make it return the first player when "@a" is passed as input.

#Future Tasks

Update to 1.12.2, or even 1.16.5/1.20,

Recently, I'm preparing a major exam that influences my future, so if you like this mod, you have to wait for a long time.

Maybe 4 months?To the June maybe.
