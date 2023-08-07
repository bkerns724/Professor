package Professor.relics;

import Professor.TheProfessor;

import static Professor.MainModfile.makeID;

public class EnlightenedBandana extends AbstractEasyRelic {
    public static final String ID = makeID(EnlightenedBandana.class.getSimpleName());

    public EnlightenedBandana() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, TheProfessor.Enums.MEDIUM_RUBY_COLOR);
    }
}
