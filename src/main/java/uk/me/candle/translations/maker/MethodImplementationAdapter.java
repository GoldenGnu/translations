package uk.me.candle.translations.maker;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class MethodImplementationAdapter extends MethodVisitor {
	private String translation;
	private String descriptor;
	private String generatedClassName;
	MethodImplementationAdapter(MethodVisitor mv, String descriptor, String translation, String generatedClassName) {
		super(Opcodes.ASM7, mv);
		this.translation = translation;
		this.descriptor = descriptor;
		this.generatedClassName = generatedClassName;
	}
	@Override
	public void visitEnd() {
		Type[] types = Type.getArgumentTypes(descriptor);
		if (types.length == 0) {
			simpleGenerate();
		} else {
			complexGenerate(types);
		}
	}
	private void simpleGenerate() {
		mv.visitCode();
		mv.visitLdcInsn(translation);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0); // (1, 1) // calculated due to ClassWriter.COMPUTE_MAXS
	}
	private void complexGenerate(Type[] types) {
		int registers = countRegisters(types);
		mv.visitCode();
		mv.visitTypeInsn(Opcodes.NEW, "java/text/MessageFormat");
		mv.visitInsn(Opcodes.DUP);
		mv.visitLdcInsn(translation);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, generatedClassName, "getLocale", "()Ljava/util/Locale;", false);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/text/MessageFormat", "<init>", "(Ljava/lang/String;Ljava/util/Locale;)V", false);
		mv.visitVarInsn(Opcodes.ASTORE, registers + 2);
		mv.visitVarInsn(Opcodes.ALOAD, registers + 2);
		mv.visitIntInsn(Opcodes.BIPUSH, types.length);
		mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
		int regCount = 0;
		for (int i = 0; i < types.length; ++i) {
			boxIfNeededAndAddToArray(types[i], i, regCount);
			regCount += getRegisters(types[i]);
		}
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/text/MessageFormat", "format", "(Ljava/lang/Object;)Ljava/lang/String;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0); // (1, 1) // calculated due to ClassWriter.COMPUTE_MAXS
	}
	private void boxIfNeededAndAddToArray(Type t, int idx, int reg) {
		final String valueOf = "valueOf";
		mv.visitInsn(Opcodes.DUP);
		mv.visitLdcInsn(idx);
		switch (t.getSort()) {
			case Type.BOOLEAN:
			case Type.BYTE:
			case Type.CHAR:
			case Type.SHORT:
			case Type.INT:
				mv.visitVarInsn(Opcodes.ILOAD, reg + 1);
				break;
			case Type.LONG:
				mv.visitVarInsn(Opcodes.LLOAD, reg + 1);
				break;
			case Type.FLOAT:
				mv.visitVarInsn(Opcodes.FLOAD, reg + 1);
				break;
			case Type.DOUBLE:
				mv.visitVarInsn(Opcodes.DLOAD, reg + 1);
				break;
			case Type.OBJECT:
				mv.visitVarInsn(Opcodes.ALOAD, reg + 1);
				break;
			default:
				throw new IllegalArgumentException("Invalid type: " + t);
		}
		switch (t.getSort()) {
			case Type.BOOLEAN:
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", valueOf, "(Z)Ljava/lang/Boolean;", false);
				break;
			case Type.BYTE:
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", valueOf, "(B)Ljava/lang/Byte;", false);
				break;
			case Type.CHAR:
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", valueOf, "(C)Ljava/lang/Character;", false);
				break;
			case Type.SHORT:
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", valueOf, "(S)Ljava/lang/Short;", false);
				break;
			case Type.INT:
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", valueOf, "(I)Ljava/lang/Integer;", false);
				break;
			case Type.LONG:
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", valueOf, "(J)Ljava/lang/Long;", false);
				break;
			case Type.FLOAT:
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", valueOf, "(F)Ljava/lang/Float;", false);
				break;
			case Type.DOUBLE:
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", valueOf, "(D)Ljava/lang/Double;", false);
				break;
			case Type.OBJECT:
				break;
			default:
				throw new IllegalArgumentException("Invalid type: " + t);
		}
		mv.visitInsn(Opcodes.AASTORE);
	}
	private int getRegisters(Type t) {
		switch (t.getSort()) {
			case Type.BOOLEAN:
			case Type.BYTE:
			case Type.CHAR:
			case Type.SHORT:
			case Type.OBJECT:
			case Type.INT:
			case Type.FLOAT:
				return 1;
			case Type.DOUBLE:
			case Type.LONG:
				return 2;
			default:
				throw new IllegalArgumentException("Invalid type: " + t);
		}
	}
	private int countRegisters(Type[] types) {
		int c = 0;
		for (Type t : types) {
			c += getRegisters(t);
		}
		return c;
	}
}
