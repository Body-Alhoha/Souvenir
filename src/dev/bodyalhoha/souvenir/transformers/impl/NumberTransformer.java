package dev.bodyalhoha.souvenir.transformers.impl;

import dev.bodyalhoha.souvenir.Obfuscator;
import dev.bodyalhoha.souvenir.transformers.Transformer;
import dev.bodyalhoha.souvenir.utils.AsmUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

public class NumberTransformer extends Transformer {

    public int b(){
        return 7;
    }

    public void a(){
        double a = 8.683696317015794 + b();
    }

    @Override
    public void run(ClassNode cn) {
        if((cn.access & ACC_INTERFACE) == ACC_INTERFACE) {
            return;
        }
        cn.methods.forEach(mn -> {
            long numbers = Arrays.stream(mn.instructions.toArray()).filter(insn -> insn instanceof IntInsnNode).count();
            if(numbers > 10000)
                return;
            Arrays.stream(mn.instructions.toArray()).forEach(insn -> {
                if(insn.getOpcode() == Opcodes.BIPUSH || insn.getOpcode() == Opcodes.SIPUSH){
                    int key = Obfuscator.getInstance().r.nextInt(10) + 10;
                    int seed = Obfuscator.getInstance().r.nextInt(10) + 10;

                    IntInsnNode integer = (IntInsnNode) insn;
                    if(Obfuscator.getInstance().r.nextBoolean()){
                        integer.operand ^= key;
                        mn.instructions.insert(integer, new InsnNode(Opcodes.IXOR));
                        if(Obfuscator.getInstance().r.nextBoolean()){
                            mn.instructions.insert(integer, new InsnNode(Opcodes.IADD));
                            mn.instructions.insert(integer, AsmUtils.pushInt(key - seed));
                            mn.instructions.insert(integer, AsmUtils.pushInt(seed));
                        }else{
                            mn.instructions.insert(integer, AsmUtils.pushInt(key));

                        }


                    }else{
                        integer.operand -= key;
                        mn.instructions.insert(integer, new InsnNode(Opcodes.IADD));
                        if(Obfuscator.getInstance().r.nextBoolean()){
                            mn.instructions.insert(integer, new InsnNode(Opcodes.IXOR));
                            mn.instructions.insert(integer, AsmUtils.pushInt(key ^ seed));
                            mn.instructions.insert(integer, AsmUtils.pushInt(seed));
                        }else{
                            mn.instructions.insert(integer, AsmUtils.pushInt(key));

                        }

                    }
                }
                if(insn.getOpcode() > Opcodes.ICONST_1 && insn.getOpcode() <= Opcodes.ICONST_5){
                    int key = Obfuscator.getInstance().r.nextInt(10) + 10;
                    int seed = Obfuscator.getInstance().r.nextInt(10) + 10;

                    int amount = insn.getOpcode() - Opcodes.ICONST_0;
                    InsnList l = new InsnList();
                    l.add(AsmUtils.pushInt(amount + key));
                    if(Obfuscator.getInstance().r.nextBoolean()){
                        l.add(AsmUtils.pushInt(-key ^ seed));
                        l.add(AsmUtils.pushInt(seed));
                        l.add(new InsnNode(Opcodes.IXOR));
                    }else{
                        l.add(AsmUtils.pushInt(-key));
                    }

                    l.add(new InsnNode(Opcodes.IADD));

                    mn.instructions.insert(insn, l);
                    mn.instructions.remove(insn);
                }

                if(insn.getOpcode() == Opcodes.LDC){
                    LdcInsnNode ldc = (LdcInsnNode) insn;
                    if(ldc.cst instanceof Double){
                        int key = Obfuscator.getInstance().r.nextInt(10) + 10;
                        int seed = Obfuscator.getInstance().r.nextInt(10) + 10;
                        double amount = (Double) ldc.cst;
                        InsnList l = new InsnList();
                        l.add(new LdcInsnNode(amount + key));
                        if(Obfuscator.getInstance().r.nextBoolean()){
                            l.add(AsmUtils.pushInt(-key ^ seed));
                            l.add(AsmUtils.pushInt(seed));
                            l.add(new InsnNode(Opcodes.IXOR));
                        }else{
                            l.add(AsmUtils.pushInt(-key));
                        }
                        l.add(new InsnNode(Opcodes.I2D));
                        l.add(new InsnNode(Opcodes.DADD));
                        mn.instructions.insert(insn, l);
                        mn.instructions.remove(insn);
                    }
                }
            });

        });
    }
}
