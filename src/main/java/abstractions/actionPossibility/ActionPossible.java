package abstractions.actionPossibility;

public class ActionPossible implements IActionPossible {
    @Override
    public boolean isPossible() {
        return true;
    }

    @Override
    public String reason() {
        return "";
    }
}
