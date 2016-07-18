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

package net.doubledoordev.d3commands.entry;

import net.doubledoordev.d3commands.D3Commands;
import net.minecraft.command.ICommand;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public abstract class CommandEntry
{
    private final String name;
    private final boolean forceDisable;
    protected boolean enabled;

    public CommandEntry(String name, String... modIds)
    {
        this.name = name;
        boolean forceDisable = false;
        if (modIds != null)
        {
            for (String modId : modIds)
            {
                if (!Loader.isModLoaded(modId))
                {
                    forceDisable = true;
                    D3Commands.getLogger().info("Command {} forced disabled because mod requirements are not met. {} is not loaded. Full list: {}", getUniqueName(), modId, modIds);
                    break;
                }
            }
        }
        this.forceDisable = forceDisable;
    }

    public boolean isEnabled()
    {
        return !forceDisable && enabled;
    }

    public abstract ICommand getInstance();

    public abstract void doConfig(Configuration configuration);

    public String getUniqueName()
    {
        return name;
    }
}
