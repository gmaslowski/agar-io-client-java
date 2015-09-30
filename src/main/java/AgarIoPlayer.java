import contract.BlobDto;
import contract.BlobType;
import contract.CommandErrorCode;
import contract.GetViewResponseDto;

import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.collect.Lists.newArrayList;

public class AgarIoPlayer {

    private final AgarIoServerGateway serverGateway;
    private final String playerName;

    public AgarIoPlayer(AgarIoServerGateway serverGateway, String playerName) {
        this.serverGateway = serverGateway;
        this.playerName = playerName;
    }

    public void gameLoop() {

        while (true) {
            GetViewResponseDto getViewResponseDto = serverGateway.getView();
            if (getViewResponseDto.errorCode == CommandErrorCode.NotJoined) {
                serverGateway.joinPlayer();
                continue;
            }

            BlobDto myBlob = newArrayList(getViewResponseDto.blobs)
                    .stream()
                    .filter(blob -> blob.name.equals(playerName))
                    .findFirst()
                    .orElseGet(() -> new BlobDto());

            double destinationX = ThreadLocalRandom.current().nextDouble(-255, 256);
            double destinationY = ThreadLocalRandom.current().nextDouble(-255, 256);

            BlobDto closestFood = newArrayList(getViewResponseDto.blobs)
                    .stream()
                    .filter(blob -> blob.type == BlobType.Food)
                    .sorted((b1, b2) -> blobCompare(b1, b2, myBlob))
                    .findFirst()
                    .orElseGet(() -> new BlobDto());

            if (closestFood != null) {
                destinationX = closestFood.position.x - myBlob.position.x;
                destinationY = closestFood.position.y - myBlob.position.y;
            }

            serverGateway.move(destinationX * 1000, destinationY * 1000);
        }
    }

    private int blobCompare(BlobDto b1, BlobDto b2, BlobDto myBlob) {
        double b1d = Math.sqrt(Math.pow(b1.position.x - myBlob.position.x, 2) + Math.pow(b1.position.y - myBlob.position.y, 2));
        double b2d = Math.sqrt(Math.pow(b2.position.x - myBlob.position.x, 2) + Math.pow(b2.position.y - myBlob.position.y, 2));
        return b1d > b2d ? -1 : 1;
    }
}
