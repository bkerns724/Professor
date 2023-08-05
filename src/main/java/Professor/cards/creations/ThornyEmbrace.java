package Professor.cards.creations;

import Professor.cards.abstracts.AbstractCreationCard;
import Professor.util.CardArtRoller;
import Professor.util.KeywordManager;
import Professor.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import static Professor.MainModfile.makeID;

public class ThornyEmbrace extends AbstractCreationCard {
    public final static String ID = makeID(ThornyEmbrace.class.getSimpleName());

    public ThornyEmbrace() {
        this(null);
    }

    public ThornyEmbrace(ElementData data) {
        super(ID, 1, CardType.POWER, CardRarity.SPECIAL, CardTarget.SELF);
        updateElementData(data);
        addCustomKeyword(KeywordManager.THORNY_EMBRACE);
    }

    @Override
    public void updateElementData(ElementData data) {
        baseMagicNumber = magicNumber = 5;
        if (data != null) {
            baseMagicNumber += data.g;
            baseMagicNumber += data.y;
            magicNumber = baseMagicNumber;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        if (data != null) {
            return new ThornyEmbrace(data.cpy());
        }
        return super.makeCopy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new ThornsPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(3);
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, mix(Color.FOREST, Color.GRAY), WHITE, mix(Color.FOREST, Color.GRAY), WHITE, false);
    }

    @Override
    public String cardArtCopy() {
        return VoidCard.ID;
    }

    @Override
    public String itemArt() {
        return ThornyEmbrace.class.getSimpleName();
    }
}