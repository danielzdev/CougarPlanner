package csusm.cougarplanner.transitions;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class ExponentialTransitionTranslation extends Transition {
    private final Node node;
    private final double startValue;
    private final double endValue;

    public ExponentialTransitionTranslation(Node node, double startValue, double endValue, Duration duration) {
        this.node = node;
        this.startValue = startValue;
        this.endValue = endValue;
        setCycleDuration(duration);
    }

    @Override
    protected void interpolate(double fraction) {
        //parabolic exponential growth/decay function - y = - (end - start) * (x - 1)^(2) + end
        double value = (-1 * (endValue - startValue) * Math.pow((fraction - 1),2)) + endValue;

        node.setTranslateX(value);
    }
}
