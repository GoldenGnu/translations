package uk.me.candle.translations;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.TraceClassVisitor;

/**
 *
 * @author Andrew Wheat
 */
public class Bundle {

	Locale locale;
	static final boolean LOAD_IGNORE_MISSING = true;
	static final boolean LOAD_IGNORE_EXTRA = true;
	static final boolean LOAD_IGNORE_PARAM_MISMATCH = true;

	static <T extends Bundle> T load(Class<T> cls, Locale locale) throws Exception { // XXX throw more specific exceptions.

		final Properties translations = new Properties();
		InputStream translationsIn = cls.getClassLoader().getResourceAsStream(
				cls.getPackage().getName().replace(".", "/")
				+ "/"
				+ cls.getSimpleName().toLowerCase()
				+ ".properties"
				); // vary this per locale.
 		translations.load(translationsIn);
		final Set<String> usedKeys = new HashSet<String>();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		{
			InputStream classIn = Bundle.class.getClassLoader().getResourceAsStream(
					cls.getName().replace(".", "/")
					+ ".class"
					);
			byte[] buff = new byte[1024];
			int read = -1;
			while (0 < (read = classIn.read(buff))) {
				baos.write(buff, 0, read);
			}
		}


		byte[] b1 = baos.toByteArray();
		ClassReader cr = new ClassReader(b1);
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
		TraceClassVisitor tcv = new TraceClassVisitor(cw, new PrintWriter(System.out));
		ImplementMethodsAdapter ca = new ImplementMethodsAdapter(tcv, translations, usedKeys);
		cr.accept(ca, 0);
		byte[] b2 = cw.toByteArray();

		BundleClassLoader bcl = new BundleClassLoader();
		Class<?> result = bcl.defineClass(ca.getNewName().replace("/", "."), b2);

		return (T) result.newInstance();
	}

	public static class ImplementMethodsAdapter extends ClassAdapter {
		String newName;
		Properties translations;
		Set<String> usedKeys;

		public ImplementMethodsAdapter(ClassVisitor cv, Properties translations, Set<String> usedKeys) {
			super(cv);
			this.translations = translations;
			this.usedKeys = usedKeys;
		}

		public String getNewName() {
			return newName;
		}

		@Override
		public void visit(int version, int access, String name,
				String signature, String superName, String[] interfaces) {
			newName = name + "__Impl";
			cv.visit(Opcodes.V1_6
					, access - Opcodes.ACC_ABSTRACT // remove the abstract.
					, newName // new name
					, signature // generics? signature
					, name // extend this
					, interfaces
					);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name,
				String desc, String signature, String[] exceptions) {
			if ((access & Opcodes.ACC_ABSTRACT) > 0 &&
					Type.getReturnType(desc).equals(Type.getType(String.class))) {
				MethodVisitor mv = cv.visitMethod(
							access - Opcodes.ACC_ABSTRACT
							, name
							, desc
							, signature
							, exceptions);
				return new MethodImplementationAdapter(
						mv
						, translations.getProperty(name)
						);
			} else {
				return cv.visitMethod(access, name, desc, signature, exceptions);
			}
		}

	}

	static class MethodImplementationAdapter extends MethodAdapter {
		String translation;

		public MethodImplementationAdapter(MethodVisitor mv, String translation) {
			super(mv);
			this.translation = translation;
		}

		@Override
		public void visitEnd() {
			foo();
		}

		private void foo() {
			mv.visitCode();
			mv.visitLdcInsn(translation);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0); // (1, 1) // calculated due to ClassWriter.COMPUTE_MAXS
		}

	}

	/**
	 * Checks to see if the string translation has all the required {x} fields for the method signature.
	 * @param translation
	 * @param method
	 * @return
	 */
	private static boolean checkMethodParamsAndStringFields(String translation, Method method) {
		return true;
	}

	/**
	 * Compares the used keys and translations for extra translations.
	 * @param translations
	 * @param usedKeys
	 */
	private static void checkForExtras(Properties translations, Set<String> usedKeys) {
		Set<String> extras = new HashSet<String>();
		for (String o : translations.stringPropertyNames()) {
			if (!usedKeys.contains(o)) {
				extras.add(o);
			}
		}
		if (!extras.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String s : extras) {
				if (sb.length() != 0) {
					sb.append(", ");
				}
				sb.append(s);
			}
			throw new RuntimeException("Extra keys in the bundle: " + sb.toString());
		}
	}
}

class BundleClassLoader extends ClassLoader {

	public Class<?> defineClass(String name, byte[] b) {
		return defineClass(name, b, 0, b.length);
	}
}