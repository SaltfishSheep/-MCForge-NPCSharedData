package saltsheep.npcshareddata.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PlayerFactionData;
import noppes.npcs.util.NBTJsonUtil;

import java.io.File;
import java.util.Iterator;

public class SheepFactionData extends PlayerFactionData {

    public static SheepFactionData instance = null;

    public static void init(){
        instance = new SheepFactionData();
        instance.readData();
    }

    public static void save(){
        if(instance==null)
            return;
        instance.saveData();
    }

    public void readData(){
        File dic = CustomNpcs.getWorldSaveDirectory();
        File data = new File(dic, "sheepfactiondata.json");
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
        String filename = "sheepfactiondata.json";
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

    @Override
    public NBTTagCompound getPlayerGuiData() {
        NBTTagCompound compound = new NBTTagCompound();
        super.saveNBTData(compound);
        NBTTagList list = new NBTTagList();
        Iterator<Integer> var3 = this.factionData.keySet().iterator();

        while(var3.hasNext()) {
            int id = var3.next();
            Faction faction = FactionController.getInstance().getFaction(id);
            if (faction != null && !faction.hideFaction) {
                NBTTagCompound com = new NBTTagCompound();
                faction.writeNBT(com);
                list.appendTag(com);
            }
        }

        compound.setTag("FactionList", list);
        return compound;
    }

}
