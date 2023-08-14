package Professor.cardmods;

import Professor.util.CalcHelper;
import Professor.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractInfusion extends AbstractCardModifier implements DynvarInterface {
    public enum InfusionType {
        DAMAGE_DIRECT,
        DAMAGE_RANDOM,
        DAMAGE_ALL,
        BLOCK,
        MAGIC
    }
    public InfusionType type;
    public String key;
    public int val;
    public int[] multiVal;
    public int baseVal;
    public boolean valModified;
    public boolean valUpgraded;
    public transient Texture modIcon;
    public String modText;
    public int relicStatsVal;

    public AbstractInfusion(String key, InfusionType type, int baseAmount, String modText, Texture modIcon) {
        this.key = key;
        this.type = type;
        this.baseVal = this.val = baseAmount;
        this.modText = modText;
        this.modIcon = modIcon;
    }

    @Override
    public List<String> extraDescriptors(AbstractCard card) {
        if (type == InfusionType.DAMAGE_ALL || type == InfusionType.DAMAGE_RANDOM || type == InfusionType.DAMAGE_DIRECT) {
            if (card.type != AbstractCard.CardType.ATTACK) {
                return Collections.singletonList(AbstractCard.TEXT[0]);
            }
        }
        return super.extraDescriptors(card);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (priority < 0) {
            return FormatHelper.insertBeforeText(rawDescription , String.format(modText, descriptionKey()));
        } else {
            return FormatHelper.insertAfterText(rawDescription , String.format(modText, descriptionKey()));
        }
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        switch (type) {
            case DAMAGE_DIRECT:
            case DAMAGE_RANDOM:
                val = CalcHelper.applyPowers(baseVal);
                break;
            case DAMAGE_ALL:
                multiVal = CalcHelper.applyPowersMulti(baseVal);
                val = multiVal[0];
                break;
            case BLOCK:
                val = CalcHelper.applyPowersToBlock(baseVal);
                break;
            case MAGIC:
                break;
        }
        valModified = val != baseVal;
    }

    @Override
    public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
        switch (type) {
            case DAMAGE_DIRECT:
                val = CalcHelper.calculateCardDamage(baseVal, mo);
                break;
            case DAMAGE_RANDOM:
                val = CalcHelper.calculateCardDamage(baseVal, null);
                break;
            case DAMAGE_ALL:
                multiVal = CalcHelper.calculateCardDamageMulti(baseVal);
                val = multiVal[0];
                break;
            case BLOCK:
                val = CalcHelper.applyPowersToBlock(baseVal);
                break;
            case MAGIC:
                break;
        }
        valModified = val != baseVal;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        val = baseVal;
        valModified = false;
        return false;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        ArrayList<AbstractCardModifier> mods = CardModifierManager.getModifiers(card, key);
        if (!mods.isEmpty()) {
            AbstractInfusion mod = (AbstractInfusion) mods.get(0);
            mod.relicStatsVal += this.relicStatsVal;
            mod.baseVal += this.baseVal;
            mod.val = mod.baseVal;
            return false;
        }
        return true;
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(modIcon).text(String.valueOf(baseVal)).textOffsetX(3).drawColor(new Color(1, 1, 1, card.transparency)).render(card);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(modIcon).text(String.valueOf(baseVal)).textOffsetX(6).drawColor(new Color(1, 1, 1, card.transparency)).render(card);
    }

    public String descriptionKey() {
        return "!"+key+"!";
    }

    @Override
    public String identifier(AbstractCard card) {
        return key;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public int val(AbstractCard card) {
        return val;
    }

    @Override
    public int baseVal(AbstractCard card) {
        return baseVal;
    }

    @Override
    public boolean modified(AbstractCard card) {
        return valModified;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return valUpgraded;
    }

    public static boolean usesVanillaTargeting(AbstractCard card) {
        return card.target == AbstractCard.CardTarget.ENEMY ||
                card.target == AbstractCard.CardTarget.ALL_ENEMY ||
                card.target == AbstractCard.CardTarget.SELF ||
                card.target == AbstractCard.CardTarget.NONE ||
                card.target == AbstractCard.CardTarget.SELF_AND_ENEMY ||
                card.target == AbstractCard.CardTarget.ALL;
    }
}
