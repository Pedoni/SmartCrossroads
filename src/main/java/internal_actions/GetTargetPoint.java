package internal_actions;

import java.util.Random;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import jason.util.Pair;
import model.view_elements.Tile;
import utils.Direction;
import utils.Utils;

public class GetTargetPoint extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent agent = ts.getAg();
        int x = (int) ((NumberTerm) args[0]).solve();
        int y = (int) ((NumberTerm) args[1]).solve();
        int d = (int) ((NumberTerm) args[2]).solve();
        Direction dir = Direction.values()[d];
        Tile tile = new Tile(x, y);

        var points = Utils.getDirections().get(new Pair<>(tile, dir));
        if (points.size() > 0) {
            int index = new Random().nextInt(points.size());
            var tileDir = points.get(index);
            var target = tileDir.getFirst();
            var newDir = tileDir.getSecond();

            Literal oldDirection = Literal.parseLiteral("direction(_)");
            agent.abolish(oldDirection, un);

            Literal directionBelief = Literal.parseLiteral(
                    String.format("direction(%d)", newDir.ordinal()));
            agent.addBel(directionBelief);

            Literal oldTarget = Literal.parseLiteral("target(_, _)");
            agent.abolish(oldTarget, un);

            Literal targetBelief = Literal.parseLiteral(
                    String.format("target(%d, %d)", target.getPosX(), target.getPosY()));
            agent.addBel(targetBelief);
        } else {
            Literal oldDirection = Literal.parseLiteral("direction(_)");
            agent.abolish(oldDirection, un);
            Literal oldTarget = Literal.parseLiteral("target(_, _)");
            agent.abolish(oldTarget, un);
            agent.addBel(Literal.parseLiteral("target(-1, -1)"));
        }
        return true;
    }
}