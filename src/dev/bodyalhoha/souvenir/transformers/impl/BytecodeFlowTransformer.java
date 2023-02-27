package dev.bodyalhoha.souvenir.transformers.impl;

import dev.bodyalhoha.souvenir.Obfuscator;
import dev.bodyalhoha.souvenir.transformers.Transformer;
import dev.bodyalhoha.souvenir.utils.RandomUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

public class BytecodeFlowTransformer extends Transformer {

    public InsnList getRandomFlow(){
        InsnList flow = new InsnList();

        int m = Obfuscator.getInstance().r.nextInt(3);
        if(m == 0){
            flow.add(new IntInsnNode(BIPUSH, (int) (Math.floor(Math.random() * 60) - 30)));
            flow.add(new IntInsnNode(BIPUSH, (int) (Math.floor(Math.random() * 60)) - 30));
            int r = Obfuscator.getInstance().r.nextInt(3);
            if(r == 0){
                flow.add(new InsnNode(Opcodes.IAND));
            }
            if(r == 1){
                flow.add(new InsnNode(Opcodes.IADD));
            }
            if(r == 2){
                flow.add(new InsnNode(POP));
            }
            flow.add(new InsnNode(POP));
        }
        if(m == 1){
            flow.add(new LdcInsnNode(RandomUtils.getRandomUTF(10)));
            flow.add(new InsnNode(POP));
        }
        if(m == 1){
            flow.add(new InsnNode(Obfuscator.getInstance().r.nextBoolean() ? Opcodes.ICONST_1 : Opcodes.ICONST_0));
            flow.add(new InsnNode(POP));
        }



        return flow;
    }


    @Override
    public void run(ClassNode cn) {
        if((cn.access & ACC_INTERFACE) == ACC_INTERFACE) {
            return;
        }
        cn.fields.forEach(fn -> {
            if((fn.access & ACC_DEPRECATED) == 0){
                fn.access |= ACC_INTERFACE;
            }
        });
        cn.methods.forEach(mn -> {
            if(mn.instructions.size() == 0)
                return;
            if((mn.access & ACC_DEPRECATED) == 0){
                mn.access |= ACC_INTERFACE;
            }
            mn.instructions.insertBefore(mn.instructions.getFirst(), getRandomFlow());
            Arrays.stream(mn.instructions.toArray()).forEach((insn) -> {
                if(insn.getOpcode() == Opcodes.BIPUSH || insn.getOpcode() == Opcodes.SIPUSH || insn.getOpcode() == Opcodes.LDC){
                    if(Obfuscator.getInstance().r.nextInt( 2) == 1){
                        for(int i = 0; i < 2 + Obfuscator.getInstance().r.nextInt(5); i++){
                            mn.instructions.insert(insn, getRandomFlow());
                        }
                    }
                }
            });
        });
    }
}
