package dev.bodyalhoha.souvenir.transformers.impl;

import dev.bodyalhoha.souvenir.transformers.Transformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.Arrays;

public class InvokeDynamicTransformer extends Transformer {
    @Override
    public void run(ClassNode cn) {
        cn.methods.forEach(mn -> {
            Arrays.stream(mn.instructions.toArray()).forEach(insn -> {
                // soon:tm:
            });
        });
    }
}
