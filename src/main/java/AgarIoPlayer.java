import contract.*;

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
                    .filter(blob -> playerName.equals(blob.name))
                    .findFirst()
                    .orElseGet(() -> defaultBlobDto());

            double destinationX = ThreadLocalRandom.current().nextDouble(-255, 256);
            double destinationY = ThreadLocalRandom.current().nextDouble(-255, 256);

            BlobDto closestFood = newArrayList(getViewResponseDto.blobs)
                    .stream()
                    .filter(blob -> blob.type == BlobType.Food)
                    .sorted((b1, b2) -> blobCompare(b1, b2, myBlob))
                    .findFirst()
                    .orElseGet(() -> defaultBlobDto());

            if (closestFood != null) {
                destinationX = closestFood.position.x - myBlob.position.x;
                destinationY = closestFood.position.y - myBlob.position.y;
            }

            serverGateway.move(destinationX * 1000, destinationY * 1000);
        }
    }

    private BlobDto defaultBlobDto() {
        BlobDto blobDto = new BlobDto();
        blobDto.position = new VectorDto();
        return blobDto;
    }

    private int blobCompare(BlobDto b1, BlobDto b2, BlobDto myBlob) {
        double b1d = Math.sqrt(Math.pow(b1.position.x - myBlob.position.x, 2) + Math.pow(b1.position.y - myBlob.position.y, 2));
        double b2d = Math.sqrt(Math.pow(b2.position.x - myBlob.position.x, 2) + Math.pow(b2.position.y - myBlob.position.y, 2));
        return b1d > b2d ? -1 : 1;
    }
}
