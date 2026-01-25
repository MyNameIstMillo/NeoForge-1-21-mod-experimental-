package net.mynameistmillo.experimentalmod.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.WandLogic.Compact.CompactSpells;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.wand.GetSpells;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.wand.SaveSpells;
import net.mynameistmillo.experimentalmod.Enum.SaveOrGetTypeW;
import net.mynameistmillo.experimentalmod.block.entity.ModBlockEntities;
import net.mynameistmillo.experimentalmod.items.custom.WandItem;
import net.mynameistmillo.experimentalmod.screen.custom.WandEditorMenu;
import net.mynameistmillo.experimentalmod.data.ModTags;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.checkerframework.checker.nullness.qual.Nullable;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WandEditorEntity extends BlockEntity implements MenuProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    public final ItemStackHandler inventory = new ItemStackHandler(30){
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }

            //LOGGER.info("onContentsChanged -> ");
            if(slot == 28 || slot == 29){
                ItemStack wand = inventory.getStackInSlot(27);
                ItemStack key = inventory.getStackInSlot(slot);

                if(level.isClientSide()) return;
                if(wand.isEmpty() && key.isEmpty()) return;
                if(!(wand.getItem() instanceof WandItem wandItem)) return;

                if(key.is(ModTags.Items.KEY_ITEM)) {

                    switch (slot){
                        case 28 -> {
                            sendSpellsToWand(wand);
                        }
                        case 29 -> {
                            downloadSpellsFromWand(wandItem, wand);
                        }
                    }

                    ItemEntity entity = new ItemEntity(level,
                            worldPosition.getX() + 0.5,
                            worldPosition.getY() + 1.0,
                            worldPosition.getZ() + 0.5,
                            key.copy());
                    level.addFreshEntity(entity);
                    inventory.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }


        }
    };

    private float rotation;

    public WandEditorEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.WAND_EDITOR_BE.get(), pos, blockState);
    }

    public float getRenderingRotation(){
        rotation+=0.2f;
        if(rotation >= 360){
            rotation = 0;
        }
        return rotation;
    }



    private void sendSpellsToWand(ItemStack wand){
        if(!(wand.getItem() instanceof WandItem wandItem)) return;
        int capacity = wandItem.getCapacity(wand);
        if(wandItem.areThereSpellsInWand(wand, level)){
            playFeedbackSound(level, getBlockPos(), FeedbackType.FAIL);
            return;
        }

        NonNullList<ItemStack> spellsToSend = NonNullList.withSize(capacity, new ItemStack(Items.DIRT));

        for(int i=0; i<capacity; i++){
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack.is(Items.AIR)) continue;

            spellsToSend.set(i, stack);
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }

        if(spellsToSend.stream().anyMatch(stack -> !stack.is(Items.DIRT))){
            SaveSpells.saveSpells(wand, spellsToSend, this.level, spellsToSend.size(), SaveOrGetTypeW.NORMAL);
            //wandItem.compactSpells(wand, wandItem.resetSpellStats(spellsToSend), this.level);
            CompactSpells.compactSpells(wand, spellsToSend, this.level);

            playFeedbackSound(level, getBlockPos(), FeedbackType.SUCCESS);
        }
        else{
            playFeedbackSound(level, getBlockPos(), FeedbackType.FAIL);
        }

    }

    public void downloadSpellsFromWand(WandItem wandItem, ItemStack wand){
        int capacity = wandItem.getCapacity(wand);
        if(!wandItem.areThereSpellsInWand(wand, level)){
            playFeedbackSound(level, getBlockPos(), FeedbackType.FAIL);
            return;
        }
        List<ItemStack> storedSpells = GetSpells.getSpellsType(wand, this.level,
                                            wandItem.getCapacity(wand), SaveOrGetTypeW.NORMAL);
        //List<ItemStack> storedSpells = wandItem.getSavedSpells(wand, this.level);

        for(int i=0; i<capacity; i++){
            if(storedSpells.get(i).is(Items.DIRT)) continue;

            ItemStack itemInBlock = inventory.getStackInSlot(i);
            if(itemInBlock.isEmpty()){
                inventory.setStackInSlot(i, storedSpells.get(i).copy());
            }
            else {
                ItemEntity entity = new ItemEntity(level,
                        worldPosition.getX()+0.5,
                        worldPosition.getY()+1.0,
                        worldPosition.getZ()+0.5,
                        itemInBlock);
                level.addFreshEntity(entity);
                inventory.setStackInSlot(i, storedSpells.get(i).copy());
            }
            SaveSpells.saveWithDirt(wand, wandItem.getCapacity(wand));
        }
        playFeedbackSound(level, getBlockPos(), FeedbackType.SUCCESS);

    }

    public enum FeedbackType{
        SUCCESS,
        FAIL
    }

    public void playFeedbackSound(Level level, BlockPos pos, FeedbackType type){
        switch (type){
            case SUCCESS -> level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0f, 2.0f);
            case FAIL -> level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.2f, 0.7f);
        }
    }



    public void drops(){
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i=0; i<inventory.getSlots(); i++){
            inv.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("wand_editor_inventory", inventory.serializeNBT(registries));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Wand Editor");
    }

    @Override
    public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new WandEditorMenu(containerId, playerInventory, this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("wand_editor_inventory"));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}
