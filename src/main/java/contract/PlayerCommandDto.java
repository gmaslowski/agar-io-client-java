package contract;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayerCommandDto.SplitPlayerCommandDto.class, name = "Split"),
        @JsonSubTypes.Type(value = PlayerCommandDto.EjectMassPlayerCommandDto.class, name = "EjectMass"),
        @JsonSubTypes.Type(value = PlayerCommandDto.GetViewPlayerCommandDto.class, name = "GetView"),
        @JsonSubTypes.Type(value = PlayerCommandDto.JoinPlayerCommandDto.class, name = "Join"),
        @JsonSubTypes.Type(value = PlayerCommandDto.MovePlayerCommandDto.class, name = "Move")
})
@JsonSerialize
public class PlayerCommandDto {

    protected PlayerCommandDto(PlayerCommandType type) {
        this.type = type;
    }

    public PlayerCommandType type;

    public static class SplitPlayerCommandDto extends PlayerCommandDto {
        public SplitPlayerCommandDto() {
            super(PlayerCommandType.Split);
        }
    }

    public static class MovePlayerCommandDto extends PlayerCommandDto {

        public double dx;
        public double dy;

        public MovePlayerCommandDto(double destinationX, double destinationY) {
            super(PlayerCommandType.Move);
            this.dx = destinationX;
            this.dy = destinationY;
        }

        public MovePlayerCommandDto() {
            this(0.0, 0.0);
        }
    }

    public static class EjectMassPlayerCommandDto extends PlayerCommandDto {
        public EjectMassPlayerCommandDto() {
            super(PlayerCommandType.EjectMass);
        }
    }

    public static class GetViewPlayerCommandDto extends PlayerCommandDto {
        public GetViewPlayerCommandDto() {
            super(PlayerCommandType.GetView);
        }
    }

    public static class JoinPlayerCommandDto extends PlayerCommandDto {

        public JoinPlayerCommandDto() {
            super(PlayerCommandType.Join);
        }

        public JoinPlayerCommandDto(UUID playerId) {
            this();
            this.playerId = playerId;
        }

        public UUID playerId;
    }
}