package saltsheep.npcshareddata;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import saltsheep.npcshareddata.asm.Handler;
import saltsheep.npcshareddata.data.SheepDialogData;
import saltsheep.npcshareddata.data.SheepFactionData;
import saltsheep.npcshareddata.data.SheepQuestData;

@Mod(modid = NPCSharedData.MODID, name = NPCSharedData.NAME, version = NPCSharedData.VERSION, acceptedMinecraftVersions = "1.7.10", dependencies = "required-before:customnpcs", acceptableRemoteVersions = "*")
public class NPCSharedData
{
    public static final String MODID = "npcshareddata";
    public static final String NAME = "NPCSharedData";
    public static final String VERSION = "1.0.3";
	public static final Logger LOGGER = LogManager.getLogger();

    @Instance(NPCSharedData.MODID)
    public static NPCSharedData instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ((LaunchClassLoader)this.getClass().getClassLoader()).registerTransformer("saltsheep.npcshareddata.asm.Transformer");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    @EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent event){
        Handler.lastDialogPlayer = null;
        SheepDialogData.init();
        SheepFactionData.init();
        SheepQuestData.init();
    }

    @SubscribeEvent
    public void onServerSave(WorldEvent.Save event){
        if (event.world.isRemote)
            return;
        if(event.world!= FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[0])
            return;
        SheepDialogData.save();
        SheepFactionData.save();
        SheepQuestData.save();
    }

    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent event){
        SheepDialogData.instance = null;
        SheepFactionData.instance = null;
        SheepQuestData.instance = null;
    }

    //*It doesn't complete the implement, also use Transformer & Handler.getDefaultCountNew(String)
    @SubscribeEvent
    public void onCommandPre(CommandEvent event) {
        for (int i = 0; i < event.parameters.length; i++)
            event.parameters[i] = event.parameters[i].replace("@p", "@a");
    }

}