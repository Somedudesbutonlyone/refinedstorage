package com.raoulvdberge.refinedstorage.container;

import com.raoulvdberge.refinedstorage.container.slot.SlotFilterFluid;
import com.raoulvdberge.refinedstorage.tile.TileFluidStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFluidStorage extends ContainerBase {
    public ContainerFluidStorage(TileFluidStorage fluidStorage, EntityPlayer player) {
        super(fluidStorage, player);

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new SlotFilterFluid(!fluidStorage.getWorld().isRemote, fluidStorage.getNode().getFilters(), i, 8 + (18 * i), 20));
        }

        addPlayerInventory(8, 141);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        Slot slot = getSlot(index);

        if (slot.getHasStack() && index >= 8) {
            return mergeItemStackToFilters(slot.getStack(), 0, 9);
        }

        return ItemStack.EMPTY;
    }
}
