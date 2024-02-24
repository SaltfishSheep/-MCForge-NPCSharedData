package saltsheep.npcshareddata.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumQuestType;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.QuestData;
import noppes.npcs.quests.QuestInterface;
import noppes.npcs.util.NBTJsonUtil;

import java.io.File;
import java.util.Iterator;

public class SheepQuestData extends PlayerQuestData {

    public static SheepQuestData instance = null;

    public static void init(){
        instance = new SheepQuestData();
        instance.readData();
    }

    public static void save(){
        if(instance==null)
            return;
        instance.saveData();
    }

    public void readData(){
        File dic = CustomNpcs.getWorldSaveDirectory();
        File data = new File(dic, "sheepquestdata.json");
        if(!data.exists())
            return;
        try {
            NBTTagCompound nbt = NBTJsonUtil.LoadFile(data);
            super.loadNBTData(nbt);
        } catch (Exception e) {
            LogWriter.except(e);
        }
    }

    public void saveData() {
        File dic = CustomNpcs.getWorldSaveDirectory();
        NBTTagCompound compound = new NBTTagCompound();
        super.saveNBTData(compound);
        String filename = "sheepquestdata.json";
        try {
            File file = new File(dic, filename + "_new");
            File file1 = new File(dic, filename);
            NBTJsonUtil.SaveFile(file, compound);
            if (file1.exists()) {
                file1.delete();
            }
            file.renameTo(file1);
        } catch (Exception var7) {
            LogWriter.except(var7);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        return;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        return;
    }

    public boolean checkQuestCompletion(EntityPlayer player, EnumQuestType type) {
        boolean bo = false;
        Iterator<QuestData> var4 = this.activeQuests.values().iterator();

        while(true) {
            QuestData data;
            do {
                if (!var4.hasNext()) {
                    return bo;
                }

                data = var4.next();
            } while(data.quest.type != type && type != null);

            QuestInterface inter = data.quest.questInterface;
            if (inter.isCompleted(player)) {
                if (!data.isCompleted) {
                    if (!data.quest.complete(player, data)) {
                        Server.sendToAll(EnumPacketClient.MESSAGE, new Object[]{"quest.completed", data.quest.title});
                        Server.sendToAll(EnumPacketClient.CHAT, new Object[]{"quest.completed", ": ", data.quest.title});
                        if(type==EnumQuestType.Item)
                            Server.sendToAll(EnumPacketClient.CHAT, new Object[]{"saltsheep.npcshareddata.quest.item", ": ", player.getDisplayName()});
                    }
                    data.isCompleted = true;
                    bo = true;
                }
            } else {
                data.isCompleted = false;
            }
        }
    }

}
