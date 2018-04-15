package com.caxerx.mc.dynamicdungeon.command.parser;

import com.sk89q.worldedit.math.transform.AffineTransform;

public class TransformingParser {
    public static AffineTransform parse(String trnstr) {
        trnstr = trnstr.toLowerCase();
        AffineTransform trn = new AffineTransform();
        if (trnstr.startsWith("scale")) {
            trn.scale(Double.parseDouble(trnstr.replaceFirst("scale ", "")));
        } else if (trnstr.startsWith("rotatex")) {
            trn.rotateX(Double.parseDouble(trnstr.replaceFirst("rotatex ", "")));
        } else if (trnstr.startsWith("rotatey")) {
            trn.rotateY(Double.parseDouble(trnstr.replaceFirst("rotatey ", "")));
        } else if (trnstr.startsWith("rotatez")) {
            trn.rotateZ(Double.parseDouble(trnstr.replaceFirst("rotatez ", "")));
        }
        return trn;
    }
}
