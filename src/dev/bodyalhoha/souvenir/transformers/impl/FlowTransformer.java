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
    public void ex(){
        double[] a = new double[1000];
        a[7 ^ 17 + -7] = 29.736554500849767 + (double)(-1 ^ 18);
    }

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

                if((insn.getOpcode() >= Opcodes.IFEQ && insn.getOpcode() <= Opcodes.IF_ACMPNE) || insn.getOpcode() == Opcodes.IFNULL || insn.getOpcode() == Opcodes.IFNONNULL || insn.getOpcode() == Opcodes.IASTORE || insn.getOpcode() == Opcodes.DSTORE || insn.getOpcode() == Opcodes.GOTO || insn.getOpcode() == Opcodes.DASTORE){
                    if(Obfuscator.getInstance().r.nextInt(3) == 2){
                        InsnList list = new InsnList();
                        final LabelNode l0 = new LabelNode();
                        final LabelNode l1 = new LabelNode();

                        list.add(AsmUtils.pushInt(10));
                        list.add(new JumpInsnNode(Opcodes.GOTO, l1));
                        list.add(l0);
                        list.add(AsmUtils.pushInt(5));
                        list.add(l1);
                        list.add(new JumpInsnNode(Opcodes.IFEQ, l0));

                        mn.instructions.insert(insn, list);
                    }

                }

            });
        });



    }
}
