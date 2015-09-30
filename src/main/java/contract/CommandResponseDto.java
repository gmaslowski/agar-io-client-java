package contract;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CommandResponseDto {
    @JsonDeserialize(using = CommandErrorCode.CommandErrorCodeDeserializer.class)
    public CommandErrorCode errorCode;
    public String message;
}
