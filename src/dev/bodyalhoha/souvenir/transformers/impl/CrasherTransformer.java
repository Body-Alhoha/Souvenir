package dev.bodyalhoha.souvenir.transformers.impl;

import dev.bodyalhoha.souvenir.transformers.Transformer;
import dev.bodyalhoha.souvenir.utils.RandomUtils;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;

public class CrasherTransformer extends Transformer {
    @Override
    public void run(ClassNode cn) {
        if((cn.access & ACC_INTERFACE) == ACC_INTERFACE) {
            return;
        }
        AnnotationNode an = new AnnotationNode(RandomUtils.getRandomAscii(6));
        if(cn.invisibleAnnotations == null)
            cn.invisibleAnnotations = new ArrayList<>();
        cn.invisibleAnnotations.add(an);


    }
}
