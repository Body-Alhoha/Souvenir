package dev.bodyalhoha.souvenir.transformers.impl.string;

import dev.bodyalhoha.souvenir.Obfuscator;
import dev.bodyalhoha.souvenir.transformers.Transformer;
import dev.bodyalhoha.souvenir.transformers.impl.string.impl.CaesarEncryption;
import dev.bodyalhoha.souvenir.transformers.impl.string.impl.XorEncryption;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StringTransformer extends Transformer {

    public List<IStringEncryptionMethod> methods = Arrays.asList(new CaesarEncryption(), new XorEncryption());

    @Override
    public void run(ClassNode cn) {
        if((cn.access & ACC_INTERFACE) == ACC_INTERFACE) {
            return;
        }
        AtomicBoolean has = new AtomicBoolean(false);
        IStringEncryptionMethod encryption = methods.get(Obfuscator.getInstance().r.nextInt(methods.size()));
        MethodNode decryptMethod = encryption.createDecrypt(Obfuscator.getInstance().r.nextInt(1000) + "");
        cn.methods.forEach(mn -> {

            Arrays.stream(mn.instructions.toArray()).forEach(insn -> {
                if(insn.getOpcode() == Opcodes.LDC && ((LdcInsnNode)insn).cst instanceof String){
                    LdcInsnNode ldc = (LdcInsnNode) insn;
                    if(ldc.cst instanceof String){
                        int decryptValue = Obfuscator.getInstance().r.nextInt(30) + 6;
                        has.set(true);
                        ldc.cst = encryption.encrypt((String)ldc.cst, decryptValue);
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
