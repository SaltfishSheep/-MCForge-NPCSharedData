package saltsheep.npcshareddata.asm;

import org.objectweb.asm.*;

//*All the method invoke must be static，if hooked method is virtual, input(this,other args...), else(static) input(args...)
public class MethodEditor {

    public static byte[] invokeAtMethodFirst(byte[] classBytes, String methodName, String methodDesc, String invokeMClass, String invokeM, String invokeMDesc){
        Type[] args = Type.getArgumentTypes(methodDesc);
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (methodName.equals(name) && methodDesc.equals(desc)) {
                    return new MethodVisitor(Opcodes.ASM5,super.visitMethod(access,name,desc,signature,exceptions)) {
                        @Override
                        public void visitCode() {
                            super.visitCode();
                            boolean isStatic = (access&Opcodes.ACC_STATIC)==Opcodes.ACC_STATIC;
                            if(!isStatic) //*如果方法非静态，则将this载入
                                this.visitVarInsn(Opcodes.ALOAD, 0);
                            for(int index = 0;index<args.length;index++){
                                int varIndex = isStatic? index:index+1;
                                pushVar(this,varIndex,args[index].getClassName());
                                //System.out.println(varIndex);
                            }
                            this.visitMethodInsn(Opcodes.INVOKESTATIC,invokeMClass.replace(".","/"),invokeM,invokeMDesc,false);
                        }
                    };
                } else {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    //*If the hooked method isn't a void method, you must return the result of the hooked method.
    public static byte[] invokeAtMethodReturn(byte[] classBytes, String methodName, String methodDesc, String invokeMClass, String invokeM, String invokeMDesc){
        Type[] args = Type.getArgumentTypes(methodDesc);
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (methodName.equals(name) && methodDesc.equals(desc)) {
                    return new MethodVisitor(Opcodes.ASM5,super.visitMethod(access,name,desc,signature,exceptions)) {
                        private boolean hasWrite = false;
                        @Override
                        public void visitInsn(int opcodes) {
                            if(opcodes>177||opcodes<172) {//*如果不是返回指令
                                super.visitInsn(opcodes);
                                return;
                            }
                            if(hasWrite) {
                                super.visitInsn(opcodes);
                                return;
                            }
                            hasWrite = true;//*如果不加这个，accept会不断地访问新添加的return，然后递归爆栈
                            boolean isStatic = (access&Opcodes.ACC_STATIC)==Opcodes.ACC_STATIC;
                            if(!isStatic) //*如果方法非静态，则将this载入
                                this.visitVarInsn(Opcodes.ALOAD, 0);
                            for(int index = 0;index<args.length;index++){
                                int varIndex = isStatic? index:index+1;
                                pushVar(this,varIndex,args[index].getClassName());
                                //System.out.println(varIndex);
                            }
                            this.visitMethodInsn(Opcodes.INVOKESTATIC,invokeMClass.replace(".","/"),invokeM,invokeMDesc,false);
                            output(this,Type.getReturnType(methodDesc).getClassName());
                        }
                    };
                } else {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public static byte[] replaceMethod(byte[] classBytes, String methodName, String methodDesc, String invokeMClass, String invokeM, String invokeMDesc){
        Type[] args = Type.getArgumentTypes(methodDesc);
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (methodName.equals(name) && methodDesc.equals(desc)) {
                    return new MethodVisitor(Opcodes.ASM5,super.visitMethod(access,name,desc,signature,exceptions)) {
                        @Override
                        public void visitCode() {
                            super.visitCode();
                            boolean isStatic = (access&Opcodes.ACC_STATIC)==Opcodes.ACC_STATIC;
                            if(!isStatic) //*如果方法非静态，则将this载入
                                this.visitVarInsn(Opcodes.ALOAD, 0);
                            for(int index = 0;index<args.length;index++){
                                int varIndex = isStatic? index:index+1;
                                pushVar(this,varIndex,args[index].getClassName());
                            }
                            this.visitMethodInsn(Opcodes.INVOKESTATIC,invokeMClass.replace('.','/'),invokeM,invokeMDesc,false);
                            output(this,Type.getReturnType(methodDesc).getClassName());
                        }
                    };
                } else {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public static void pushVar(MethodVisitor mv, int varIndex, String classNameFromType){
        switch (classNameFromType){
            case "byte":
                mv.visitVarInsn(Opcodes.ILOAD,varIndex);
                break;
            case "char":
                mv.visitVarInsn(Opcodes.ILOAD,varIndex);
                break;
            case "double":
                mv.visitVarInsn(Opcodes.DLOAD,varIndex);
                break;
            case "float":
                mv.visitVarInsn(Opcodes.FLOAD,varIndex);
                break;
            case "int":
                mv.visitVarInsn(Opcodes.ILOAD,varIndex);
                break;
            case "long":
                mv.visitVarInsn(Opcodes.LLOAD,varIndex);
                break;
            case "short":
                mv.visitVarInsn(Opcodes.ILOAD,varIndex);
                break;
            case "boolean":
                mv.visitVarInsn(Opcodes.ILOAD,varIndex);
                break;
            default:
                mv.visitVarInsn(Opcodes.ALOAD,varIndex);
                break;
        }
    }

    public static void output(MethodVisitor mv, String returnClassNameFromType){
        switch (returnClassNameFromType){
            case "byte":
                mv.visitInsn(Opcodes.IRETURN);
                break;
            case "char":
                mv.visitInsn(Opcodes.IRETURN);
                break;
            case "double":
                mv.visitInsn(Opcodes.DRETURN);
                break;
            case "float":
                mv.visitInsn(Opcodes.FRETURN);
                break;
            case "int":
                mv.visitInsn(Opcodes.IRETURN);
                break;
            case "long":
                mv.visitInsn(Opcodes.LRETURN);
                break;
            case "short":
                mv.visitInsn(Opcodes.IRETURN);
                break;
            case "boolean":
                mv.visitInsn(Opcodes.IRETURN);
                break;
            case "void":
                mv.visitInsn(Opcodes.RETURN);
                break;
            default:
                mv.visitInsn(Opcodes.ARETURN);
                break;
        }
    }

}
