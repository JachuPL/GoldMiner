package abstractions.actionPossibility;

public class ActionImpossible implements IActionPossible {

    private String _reason;
    public ActionImpossible(String reason) {
        _reason = reason;
    }

    @Override
    public boolean isPossible() {
        return false;
    }

    @Override
    public String reason() {
        return _reason;
    }
}
