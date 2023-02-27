package dev.bodyalhoha.souvenir.transformers.impl;

import dev.bodyalhoha.souvenir.transformers.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;

import java.util.Arrays;

public class IntTransformer extends Transformer {
    @Override
    public void run(ClassNode cn) {
        if((cn.access & ACC_INTERFACE) == ACC_INTERFACE) {
            return;
        }
        cn.methods.forEach(mn -> {
            long numbers = Arrays.stream(mn.instructions.toArray()).filter(insn -> insn instanceof IntInsnNode).count();

            // coming soon

        });
    }
}
