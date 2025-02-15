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
        double x = ((NumberTerm) args[0]).solve();
        double y = ((NumberTerm) args[1]).solve();
        var point = Utils.map.values().stream().filter(s -> s.getX() == x && s.getY() == y)
                .findFirst()
                .get();
        var points = point.getDestinations();
        int index = new Random().nextInt(points.size());
        var target = Utils.map.get(points.get(index));
        final double targetX = target.getX();
        final double targetY = target.getY();
        agent.addBel(Literal.parseLiteral("target(" + targetX + ", " + targetY + ")"));
        return true;
    }
}
