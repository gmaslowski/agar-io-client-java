import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

public class Application {

    private static BufferedReader reader;
    private static BufferedWriter writer;

    public static void main(String[] args) throws Exception {

        Config config = ConfigFactory.defaultApplication();

        String playerName = checkNotNull(config.getString("agar-io.player-name"));
        String password = checkNotNull(config.getString("agar-io.password"));
        String hostname = checkNotNull(config.getString("agar-io.hostname"));
        int port = checkNotNull(config.getInt("agar-io.port"));
        checkArgument(!isNullOrEmpty(playerName + password), "player-Name and password must be set in application.conf to connect to server.");

        Socket tcpSocket = new Socket(hostname, port);

        reader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(tcpSocket.getOutputStream()));

        AgarIoServerGateway serverGateway = new AgarIoServerGateway(writer, reader);
        serverGateway.login(playerName, password);
        serverGateway.joinPlayer();

        AgarIoPlayer agarIoPlayer = new AgarIoPlayer(serverGateway, playerName);

        agarIoPlayer.gameLoop();
    }
}
