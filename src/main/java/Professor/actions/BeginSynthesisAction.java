package Professor.actions;

import Professor.MainModfile;
import Professor.cards.abstracts.AbstractRecipeCard;
import Professor.patches.CustomTags;
import Professor.patches.EmpowerRedirectPatches;
import Professor.ui.SynthesisItem;
import Professor.ui.SynthesisPanel;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BeginSynthesisAction extends AbstractGameAction {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(MainModfile.makeID("SynthesisAction")).TEXT;
    protected final AbstractRecipeCard recipeCard;

    public BeginSynthesisAction(AbstractRecipeCard recipe) {
        this.recipeCard = recipe;
    }

    @Override
    public void update() {
        addToTop(new BetterSelectCardsInHandAction(recipeCard.getValance(), TEXT[0], false, false, c -> true, l -> {
            int draw = 0;
            SynthesisPanel.addSynthesisItem(new SynthesisItem(recipeCard, l));
            EmpowerRedirectPatches.setRedirect(recipeCard, SynthesisPanel.BASE_X, SynthesisPanel.BASE_Y);
            AbstractDungeon.player.hand.empower(recipeCard);
            recipeCard.fadingOut = true;
            for (AbstractCard c : l) {
                EmpowerRedirectPatches.setRedirect(c, SynthesisPanel.BASE_X, SynthesisPanel.BASE_Y);
                AbstractDungeon.player.hand.empower(c);
                if (c.hasTag(CustomTags.PROF_REACTANT)) {
                    draw++;
                }
            }
            l.clear();
            if (draw > 0) {
                addToTop(new DrawCardAction(draw));
            }
            //addToTop(new PerformSynthesisAction());
            }));
        this.isDone = true;
    }
}
