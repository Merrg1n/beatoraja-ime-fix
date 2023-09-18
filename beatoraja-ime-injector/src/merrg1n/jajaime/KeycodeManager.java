package merrg1n.jajaime;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class KeycodeManager {
    public static int[] gdxKeyToVirtKey = new int[256];

    public static void initKeymap(Object input_obj) {
        int[] lwjglKeyToVirtKey = new int[256];
        Set<Integer> lwjglKeys = new HashSet<>();
        try {
            // lwjgl key to virt key
            Class<?> wk_clazz = Class.forName("org.lwjgl.opengl.WindowsKeycodes");
            Method mapVirtualKeyToLWJGLCode = wk_clazz.getDeclaredMethod("mapVirtualKeyToLWJGLCode", Integer.TYPE);
            mapVirtualKeyToLWJGLCode.setAccessible(true);
            Field[] fields = wk_clazz.getFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isPublic(mod) &&
                    Modifier.isStatic(mod) &&
                    Modifier.isFinal(mod) &&
                    field.getType().equals(int.class) &&
                    field.getName().startsWith("VK_")) {
                    field.setAccessible(true);
                    int virtKey = field.getInt(null);
                    int lwjglKey = (Integer) mapVirtualKeyToLWJGLCode.invoke(null, virtKey);
                    lwjglKeys.add(lwjglKey);
                    lwjglKeyToVirtKey[lwjglKey] = virtKey;
                }
            }
            // gdx key to lwjgl key
            Class<?> input_clazz = input_obj.getClass();
            Method getGdxKeyCode = input_clazz.getDeclaredMethod("getGdxKeyCode", Integer.TYPE);
            int mod = getGdxKeyCode.getModifiers();
            getGdxKeyCode.setAccessible(true);
            for (int lwjglKey: lwjglKeys) {
                // in newer version libgdx, this method is not static
                Object invoke_this = Modifier.isStatic(mod) ? null: input_obj;
                int gdxKey = (Integer) getGdxKeyCode.invoke(invoke_this, lwjglKey);
                if (gdxKey < 0)
                    gdxKey = 0;
                gdxKeyToVirtKey[gdxKey] = lwjglKeyToVirtKey[lwjglKey];
            }

            System.out.println("[IME fix] initKeymap success.");
//            for (int i = 0; i < 255; i++) {
//                System.out.print(gdxKeyToVirtKey[i]);
//                System.out.print(',');
//            }
//            System.out.print('\n');

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("[IME fix] initKeymap fail.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
