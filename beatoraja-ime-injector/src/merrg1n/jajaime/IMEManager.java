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

    public static void create(String title) {
        System.out.println("[IME fix] get window title:" + title);

        long hwnd = getHWND(title);
        if (hwnd == 0) {
            System.out.println("[IME fix] fail to get hwnd.");
            System.exit(-1);
        }

        System.out.println("[IME fix] get hwnd successfully.");
        instance = new IMEManager(hwnd);
    }

    public static void dispose() {
        instance.enable();
        instance = null;
    }

    private IMEManager(long hwnd) {
        System.out.println("[IME fix] IMEManager Init!");
        this.hwnd = hwnd;
        this.himc = associateContext(hwnd, 0);
        this.state = false;
    }

    private final long hwnd;
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

}
