package dev.bodyalhoha.souvenir.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public abstract class Transformer implements Opcodes {

    public abstract void run(ClassNode cn);
}
