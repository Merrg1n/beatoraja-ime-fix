package merrg1n.jajaime;

import java.lang.instrument.Instrumentation;

public class PreMain {
    public static void premain(String agentOps, Instrumentation inst){
        System.out.println("[IME Fix] Beatoraja IME Fix start!");
        inst.addTransformer(new MainControllerTransformer());
        inst.addTransformer(new InputTransformer());
    }
}
