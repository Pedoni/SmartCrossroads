package internal_actions;

import java.util.Random;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import utils.Utils;

public class GetTargetPoint extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent agent = ts.getAg();
        int x = (int) ((NumberTerm) args[0]).solve();
        int y = (int) ((NumberTerm) args[1]).solve();
        var point = Utils.map.values().stream().filter(s -> s.getPosX() == x && s.getPosY() == y)
                .findFirst()
                .get();
        var points = point.getDestinations();
        int index = new Random().nextInt(points.size());
        var target = Utils.map.get(points.get(index));
        final int targetX = target.getPosX();
        final int targetY = target.getPosY();
        agent.addBel(Literal.parseLiteral("target(" + targetX + ", " + targetY + ")"));
        return true;
    }
}
