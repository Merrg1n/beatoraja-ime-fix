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
		return new MethodVisitor(ASM9, old) {
			@Override
			public void visitCode() {
				super.visitVarInsn(Opcodes.ILOAD, 1);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "merrg1n/jajaime/IMEManager", "isKeyPressed", "(I)Z", false);
				super.visitInsn(Opcodes.IRETURN);
				super.visitEnd();
			}
		};
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if ("com/badlogic/gdx/backends/lwjgl/LwjglInput".equals(className)) {
			System.out.println("[IME Fix] Found com.badlogic.gdx.backends.lwjgl.LwjglInput, start transform.");
			ClassReader cr = new ClassReader(classfileBuffer);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			ClassVisitor cv = new ClassVisitor(ASM9, cw) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
					MethodVisitor old = super.visitMethod(access, name, descriptor, signature, exceptions);
					if ("isKeyPressed".equals(name))
						return transform_isKeyPressed(old);

					return old;
				}
			};
			cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		}
		return classfileBuffer;
	}
}
