package net.alxndr2000.tutorialmod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EightBallItem extends Item {

    public EightBallItem(Properties properties) {
        super(properties);
    }
    public int LuckRange = 10;
    @Override
    public InteractionResult useOn(UseOnContext context) {



        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {

            BlockPos blockpos = context.getClickedPos();
            BlockState blockstate = level.getBlockState(blockpos);

            // Item dupes based on luck

            int luckyNumber = getRandomNumber(LuckRange);

            if (luckyNumber>=7) {

                // Duplicate the block if lucky

                level.addDestroyBlockEffect(blockpos, blockstate);
                level.destroyBlock(blockpos, true);
                level.setBlockAndUpdate(blockpos, blockstate);
                player.getCooldowns().addCooldown(this, 10);

                outputLuckyNumber(player, true, luckyNumber);
            } else {

                // Destroy the block if unlucky

                level.addDestroyBlockEffect(blockpos, blockstate);
                level.destroyBlock(blockpos, false);
                player.getCooldowns().addCooldown(this, 5);

                outputLuckyNumber(player, false, luckyNumber);
            }

            ItemStack UsedBall = player.getItemInHand(hand);
            UsedBall.setDamageValue(UsedBall.getDamageValue() + luckyNumber);
            if (UsedBall.getDamageValue() >= UsedBall.getMaxDamage()) UsedBall.setCount(0);
            // Output Random Number between 1 and 10


        }








        return super.useOn(context);
    }



    private void outputLuckyNumber(Player player, boolean luck, int luckyNumber) {
        if (luck) {
            player.sendSystemMessage(Component.literal("Your Lucky Number is " + luckyNumber));
        } else {
            player.sendSystemMessage(Component.literal("Your Unlucky Number is " + luckyNumber));
        }


    }
    private int getRandomNumber (int range) {
        return RandomSource.createNewThreadLocalInstance().nextInt(range)+1;
    }


}
