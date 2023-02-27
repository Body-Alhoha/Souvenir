package dev.bodyalhoha.souvenir.transformers.impl;

import dev.bodyalhoha.souvenir.Obfuscator;
import dev.bodyalhoha.souvenir.transformers.Transformer;
import dev.bodyalhoha.souvenir.utils.AsmUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FlowTransformer extends Transformer {

    public void encapsulate(MethodNode mn, AbstractInsnNode node){
        if(Obfuscator.getInstance().r.nextBoolean()){
            InsnList gflow = new InsnList();
            LabelNode label = new LabelNode();
            boolean v = Obfuscator.getInstance().r.nextBoolean();
            gflow.add(new InsnNode(v ? Opcodes.ICONST_1 : Opcodes.ICONST_0));
            gflow.add(new JumpInsnNode(v ? Opcodes.IFEQ : Opcodes.IFNE, label));

            mn.instructions.insertBefore(node, gflow);
            mn.instructions.insert(node, label);
        }else{
            InsnList gflow = new InsnList();
            LabelNode bLabel = new LabelNode();
            LabelNode cLabel = new LabelNode();
            LabelNode end = new LabelNode();

            int a = Obfuscator.getInstance().r.nextInt(100) + 10;
            int b = a - Obfuscator.getInstance().r.nextInt(20) - 5;
            LookupSwitchInsnNode switchNode = new LookupSwitchInsnNode(end, Stream.of(b, a).mapToInt(i -> i).toArray(), Arrays.asList(bLabel, cLabel).toArray(new LabelNode[2]));
            gflow.add(new IntInsnNode(Opcodes.BIPUSH, a));
            gflow.add(switchNode);
            gflow.add(bLabel);
            gflow.add(new JumpInsnNode(Opcodes.GOTO, end));
            gflow.add(cLabel);
            mn.instructions.insertBefore(node, gflow);
            gflow.clear();

            gflow.add(new JumpInsnNode(Opcodes.GOTO, end));


            gflow.add(end);
            mn.instructions.insert(node, gflow);

        }

    }


    @Override
    public void run(ClassNode cn) {
        cn.methods.forEach(mn -> {
            Arrays.stream(mn.instructions.toArray()).forEach(insn -> {
                if(insn.getOpcode() == Opcodes.GOTO)
                    encapsulate(mn, insn);

            });
        });



    }
}
