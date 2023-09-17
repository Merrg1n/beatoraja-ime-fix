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


public class InputTransformer implements ClassFileTransformer {
    public MethodVisitor transform_isKeyPressed(MethodVisitor old) {
        System.out.println("[IME Fix] transfromed isKeyPressed.");
        return new MethodVisitor(ASM9, old) {
            @Override
            public void visitCode() {
                super.visitVarInsn(Opcodes.ILOAD, 1);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "merrg1n/jajaime/IMEManager", "isGdxKeyPressed", "(I)Z", false);
                super.visitInsn(Opcodes.IRETURN);
                super.visitEnd();
            }
        };
    }

    public MethodVisitor transform_constructor(MethodVisitor old) {
        System.out.println("[IME Fix] transfromed constructor.");
        return new MethodVisitor(ASM9, old) {
            @Override
            public void visitInsn(int opcode) {
                if (opcode == Opcodes.RETURN) {
                    // constructor only have RETURN opcode
                    super.visitVarInsn(Opcodes.ALOAD, 0);
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "merrg1n/jajaime/KeycodeManager", "initKeymap", "(Ljava/lang/Object;)V", false);
                }
                super.visitInsn(opcode);
            }
        };
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if ("com/badlogic/gdx/backends/lwjgl/DefaultLwjglInput".equals(className) ||
            "com/badlogic/gdx/backends/lwjgl/LwjglInput".equals(className)) {
            System.out.println("[IME Fix] Found " + className + " , start transform.");
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new ClassVisitor(ASM9, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodVisitor old = super.visitMethod(access, name, descriptor, signature, exceptions);
                    if ("isKeyPressed".equals(name)) return transform_isKeyPressed(old);
                    if ("<init>".equals(name)) return transform_constructor(old);

                    return old;
                }
            };
            cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            return cw.toByteArray();
        }
        return classfileBuffer;
    }
}
