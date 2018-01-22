package cf.brforgers.mods.IndustrialExpansion.items;

import cf.brforgers.mods.IndustrialExpansion.manager.ItemManager;
import cofh.api.energy.IEnergyContainerItem;
import cofh.core.item.ItemArmorAdv;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.StringHelper;
import cf.brforgers.mods.IndustrialExpansion.helper.DurabillityHelper;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraft.potion.Potion;

public class QuantumArmor extends ItemArmorAdv implements ISpecialArmor, IEnergyContainerItem {

    /* priority 优先级
     * ratio 比率
     * AbsorbMax 最大吸收值*/
    public static final ArmorProperties QUANTUM = new ArmorProperties(0, 0.2D, Integer.MAX_VALUE);



    /**
     * 装甲总能量
     */
    public int maxEnergy = 2000000;

    /**最大充电速度*/
    public int maxTransfer = 1000;

    public double absorbRatio = 1.0E7D; //0.9D
    /**
     * 每点伤害扣除的RF
     */
    public int energyPerDamage = 20000;
    private int speedLevel;
    private int damageBoostLevel;
    private int nightVisionLevel;
    private int jumpLevel;
    private boolean enableLegsEffect;
    private boolean enablePlateEffect;
    private boolean enableHelmetEffect;
    private boolean enableBootsEffect;

//    public String[] textures = new String[2];

    public QuantumArmor(ArmorMaterial material, int type) {

        super(material, type);
    }

    public QuantumArmor setEnergyParams(int maxEnergy, int maxTransfer) {

        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;

        return this;
    }

    public QuantumArmor setEnergyParams(int maxEnergy, int maxTransfer, int energyPerDamage) {
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
        this.energyPerDamage = energyPerDamage;

        return this;
    }

    public QuantumArmor setEffectSwitch(boolean enableLegsEffect ,boolean enablePlateEffect,boolean enableHelmetEffect,boolean enableBootsEffect){
        this.enableLegsEffect = enableLegsEffect;
        this.enablePlateEffect = enablePlateEffect;
        this.enableHelmetEffect = enableHelmetEffect;
        this.enableBootsEffect = enableBootsEffect;

        return this;
    }


    public QuantumArmor setEffectLevel(int damageBoostLevel , int nightVisionLevel , int speedLevel,int jumpVisionLevel) {
        this.damageBoostLevel = damageBoostLevel;
        this.speedLevel = speedLevel;
        this.nightVisionLevel = nightVisionLevel;
        this.jumpLevel = jumpVisionLevel;

        return this;
    }


    /**返回这个ItemStack是否能被铁砧修复*/
    @Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack) {

        return false;
    }


    @Override
    /**根据装备稀有等级返回颜色枚举*/
    public EnumRarity getRarity(ItemStack stack) {

        return EnumRarity.uncommon;
    }

    @Override
    /**添加物品NBT信息（显示状态信息用）*/
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean check) {

        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {//关闭shift显示细节 && !按下了shift键
            list.add(StringHelper.shiftForDetails());//增加"按下shift查看更多"的信息
        }
        if (!StringHelper.isShiftKeyDown()) {//!按下了shift键
            return;
        }
        if (stack.stackTagCompound == null) {//物品NBT属性为空
            EnergyHelper.setDefaultEnergyTag(stack, 0);//设置为默认电量
        }
        //增加 剩余电量信息
        list.add(StringHelper.localize("info.cofh.charge") + ": " + stack.stackTagCompound.getInteger("Energy") + " / " + maxEnergy + " RF");
    }

    /**设置这个ItemStack的伤害为damage*/
    @Override
    public void setDamage(ItemStack stack, int damage) {

        super.setDamage(stack, 0);
    }

    @Override
    public int getDisplayDamage(ItemStack stack) {

        if (stack.stackTagCompound == null) {
            EnergyHelper.setDefaultEnergyTag(stack, 0);
        }
        return maxEnergy - stack.stackTagCompound.getInteger("Energy");
    }

    @Override
    /**获得这个ItemStack的最大伤害*/
    public int getMaxDamage(ItemStack stack) {

        return maxEnergy;
    }

    @Override
    /**是否显示耐久*/
    public boolean showDurabilityBar(ItemStack stack) {

        return !DurabillityHelper.showArmorCharge ? false : stack.stackTagCompound == null || !stack.stackTagCompound.getBoolean("CreativeTab");
    }

    @Override
    /**如果这个itemstack被损坏，则返回true。 只有在isDamageable() 为true的情况下才会调用。*/
    public boolean isDamaged(ItemStack stack) {

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    /**返回具有相同ID但不同元的项目列表（例如：染料会返回16项）*/
    public void getSubItems(Item item, CreativeTabs tab, List list) {

        list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, 0), 0));
        list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, 0), maxEnergy));
    }

    /*获得基础吸收值*/
    protected int getBaseAbsorption() {

        return 20;
    }

    /**
     * Returns a % that each piece absorbs, set sums to 100.
     * 返回每件装备吸收伤害的百分比，将总和为100
     */
    protected int getAbsorptionRatio() {

        switch (armorType) {
            case 0:
                return 25;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 5;
        }
        return 0;
    }

    /**返回每点伤害扣除的能量*/
    protected int getEnergyPerDamage(ItemStack stack) {

        int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack), 0, 4);
        return energyPerDamage * (5 - unbreakingLevel) / 5;
    }

    /**检索在计算装甲伤害时使用的修饰符。 更高优先级的装甲将在低优先级之前对其施加损害。 如果有多件同等优先级的装甲，则根据吸收率分配它们之间的伤害。
     * @param player 具有生命值的实体
     * @param armor 装甲的ItemStack实例
     * @param source 伤害来源
     * @param damage 对实体造成的总伤害
     * @param slot 装甲所在的装备槽
     * @return 返回装甲的受损信息 ArmorProperties 实例。
     * */
    @Override       /* ISpecialArmor */         
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {

        if (source.damageType.equals("quantum")) {//伤害类型是否为quantum（量子）类型
            return QUANTUM;
        } else if (source.isUnblockable()) {
            int absorbMax = getEnergyPerDamage(armor) > 0 ? 25 * getEnergyStored(armor) / getEnergyPerDamage(armor) : 0;
            return new ArmorProperties(0, absorbRatio * getArmorMaterial().getDamageReductionAmount(armorType) * 0.025, absorbMax);
        }
        int absorbMax = getEnergyPerDamage(armor) > 0 ? 25 * getEnergyStored(armor) / getEnergyPerDamage(armor) : 0;
        return new ArmorProperties(0, absorbRatio * getArmorMaterial().getDamageReductionAmount(armorType) * 0.05, absorbMax);
        // 0.05 = 1 / 20 (max armor)
    }


    /**获得显示的装甲*/
    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        if (getEnergyStored(armor) >= getEnergyPerDamage(armor)) {
            return Math.min(getBaseAbsorption(), 20) * getAbsorptionRatio() / 100;
        }
        return 0;
    }


    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack armor) {
        if (world.isRemote) return;
        if ( getEnergyStored(armor) >= getEnergyPerDamage(armor)) {    //装甲剩余电量 >= 装甲造成伤害扣除的电量
            if (enableLegsEffect && armor.getItem() == ItemManager.itemLegsQuantum) {         //裤子
                player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 260, speedLevel, true));   //获得速度效果
            }
            if (enablePlateEffect && armor.getItem() == ItemManager.itemPlateQuantum) {           //PotionEffect(效果ID，持续时间，效果等级，是否是范围的？)
                player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 260, damageBoostLevel, true));  //伤害提升
            }
            if (enableHelmetEffect && armor.getItem() == ItemManager.itemHelmetQuantum) {
                player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 260, nightVisionLevel, true));
            }
            if (enableBootsEffect && armor.getItem() == ItemManager.itemBootsQuantum){
                player.addPotionEffect(new PotionEffect(Potion.jump.id,260, jumpLevel,true));
            }
        }
    }
    /**对指定的ItemStack施加伤害。 该方法负责减少物品的耐用性和堆叠数量。 如果堆叠已经耗尽，它将被自动清理。*/
    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack armor, DamageSource source, int damage, int slot) {

        if (source.damageType.equals("quantum")) {
            boolean p = source.getEntity() == null;
            receiveEnergy(armor, damage * (p ? energyPerDamage / 2 : getEnergyPerDamage(armor)), false);
        } else {
            extractEnergy(armor, damage * getEnergyPerDamage(armor), false);
        }
    }

    /* IEnergyContainerItem 获得能量*/
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

        if (container.stackTagCompound == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        int stored = container.stackTagCompound.getInteger("Energy");
        int receive = Math.min(maxReceive, Math.min(maxEnergy - stored, maxTransfer));

        if (!simulate) {
            stored += receive;
            container.stackTagCompound.setInteger("Energy", stored);
        }
        return receive;
    }

    /*提取能量*/
    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

        if (container.stackTagCompound == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        int stored = container.stackTagCompound.getInteger("Energy");
        int extract = Math.min(maxExtract, stored);

        if (!simulate) {
            stored -= extract;
            container.stackTagCompound.setInteger("Energy", stored);
        }
        return extract;
    }

    /**获得装甲剩余的电量*/
    @Override
    public int getEnergyStored(ItemStack container) {

        if (container.stackTagCompound == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        return container.stackTagCompound.getInteger("Energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {

        return maxEnergy;
    }
}
