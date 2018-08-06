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

import com.google.common.base.Throwables;
import net.doubledoordev.d3commands.util.Constants;
import net.minecraft.command.ICommand;
import net.minecraftforge.common.config.Configuration;

/**
 * Uses lazy instantiation because it avoids class loading mods that may not exist.
 * Also it caches the command instance to prevent it needing to be remade every config reload. (no state loss)
 * This also helps prevent cluttering the command map/list, as MC uses both for some reason. Surely just a map would do?
 *  Side effects include: Possibility disable commands still show up in help list. (there is no command un-register)
 */
public class BasicCommandEntry extends CommandEntry
{
    private final Class<? extends ICommand> clazz;
    private final String name;
    private final boolean defaultEnabled;
    private final String comment;

    private ICommand instance;

    public BasicCommandEntry(Class<? extends ICommand> clazz, String name, boolean enabled, String comment, String... modIds)
    {
        super(clazz.getSimpleName(), modIds);
        this.clazz = clazz;
        this.name = name;
        this.defaultEnabled = enabled;
        this.enabled = enabled;
        this.comment = comment;
    }

    public ICommand getInstance()
    {
        if (instance == null && isEnabled())
        {
            try
            {
                instance = clazz.newInstance();
            }
            catch (Exception e)
            {
                Throwables.propagate(e);
            }
        }
        return instance;
    }

    @Override
    public void doConfig(Configuration configuration)
    {
        enabled = configuration.getBoolean(name, Constants.MODID, defaultEnabled, comment);
    }
}
