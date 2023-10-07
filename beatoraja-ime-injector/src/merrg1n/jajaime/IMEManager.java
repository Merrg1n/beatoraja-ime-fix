package merrg1n.jajaime;

public class IMEManager {
    static {
        System.loadLibrary("BeatorajaIMEFixNative");
    }
    public native static long getHWND(String title);

    public native static long associateContext(long hwnd, long himc);

    public native static boolean isKeyPressed(int key);
    private static IMEManager instance = null;

    public static IMEManager getInstance() {
        return instance;
    }

    public static boolean isGdxKeyPressed(int key) {
        // avoid accepting keyboard input while minimized
        boolean isMinimized = false;
        if (IMEManager.instance != null)
            isMinimized = !IMEManager.instance.getState();

        return isMinimized && isKeyPressed(KeycodeManager.gdxKeyToVirtKey[key]);
    }


    public static void create(String title) {
        System.out.println("[IME fix] get window title:" + title);

        instance = new IMEManager(title);
    }

    public static void dispose() {
        instance.enable();
        instance = null;
    }

    private IMEManager(String title) {
        System.out.println("[IME fix] IMEManager Init!");
        this.title = title;
        this.updateHWND();
        this.disable();
    }

    private final String title;
    private long hwnd;
    private long himc;
    private boolean state;

    public boolean getState() {
        return state;
    }

    public void disable() {
        long tmp = associateContext(hwnd, 0);
        if (tmp != 0)
            this.himc = tmp;
        this.state = false;
        System.out.println("[IME fix] IME disabled.");
    }

    public void enable() {
        associateContext(hwnd, this.himc);
        this.state = true;
        System.out.println("[IME fix] IME enabled.");
    }

    private void updateHWND() {
        long hwnd = getHWND(this.title);
        if (hwnd == 0) {
            throw new RuntimeException("fail to get hwnd");
        }
        this.hwnd = hwnd;
        System.out.println("[IME fix] get hwnd successfully.");
    }

    public void refresh() {
        System.out.println("[IME fix] display mode changed, rebind hwnd.");
        this.updateHWND();
        this.disable();
    }

}
