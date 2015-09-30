package contract;

public enum CommandErrorCode {
    Success(0),
    WrongLogin(1),
    CommandsLimitExceeded(100),
    GameNotStarted(2),
    NotJoined(3),
    TooManyBlobs(4),
    TooLowMass(5),
    AlreadyJoined(6);

    private final int code;

    CommandErrorCode(int i) {
        this.code = i;
    }
}
