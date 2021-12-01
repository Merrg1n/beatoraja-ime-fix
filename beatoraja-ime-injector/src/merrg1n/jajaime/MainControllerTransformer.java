package merrg1n.jajaime;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import static org.objectweb.asm.Opcodes.ASM9;


public class MainControllerTransformer implements ClassFileTransformer {
    public MethodVisitor transform_create(MethodVisitor old) {
        return new MethodVisitor(ASM9, old) {
            @Override
            public void visitCode() {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "bms/player/beatoraja/MainController", "getVersion", "()Ljava/lang/String;", false);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "merrg1n/jajaime/IMEManager", "create", "(Ljava/lang/String;)V", false);
                super.visitCode();
            }
        };
    }

    public MethodVisitor transform_dispose(MethodVisitor old) {
        return new MethodVisitor(ASM9, old) {
            @Override
            public void visitCode() {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "merrg1n/jajaime/IMEManager", "dispose", "()V", false);
                super.visitCode();
            }
        };
    }

    public MethodVisitor transform_pause(MethodVisitor old) {
        return new MethodVisitor(ASM9, old) {
            @Override
            public void visitCode() {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "merrg1n/jajaime/IMEManager", "getInstance", "()Lmerrg1n/jajaime/IMEManager;", false);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "merrg1n/jajaime/IMEManager", "enable", "()V", false);
                super.visitCode();
            }
        };
    }

    public MethodVisitor transform_resume(MethodVisitor old) {
        return new MethodVisitor(ASM9, old) {
            @Override
            public void visitCode() {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "merrg1n/jajaime/IMEManager", "getInstance", "()Lmerrg1n/jajaime/IMEManager;", false);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "merrg1n/jajaime/IMEManager", "disable", "()V", false);
                super.visitCode();
            }
        };
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if ("bms/player/beatoraja/MainController".equals(className)) {
            System.out.println("[IME Fix] Found MainController, start transform.");
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new ClassVisitor(ASM9, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodVisitor old = super.visitMethod(access, name, descriptor, signature, exceptions);
                    if ("create".equals(name))
                        return transform_create(old);

                    if ("dispose".equals(name))
                        return transform_dispose(old);

                    if ("pause".equals(name))
                        return transform_pause(old);

                    if ("resume".equals(name))
                        return transform_resume(old);

                    return old;
                }
            };
            cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            return cw.toByteArray();
        }
        return classfileBuffer;
    }
}
