package saltsheep.npcshareddata.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import saltsheep.npcshareddata.NPCSharedData;

public class Transformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] cbytes) {
        if (transformedName.equals("noppes.npcs.controllers.PlayerData")) {
            NPCSharedData.LOGGER.info("NPCSharedData has translate class:"+transformedName);
            cbytes = MethodEditor.invokeAtMethodReturn(cbytes, "<init>", DescriptorUtils.getMethodDescriptor("void"), "saltsheep.npcshareddata.asm.Handler", "onPlayerDataInit", DescriptorUtils.getMethodDescriptor("void", "noppes.npcs.controllers.PlayerData"));
        } else if (transformedName.equals("noppes.npcs.NoppesUtilServer")) {
            NPCSharedData.LOGGER.info("NPCSharedData has translate class:"+transformedName);
            cbytes = MethodEditor.invokeAtMethodFirst(cbytes, "openDialog", DescriptorUtils.getMethodDescriptor("void", "net.minecraft.entity.player.EntityPlayer", "noppes.npcs.entity.EntityNPCInterface", "noppes.npcs.controllers.Dialog"), "saltsheep.npcshareddata.asm.Handler", "onOpenDialog", DescriptorUtils.getMethodDescriptor("void", "net.minecraft.entity.player.EntityPlayer", "noppes.npcs.entity.EntityNPCInterface", "noppes.npcs.controllers.Dialog"));
        }else if(transformedName.equals("noppes.npcs.NoppesUtilPlayer")){
            NPCSharedData.LOGGER.info("NPCSharedData has translate class:"+transformedName);
            cbytes = MethodEditor.invokeAtMethodFirst(cbytes,"dialogSelected", DescriptorUtils.getMethodDescriptor("void","int","int","net.minecraft.entity.player.EntityPlayerMP","noppes.npcs.entity.EntityNPCInterface"),"saltsheep.npcshareddata.asm.Handler","onDialogSelected", DescriptorUtils.getMethodDescriptor("void","int","int","net.minecraft.entity.player.EntityPlayerMP","noppes.npcs.entity.EntityNPCInterface"));
        }else if(transformedName.equals("noppes.npcs.controllers.PlayerQuestController")){
            NPCSharedData.LOGGER.info("NPCSharedData has translate class:"+transformedName);
            cbytes = MethodEditor.replaceMethod(cbytes,"addActiveQuest",DescriptorUtils.getMethodDescriptor("void","noppes.npcs.controllers.Quest","net.minecraft.entity.player.EntityPlayer"),"saltsheep.npcshareddata.asm.Handler","addActiveQuestNew",DescriptorUtils.getMethodDescriptor("void","noppes.npcs.controllers.Quest","net.minecraft.entity.player.EntityPlayer"));
        }else if(transformedName.equals("noppes.npcs.scripted.ScriptPlayer")){
            NPCSharedData.LOGGER.info("NPCSharedData has translate class:"+transformedName);
            cbytes = MethodEditor.replaceMethod(cbytes,"startQuest",DescriptorUtils.getMethodDescriptor("void","int"),"saltsheep.npcshareddata.asm.Handler","startQuestNew",DescriptorUtils.getMethodDescriptor("void","noppes.npcs.scripted.ScriptPlayer","int"));
        }else if(transformedName.equals("foxz.command.CmdQuest")){
            NPCSharedData.LOGGER.info("NPCSharedData has translate class:"+transformedName);
            cbytes = MethodEditor.replaceMethod(cbytes,"start",DescriptorUtils.getMethodDescriptor("java.lang.Boolean","java.lang.String[]"),"saltsheep.npcshareddata.asm.Handler","startNew",DescriptorUtils.getMethodDescriptor("java.lang.Boolean","foxz.command.CmdQuest","java.lang.String[]"));
        }else if(transformedName.equals("noppes.npcs.entity.EntityNPCInterface")){
            NPCSharedData.LOGGER.info("NPCSharedData has translate class:"+transformedName);
            cbytes = MethodEditor.replaceMethod(cbytes,"getDialog",DescriptorUtils.getMethodDescriptor("noppes.npcs.controllers.Dialog","net.minecraft.entity.player.EntityPlayer"),"saltsheep.npcshareddata.asm.Handler","getDialogNew",DescriptorUtils.getMethodDescriptor("noppes.npcs.controllers.Dialog","noppes.npcs.entity.EntityNPCInterface","net.minecraft.entity.player.EntityPlayer"));
        }else if(transformedName.equals("noppes.npcs.entity.EntityNPCInterface")){
            NPCSharedData.LOGGER.info("NPCSharedData has translate class:"+transformedName);
            cbytes = MethodEditor.replaceMethod(cbytes,"getDialog",DescriptorUtils.getMethodDescriptor("noppes.npcs.controllers.Dialog","net.minecraft.entity.player.EntityPlayer"),"saltsheep.npcshareddata.asm.Handler","getDialogNew",DescriptorUtils.getMethodDescriptor("noppes.npcs.controllers.Dialog","noppes.npcs.entity.EntityNPCInterface","net.minecraft.entity.player.EntityPlayer"));
        }else if(transformedName.equals("net.minecraft.command.PlayerSelector")){
            NPCSharedData.LOGGER.info("NPCSharedData has translate class:"+transformedName);
            cbytes = MethodEditor.replaceMethod(cbytes,"func_82382_g",DescriptorUtils.getMethodDescriptor("int","java.lang.String"),"saltsheep.npcshareddata.asm.Handler","getDefaultCountNew",DescriptorUtils.getMethodDescriptor("int","java.lang.String"));
            cbytes = MethodEditor.replaceMethod(cbytes,"func_82386_a",DescriptorUtils.getMethodDescriptor("net.minecraft.entity.player.EntityPlayerMP","net.minecraft.command.ICommandSender","java.lang.String"),"saltsheep.npcshareddata.asm.Handler","matchOnePlayerNew",DescriptorUtils.getMethodDescriptor("net.minecraft.entity.player.EntityPlayerMP","net.minecraft.command.ICommandSender","java.lang.String"));
        }
        return cbytes;
    }
}
