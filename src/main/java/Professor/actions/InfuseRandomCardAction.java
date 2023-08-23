package Professor.actions;

import Professor.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InfuseRandomCardAction extends AbstractGameAction {
    private final AbstractCardModifier mod;
    private final Predicate<AbstractCard> filter;

    public InfuseRandomCardAction(int amount, AbstractCardModifier mod) {
        this(amount, mod, c -> true);
    }

    public InfuseRandomCardAction(int amount, AbstractCardModifier mod, Predicate<AbstractCard> filter) {
        this.mod = mod;
        this.amount = amount;
        this.filter = filter.and(InfuseCardsInHandAction.shenaniganFilter);
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> validCards = Wiz.adp().hand.group.stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0 ; i < amount ; i++) {
            AbstractCard c = Wiz.getRandomItem(validCards);
            InfuseCardsInHandAction.doInfusion(c, mod);
        }
        InfuseCardsInHandAction.infusionSFX();
        this.isDone = true;
    }
}
