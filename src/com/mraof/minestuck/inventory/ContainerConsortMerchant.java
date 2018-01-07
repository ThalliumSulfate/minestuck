package com.mraof.minestuck.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerConsortMerchant extends Container
{
	public InventoryConsortMerchant inventory;
	
	private EntityPlayer player;
	
	public ContainerConsortMerchant(EntityPlayer player, InventoryConsortMerchant inv)
	{
		this(player);
		setInventory(inv);
	}
	
	public ContainerConsortMerchant(EntityPlayer player)
	{
		this.player = player;
	}
	
	public void setInventory(InventoryConsortMerchant inv)
	{
		inventory = inv;
		
		for(int i = 0; i < 9; i++)
			this.addSlotToContainer(new SlotConsortMerchant(inventory, i, 17 + 35*(i%3), 35 + 33*(i/3)));
		
		for(IContainerListener listener : this.listeners)
			listener.sendAllWindowProperties(this, inventory);
	}
	
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		if(inventory != null)
			listener.sendAllWindowProperties(this, inventory);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.player == playerIn;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data)
	{
		inventory.setField(id, data);
	}
}
