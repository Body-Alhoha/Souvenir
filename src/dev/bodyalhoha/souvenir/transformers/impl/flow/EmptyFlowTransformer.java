package dev.bodyalhoha.souvenir.transformers.impl.flow;

import dev.bodyalhoha.souvenir.Obfuscator;
import dev.bodyalhoha.souvenir.transformers.Transformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.stream.Stream;

public class EmptyFlowTransformer extends Transformer {




    public InsnList getFlow(int amount){
        InsnList gflow = new InsnList();
        for(int i = 0; i < amount; i++){
            LabelNode label = new LabelNode();
            boolean v = Obfuscator.getInstance().r.nextBoolean();
            gflow.add(new InsnNode(v ? Opcodes.ICONST_1 : Opcodes.ICONST_0));
            gflow.add(new JumpInsnNode(v ? Opcodes.IFNE : Opcodes.IFEQ, label));
            gflow.add(new InsnNode(ACONST_NULL));
            gflow.add(new InsnNode(ATHROW));
            gflow.add(label);
        }
        return gflow;
    }

    public InsnList getSwitches(){
        InsnList gflow = new InsnList();
        LabelNode bLabel = new LabelNode();
        LabelNode cLabel = new LabelNode();
        LabelNode end = new LabelNode();

        int a = Obfuscator.getInstance().r.nextInt(100) + 10;
        int b = a - Obfuscator.getInstance().r.nextInt(20) - 5;
        int c = b - Obfuscator.getInstance().r.nextInt(20) - 5;
        LookupSwitchInsnNode switchNode = new LookupSwitchInsnNode(end, Stream.of(c, b).mapToInt(i -> i).toArray(), Arrays.asList(bLabel, cLabel).toArray(new LabelNode[2]));
        gflow.add(new IntInsnNode(Opcodes.BIPUSH, a));
        gflow.add(switchNode);
        gflow.add(bLabel);
        gflow.add(new JumpInsnNode(Opcodes.GOTO, end));
        gflow.add(cLabel);
        gflow.add(new JumpInsnNode(Opcodes.GOTO, end));


        gflow.add(end);
        return gflow;
    }

    @Override
    public void run(ClassNode cn) {
        if((cn.access & ACC_INTERFACE) == ACC_INTERFACE) {
            return;
        }
        cn.methods.forEach(mn -> {

            if(mn.instructions.size() == 0)
                return;


            mn.instructions.insertBefore(mn.instructions.getFirst(), getFlow(1));


            Arrays.stream(mn.instructions.toArray()).forEach((insn) -> {
                if(insn == null) return;
                if(insn.getOpcode() == Opcodes.GOTO){
                    InsnList flow = new InsnList();
                    flow.add(new InsnNode(Opcodes.ICONST_0));
                    flow.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) insn).label));
                    flow.add(new InsnNode(ACONST_NULL));
                    flow.add(new InsnNode(ATHROW));
                    mn.instructions.insertBefore(insn, flow);
                    mn.instructions.remove(insn);

                }

                if(insn instanceof MethodInsnNode){
                    if(Obfuscator.getInstance().r.nextInt(3) == 2){
                        if(Obfuscator.getInstance().r.nextBoolean()){
                            mn.instructions.insertBefore(insn, getSwitches());
                        }else{
                            mn.instructions.insertBefore(insn,getFlow(1));

                        }

                    }


                }
            });


        });
    }
}
