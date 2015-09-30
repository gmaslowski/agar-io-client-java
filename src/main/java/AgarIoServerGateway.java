import contract.CommandResponseDto;
import contract.GetViewResponseDto;
import contract.LoginDto;
import contract.PlayerCommandDto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import static com.google.common.base.Throwables.propagate;

public class AgarIoServerGateway {

    private final BufferedWriter writer;
    private final BufferedReader reader;

    public AgarIoServerGateway(BufferedWriter writer, BufferedReader reader) {
        this.writer = writer;
        this.reader = reader;
    }

    public void joinPlayer() {
        command(new PlayerCommandDto.JoinPlayerCommandDto(), CommandResponseDto.class);
    }

    public void login(String playerName, String password) {
        command(new LoginDto(playerName, password), CommandResponseDto.class);
    }

    public GetViewResponseDto getView() {
        return command(new PlayerCommandDto.GetViewPlayerCommandDto(), GetViewResponseDto.class);
    }

    public void move(double destinationX, double destinationY) {
        executeCommand(new PlayerCommandDto.MovePlayerCommandDto(destinationX, destinationY));
    }

    private <DTO> DTO command(Object dto, Class<DTO> clazz) {
        String responseJson = executeCommand(dto);
        return JsonWrapper.fromJson(responseJson, clazz);
    }

    private String executeCommand(Object dto) {
        String commandJson = JsonWrapper.toJson(dto);
        System.out.println("AgarIo request:" + commandJson); // debug
        writeLine(commandJson);
        String responseJson = readLine();
        System.out.println("AgarIo response:" + responseJson); // debug
        return responseJson;
    }

    private String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    private void writeLine(String line) {
        try {
            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw propagate(e);
        }
    }
}
