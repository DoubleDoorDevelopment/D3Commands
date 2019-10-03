
package net.doubledoordev.d3commands.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class FakePlayerExtraInventory implements IInventory
{
    private final List<NonNullList<ItemStack>> allInventories;
    private String name;

    public FakePlayerExtraInventory(EntityPlayerMP inv)
    {
        final NonNullList<ItemStack> armor;
        final NonNullList<ItemStack> offHand;
        final NonNullList<ItemStack> mainInv;
        final NonNullList<ItemStack> heldItemList = NonNullList.create();

        final ItemStack heldItem;
        heldItem = inv.inventory.getCurrentItem();
        heldItemList.add(heldItem);

        name = inv.getDisplayNameString();
        armor = inv.inventory.armorInventory;
        offHand = inv.inventory.offHandInventory;
        mainInv = inv.inventory.mainInventory;

        allInventories = Arrays.asList(mainInv, armor, offHand, heldItemList);
    }

    @Override
    public int getSizeInventory()
    {
        return 45;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        List<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : this.allInventories)
        {
            if (slot < nonnulllist.size())
            {
                list = nonnulllist;
                break;
            }

            slot -= nonnulllist.size();
        }

        return list == null ? ItemStack.EMPTY : list.get(slot);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        List<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : this.allInventories)
        {
            if (index < nonnulllist.size())
            {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }

        return list != null && !(list.get(index)).isEmpty() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        NonNullList<ItemStack> nonnulllist = null;

        for (NonNullList<ItemStack> nonnulllist1 : this.allInventories)
        {
            if (index < nonnulllist1.size())
            {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null && !(nonnulllist.get(index)).isEmpty())
        {
            ItemStack itemstack = nonnulllist.get(index);
            nonnulllist.set(index, ItemStack.EMPTY);
            return itemstack;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack itemStack)
    {

        NonNullList<ItemStack> nonnulllist = null;

        for (NonNullList<ItemStack> nonnulllist1 : this.allInventories)
        {
            if (index < nonnulllist1.size())
            {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null)
        {
            nonnulllist.set(index, itemStack);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(name);
    }
}
