package dev.bodyalhoha.souvenir;

import dev.bodyalhoha.souvenir.transformers.Transformer;
import dev.bodyalhoha.souvenir.transformers.impl.*;
import dev.bodyalhoha.souvenir.transformers.impl.flow.BytecodeFlowTransformer;
import dev.bodyalhoha.souvenir.transformers.impl.flow.EmptyFlowTransformer;
import dev.bodyalhoha.souvenir.transformers.impl.flow.FlowTransformer;
import dev.bodyalhoha.souvenir.transformers.impl.string.StringTransformer;
import dev.bodyalhoha.souvenir.utils.JarLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Obfuscator {

    private static Obfuscator instance;
    private final List<Transformer> transformers = Arrays.asList(

            new CrasherTransformer(),
            new EmptyFlowTransformer(),
            new ShrinkTransformer(),
            new FlowTransformer(),
            new BytecodeFlowTransformer(),
            new InvokeDynamicTransformer(),
            new StringTransformer(),
            new NumberTransformer()
    );

    public final Random r = new Random();

    public static Obfuscator getInstance(){
        if(instance == null)
            instance = new Obfuscator();
        return instance;
    }

    public void obfuscate(String input, String pkg) throws Exception{
        JarLoader loader = new JarLoader(input, input.substring(0, input.length() - 4) + "-obfuscated.jar");
        System.out.println("[core] Loading the jar...");
        loader.loadJar();

        loader.classes.stream().filter(c -> c.name.startsWith(pkg + "/")).forEach(c -> transformers.forEach(t -> t.run(c)));

        System.out.println("[core] Saving the jar...");
        loader.saveJar();

    }

}
