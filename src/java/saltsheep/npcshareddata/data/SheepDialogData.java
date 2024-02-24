package saltsheep.npcshareddata.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PlayerDialogData;
import noppes.npcs.controllers.PlayerFactionData;
import noppes.npcs.util.JsonException;
import noppes.npcs.util.NBTJsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class SheepDialogData extends PlayerDialogData {

    public static SheepDialogData instance = null;

    public static void init(){
        instance = new SheepDialogData();
        instance.readData();
    }

    public static void save(){
        if(instance==null)
            return;
        instance.saveData();
    }

    public void readData(){
        File dic = CustomNpcs.getWorldSaveDirectory();
        File data = new File(dic, "sheepdialogdata.json");
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
        //File data = new File(dic, "sheepdialogdata.json");
        NBTTagCompound compound = new NBTTagCompound();
        super.saveNBTData(compound);
        String filename = "sheepdialogdata.json";
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

}
