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
 */

package net.doubledoordev.d3commands.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;

/**
 * Created by Wout on 27/10/2014.
 */
public class Location
{
    private ChunkCoordinates coordinates;
    private int dimension;

    public Location(ChunkCoordinates coordinates, int dimension)
    {
        this.setCoordinates(coordinates);
        this.setDimension(dimension);
    }

    public Location(int x, int y, int z, int dimensionId)
    {
        this(new ChunkCoordinates(x, y, z), dimensionId);
    }

    public Location(Entity entity)
    {
        this(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ), entity.dimension);
    }

    public ChunkCoordinates getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(ChunkCoordinates coordinates)
    {
        this.coordinates = new ChunkCoordinates(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public int getDimension()
    {
        return dimension;
    }

    public void setDimension(int dimension)
    {
        this.dimension = dimension;
    }

    public IChatComponent toClickableChatString()
    {
        return new ChatComponentText("[" + coordinates.posX + "X " + coordinates.posY + "Y " + coordinates.posZ + "Z]")
                .setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpx " + dimension + " " + coordinates.posX + " " + coordinates.posY + " " + coordinates.posZ)));
    }

    public void teleport(EntityPlayerMP player)
    {
        player.mountEntity(null);
        if (player.dimension != dimension)
        {
            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimension);
        }
        player.setPositionAndUpdate(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public void set(int x, int y, int z)
    {
        this.coordinates.posX = x;
        this.coordinates.posY = y;
        this.coordinates.posZ = z;
    }

    public void setX(int x)
    {
        this.coordinates.posX = x;
    }

    public void setY(int y)
    {
        this.coordinates.posY = y;
    }

    public void setZ(int z)
    {
        this.coordinates.posZ = z;
    }

    public int getX()
    {
        return coordinates.posX;
    }

    public int getY()
    {
        return coordinates.posY;
    }

    public int getZ()
    {
        return coordinates.posZ;
    }

    public int deltaX(int delta)
    {
        return (this.coordinates.posX += delta);
    }

    public int deltaY(int delta)
    {
        return (this.coordinates.posY += delta);
    }

    public int deltaZ(int delta)
    {
        return (this.coordinates.posZ += delta);
    }
}
