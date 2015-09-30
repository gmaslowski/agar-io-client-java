package contract;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public enum CommandErrorCode {
    Success,
    WrongLogin,
    CommandsLimitExceeded,
    GameNotStarted,
    NotJoined,
    TooManyBlobs,
    TooLowMass,
    AlreadyJoined;

    public static CommandErrorCode fromTypeCode(final int typeCode) {
        switch (typeCode) {
            case 0:
                return Success;
            case 1:
                return WrongLogin;
            case 100:
                return CommandsLimitExceeded;
            case 101:
                return GameNotStarted;
            case 102:
                return NotJoined;
            case 103:
                return TooManyBlobs;
            case 104:
                return TooLowMass;
            case 105:
                return AlreadyJoined;
        }
        throw new IllegalArgumentException("Invalid Status type code: " + typeCode);
    }

    public static class CommandErrorCodeDeserializer extends JsonDeserializer<CommandErrorCode> {
        @Override
        public CommandErrorCode deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            return CommandErrorCode.fromTypeCode(parser.getValueAsInt());
        }
    }
}

