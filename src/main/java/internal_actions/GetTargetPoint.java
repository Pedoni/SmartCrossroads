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

        // Use a more efficient lookup if possible
        var point = Utils.map.values().stream()
                .filter(s -> s.getPosX() == x && s.getPosY() == y)
                .findFirst()
                .get();

        var points = point.getDestinations();
        if (points.size() > 0) {
            int index = new Random().nextInt(points.size());
            var target = Utils.map.get(points.get(index));

            // First, remove the old target belief using proper Literal creation
            Literal oldTarget = Literal.parseLiteral("target(_, _)");
            agent.abolish(oldTarget, un);

            // Create and add the new target belief
            Literal targetBelief = Literal.parseLiteral(
                    String.format("target(%d, %d)", target.getPosX(), target.getPosY()));
            agent.addBel(targetBelief);
        } else {
            // Handle the case where there are no destinations
            Literal oldTarget = Literal.parseLiteral("target(_, _)");
            agent.abolish(oldTarget, un);
            agent.addBel(Literal.parseLiteral("target(-1, -1)"));
        }
        return true;
    }
}