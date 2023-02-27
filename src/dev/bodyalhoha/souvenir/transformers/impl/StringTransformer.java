package dev.bodyalhoha.souvenir.transformers.impl;

import dev.bodyalhoha.souvenir.Obfuscator;
import dev.bodyalhoha.souvenir.transformers.Transformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class StringTransformer extends Transformer {

    public static String encrypt(String s, int v){
        StringBuilder a = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            a.append((char)((int)s.toCharArray()[i] + v));
        }
        return a.toString();
    }

    public static String decrypt(String s, int v){
        StringBuilder a = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            a.append((char)((int)s.toCharArray()[i] - v));
        }
        return a.toString();
    }

    public MethodNode makeStringDecrypt(String name){
        MethodNode methodVisitor = new MethodNode(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC, name, "(Ljava/lang/String;I)Ljava/lang/String;", null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitLineNumber(25, label0);
        methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        methodVisitor.visitVarInsn(ASTORE, 2);
        Label label1 = new Label();
        methodVisitor.visitLabel(label1);
        methodVisitor.visitLineNumber(26, label1);
        methodVisitor.visitInsn(ICONST_0);
        methodVisitor.visitVarInsn(ISTORE, 3);
        Label label2 = new Label();
        methodVisitor.visitLabel(label2);
        methodVisitor.visitFrame(Opcodes.F_APPEND, 2, new Object[]{"java/lang/StringBuilder", Opcodes.INTEGER}, 0, null);
        methodVisitor.visitVarInsn(ILOAD, 3);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "length", "()I", false);
        Label label3 = new Label();
        methodVisitor.visitJumpInsn(IF_ICMPGE, label3);
        Label label4 = new Label();
        methodVisitor.visitLabel(label4);
        methodVisitor.visitLineNumber(27, label4);
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C", false);
        methodVisitor.visitVarInsn(ILOAD, 3);
        methodVisitor.visitInsn(CALOAD);
        methodVisitor.visitVarInsn(ILOAD, 1);
        methodVisitor.visitInsn(ISUB);
        methodVisitor.visitInsn(I2C);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(C)Ljava/lang/StringBuilder;", false);
        methodVisitor.visitInsn(POP);
        Label label5 = new Label();
        methodVisitor.visitLabel(label5);
        methodVisitor.visitLineNumber(26, label5);
        methodVisitor.visitIincInsn(3, 1);
        methodVisitor.visitJumpInsn(GOTO, label2);
        methodVisitor.visitLabel(label3);
        methodVisitor.visitLineNumber(29, label3);
        methodVisitor.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        methodVisitor.visitInsn(ARETURN);
        Label label6 = new Label();
        methodVisitor.visitLabel(label6);
        methodVisitor.visitLocalVariable("i", "I", null, label2, label3, 3);
        methodVisitor.visitLocalVariable("s", "Ljava/lang/String;", null, label0, label6, 0);
        methodVisitor.visitLocalVariable("v", "I", null, label0, label6, 1);
        methodVisitor.visitLocalVariable("a", "Ljava/lang/StringBuilder;", null, label1, label6, 2);
        methodVisitor.visitMaxs(3, 4);
        methodVisitor.visitEnd();

        return methodVisitor;
    }

    @Override
    public void run(ClassNode cn) {
        if((cn.access & ACC_INTERFACE) == ACC_INTERFACE) {
            return;
        }
        AtomicBoolean has = new AtomicBoolean(false);
        MethodNode decryptMethod = makeStringDecrypt(Obfuscator.getInstance().r.nextInt(1000) + "");
        cn.methods.forEach(mn -> {

            Arrays.stream(mn.instructions.toArray()).forEach(insn -> {
                if(insn.getOpcode() == Opcodes.LDC && ((LdcInsnNode)insn).cst instanceof String){
                    LdcInsnNode ldc = (LdcInsnNode) insn;
                    if(ldc.cst instanceof String){
                        int decryptValue = Obfuscator.getInstance().r.nextInt(30) + 6;
                        has.set(true);
                        ldc.cst = encrypt((String)ldc.cst, decryptValue);
                        mn.instructions.insert(ldc, new MethodInsnNode(Opcodes.INVOKESTATIC, cn.name, decryptMethod.name, "(Ljava/lang/String;I)Ljava/lang/String;", false));
                        mn.instructions.insert(ldc, new IntInsnNode(BIPUSH, decryptValue));

                    }
                }
            });
        });

        if(has.get()){
            cn.methods.add(decryptMethod);
        }
    }
}
