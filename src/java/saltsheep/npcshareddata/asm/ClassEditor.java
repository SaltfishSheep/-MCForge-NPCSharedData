package saltsheep.npcshareddata.asm;

import org.objectweb.asm.*;

import java.util.Arrays;

public class ClassEditor {
    public static byte[] createField(byte[] classBytes, int access, String name, String desc, Object defaultValue) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                if (name.equals(name)) {
                    return null;
                } else {
                    return super.visitField(access, name, desc, signature, value);
                }
            }
        };
        cr.accept(cv, 0);
        FieldVisitor fv = cw.visitField(access, name, desc, null, defaultValue);
        fv.visitEnd();
        return cw.toByteArray();
    }

    public static byte[] createMethod(byte[] classBytes, int methodAccess, String methodName, String methodDesc) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (methodName.equals(name) && methodDesc.equals(desc)) {
                    return null;
                } else {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            }
        };
        cr.accept(cv, 0);
        MethodVisitor mv = cw.visitMethod(methodAccess, methodName, methodDesc, null, null);
        mv.visitCode();
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        return cw.toByteArray();
    }

    public static byte[] deleteField(byte[] classBytes, String name){
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                if (name.equals(name)) {
                    return null;
                } else {
                    return super.visitField(access, name, desc, signature, value);
                }
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public static byte[] deleteMethod(byte[] classBytes, String methodName, String methodDesc){
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (methodName.equals(name) && methodDesc.equals(desc)) {
                    return null;
                } else {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public static byte[] renameField(byte[] classBytes, String oldName, String newName) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                if (name.equals(oldName)) {
                    return super.visitField(access, newName, desc, signature, value);
                } else {
                    return super.visitField(access, name, desc, signature, value);
                }
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public static byte[] renameMethod(byte[] classBytes, String oldName, String newName, String methodDesc) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (name.equals(oldName) && methodDesc.equals(desc)) {
                    return super.visitMethod(access, newName, desc, signature, exceptions);
                } else {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public static byte[] addImplement(byte[] classBytes, String interfaceName) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                String[] newInterfaces = Arrays.copyOf(interfaces, interfaces.length + 1);
                newInterfaces[newInterfaces.length - 1] = interfaceName.replace('.', '/');
                super.visit(version, access, name, signature, superName, newInterfaces);
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }
/*
    private static class ClassInfo{
        private ClassReader cr;
        private ClassInfo(ClassReader cr){this.cr=cr;}
        private ClassInfo()
    }
*/
}