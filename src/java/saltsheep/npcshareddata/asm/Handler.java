package saltsheep.npcshareddata.asm;

import foxz.command.CmdQuest;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.*;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.scripted.ScriptPlayer;
import saltsheep.npcshareddata.data.SheepDialogData;
import saltsheep.npcshareddata.data.SheepFactionData;
import saltsheep.npcshareddata.data.SheepQuestData;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Handler {

    public static EntityPlayer lastDialogPlayer = null;
    //*[x,y,z,yaw,pitch]
    public static double[] lastInfo = {0,0,0,0,0};

    public static void onPlayerDataInit(PlayerData data){
        data.dialogData = SheepDialogData.instance;
        data.factionData = SheepFactionData.instance;
        data.questData = SheepQuestData.instance;
    }

    public static void onOpenDialog(EntityPlayer player, EntityNPCInterface npc, Dialog dia){
        Server.sendToAll(EnumPacketClient.CHAT, new Object[]{npc.display.name +":"+dia.text});
        if(!dia.options.isEmpty()){
            int i = 0;
            for(Map.Entry<Integer,DialogOption> option:dia.options.entrySet()){
                i++;
                Server.sendToAll(EnumPacketClient.CHAT, new Object[]{String.valueOf(i)+":"+option.getValue().title});
            }
        }
    }

    public static void onDialogSelected(int dialogId, int optionId, EntityPlayerMP player, EntityNPCInterface npc) {
        Dialog dialog = DialogController.instance.dialogs.get(dialogId);
        if (dialog != null) {
            if (dialog.hasDialogs(player) || dialog.hasOtherOptions()) {
                DialogOption option = dialog.options.get(optionId);
                if(option!=null){
                    Server.sendToAll(EnumPacketClient.CHAT,new Object[]{"saltsheep.common.youself",":"+option.title});
                }
            }
        }
    }

    public static void addActiveQuestForced(Quest quest) {
        if(SheepQuestData.instance.activeQuests.containsKey(quest.id))
            return;
        SheepQuestData.instance.activeQuests.put(quest.id, new QuestData(quest));
        Server.sendToAll(EnumPacketClient.MESSAGE, new Object[]{"quest.newquest", quest.title});
        Server.sendToAll(EnumPacketClient.CHAT, new Object[]{"quest.newquest", ": ", quest.title});
    }

    public static void addActiveQuestNew(Quest quest, EntityPlayer player) {
        PlayerQuestData data = SheepQuestData.instance;
        if (PlayerQuestController.canQuestBeAccepted(quest, player))
            addActiveQuestForced(quest);
    }

    public static void startQuestNew(ScriptPlayer player, int id){
        Quest quest = QuestController.instance.quests.get(Integer.valueOf(id));
        if (quest == null)
            return;
        addActiveQuestNew(quest,(EntityPlayer)player.getMCEntity());
    }

    public static Boolean startNew(CmdQuest cmd, String[] args){
        int questid;
        String playername = args[0];
        try {
            questid = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            cmd.sendmessage("QuestID must be an integer");
            return Boolean.valueOf(false);
        }
        List<PlayerData> data = cmd.getPlayersData(playername);
        if (data.isEmpty()) {
            cmd.sendmessage(String.format("Unknow player '%s'", new Object[] { playername }));
            return Boolean.valueOf(false);
        }
        Quest quest = QuestController.instance.quests.get(Integer.valueOf(questid));
        if (quest == null) {
            cmd.sendmessage("Unknown QuestID");
            return Boolean.valueOf(false);
        }
        addActiveQuestForced(quest);
        return Boolean.valueOf(true);
    }

    public static Dialog getDialogNew(EntityNPCInterface npc, EntityPlayer player) {
        antiDialog:if(lastDialogPlayer!=null&&lastDialogPlayer.isEntityAlive()&&lastDialogPlayer!=player) {
            double[] newInfo = {lastDialogPlayer.posX,lastDialogPlayer.posY,lastDialogPlayer.posZ,lastDialogPlayer.rotationYaw,lastDialogPlayer.rotationPitch};
            //System.out.println(0);
            for(int i=0;i<5;i++)
                if(Math.abs(lastInfo[i]-newInfo[i])>=1)
                    break antiDialog;
            //System.out.println(1);
            return null;
        }
        //System.out.println(2);
        lastDialogPlayer = player;
        lastInfo = new double[]{lastDialogPlayer.posX,lastDialogPlayer.posY,lastDialogPlayer.posZ,lastDialogPlayer.rotationYaw,lastDialogPlayer.rotationPitch};
        Iterator<DialogOption> var2 = npc.dialogs.values().iterator();
        while(var2.hasNext()) {
            DialogOption option = var2.next();
            if (option != null && option.hasDialog()) {
                Dialog dialog = option.getDialog();
                if (dialog.availability.isAvailable(player)) {
                    return dialog;
                }
            }
        }
        return null;
    }

    /*
    public static EntityPlayerMP matchOnePlayerNew(ICommandSender p_82386_0_, String p_82386_1_) {
        EntityPlayerMP[] aentityplayermp = PlayerSelector.matchPlayers(p_82386_0_, p_82386_1_);
        return aentityplayermp != null && aentityplayermp.length >= 1 ? aentityplayermp[0] : null;
    }*/

    public static int getDefaultCountNew(String str){
        return str.equals("a")||str.equals("p") ? 0 : 1;
    }

    public static EntityPlayerMP matchOnePlayerNew(ICommandSender var0, String var1) {
        EntityPlayerMP[] var2 = PlayerSelector.matchPlayers(var0, var1);
        return var2 != null && var2.length >= 1 ? var2[0] : null;
    }

}
