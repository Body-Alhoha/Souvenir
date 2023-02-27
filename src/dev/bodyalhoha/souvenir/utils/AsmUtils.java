package dev.bodyalhoha.souvenir.utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class AsmUtils {

    public static AbstractInsnNode pushInt(int value) {
        if (value >= -1 && value <= 5) {
            return new InsnNode(Opcodes.ICONST_0 + value);
        }
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            return new IntInsnNode(Opcodes.BIPUSH, value);
        }
        if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            return new IntInsnNode(Opcodes.SIPUSH, value);
        }
        return new LdcInsnNode(value);
    }

    public static int getPushedInt(AbstractInsnNode insn) {
        int op = insn.getOpcode();
        if (op >= Opcodes.ICONST_M1 && op <= Opcodes.ICONST_5) {
            return op - Opcodes.ICONST_0;
        }
        if (op == Opcodes.BIPUSH || op == Opcodes.SIPUSH) {
            return ((IntInsnNode) insn).operand;
        }
        if (op == Opcodes.LDC) {
            Object cst = ((LdcInsnNode) insn).cst;
            if (cst instanceof Integer) {
                return (int) cst;
            }
        }
        throw new IllegalArgumentException("insn is not a push int instruction");
    }

    public static long getAmountOf(int opcode, MethodNode mn){
        return Arrays.stream(mn.instructions.toArray()).filter(insn -> insn.getOpcode() == opcode).count();
    }
    public static long getAmountOf(int opcode, ClassNode cn){
        int amount = 0;
        for(MethodNode mn : cn.methods){
            amount += Arrays.stream(mn.instructions.toArray()).filter(insn -> insn.getOpcode() == opcode).count();
        }
        return amount;
    }

}
