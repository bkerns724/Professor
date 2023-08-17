package Professor.powers;

import Professor.MainModfile;
import Professor.cards.AnotherPlanet;
import Professor.patches.ForcedUpgradesPatches;
import Professor.patches.ZeroAmountPowerPatches;
import Professor.powers.interfaces.OnUpgradePower;
import Professor.util.PowerIconMaker;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;

public class AnotherPlanetPower extends AbstractPower implements OnUpgradePower, ZeroAmountPowerPatches.ZeroAmountPower {
    public static final String POWER_ID = MainModfile.makeID(AnotherPlanetPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AnotherPlanetPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        PowerIconMaker.setIcons(this, AnotherPlanet.class.getSimpleName(), true);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount <= 0) {
            this.description = DESCRIPTIONS[0];
        } else {
            if (amount == 1) {
                this.description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
            } else {
                this.description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[3];
            }
        }
    }

    private static boolean looping = false;
    @Override
    public void onUpgrade(AbstractCard c) {
        if (!looping) {
            flash();
            ForcedUpgradesPatches.applyUnlockIfNeeded(c);
            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(c.hb.cX, c.hb.cY));
            c.superFlash();
            c.applyPowers();
            looping = true;
            for (int i = 0 ; i < amount ; i++) {
                c.upgrade();
                c.upgraded = false;
            }
            looping = false;
        }
    }
}