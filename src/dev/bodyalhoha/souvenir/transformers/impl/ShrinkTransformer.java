package dev.bodyalhoha.souvenir.transformers.impl;

import dev.bodyalhoha.souvenir.transformers.Transformer;
import dev.bodyalhoha.souvenir.utils.RandomUtils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;

import java.util.Arrays;

public class ShrinkTransformer extends Transformer {
    @Override
    public void run(ClassNode cn) {
        cn.sourceDebug = "";
        cn.sourceFile = "";
        cn.methods.forEach(mn -> {
            if(mn.localVariables != null){
                mn.localVariables.forEach(lv -> lv.name = "souvenir" + RandomUtils.getRandomAscii(5));
            }
            Arrays.stream(mn.instructions.toArray()).forEach((insn) -> {
                if(insn instanceof LineNumberNode)
                    mn.instructions.remove(insn);
            });
        });
    }
}
