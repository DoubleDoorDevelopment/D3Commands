/*
 * Copyright (c) 2014-2016, Dries007 & DoubleDoorDevelopment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package net.doubledoordev.d3commands.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.concurrent.Immutable;

@Immutable
public class BlockPosDim extends BlockPos
{
    private final int dim;

    public BlockPosDim(int x, int y, int z, int dim)
    {
        super(x, y, z);
        this.dim = dim;
    }

    public BlockPosDim(double x, double y, double z, int dim)
    {
        super(x, y, z);
        this.dim = dim;
    }

    public BlockPosDim(Entity source)
    {
        super(source);
        this.dim = source.dimension;
    }

    public BlockPosDim(Vec3d vec, int dim)
    {
        super(vec);
        this.dim = dim;
    }

    public BlockPosDim(Vec3i source, int dim)
    {
        super(source);
        this.dim = dim;
    }

    public ITextComponent toClickableChatString()
    {
        return new TextComponentString("[" + getX() + " " + getY() + " " + getZ() + "]").setStyle(new Style().setItalic(true).setClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpx " + dim + " " + getX() + " " + getY() + " " + getZ())));
    }


}
