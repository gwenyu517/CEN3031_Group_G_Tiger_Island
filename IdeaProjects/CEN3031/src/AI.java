import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by mquac on 4/8/2017.
 */

public class AI {
    public Game game;
    private String gameID;
    private boolean gameEnd = false;

    private List<int[]> validTilePlacement = new Vector<int[]>();
    private List<int[]> buildSettlement = new Vector<int[]>();
    private List<int[]> expandSettlement = new Vector<int[]>();
    private List<int[]> buildTotoro= new Vector<int[]>();
    private List<int[]> buildTiger = new Vector<int[]>();


    public AI() {

    }

    public void setGameID(String gameID) { this.gameID = gameID; }

    public String getGameID() { return gameID; }

    public void startNewGame() {
        game = new Game();
        resetLists();
        updateLists();
    }

    private void updateLists(){
        updateValidTilePlacement();
        updateBuildSettlement();
        updateExpandSettlement();
        updateBuildTotoro();
        updateBuildTiger();
    }

    private void updateValidTilePlacement() {
        for (Hex hex : game.board.playedHexes) {
            int[] currentArr = new int[4];
            Tile temp = new Tile(TileType.JJ);
            for (int i = 0; i < 6; i++) {
                temp.setOrientation(i);
                currentArr[0] = hex.indexX;
                currentArr[1] = hex.indexY;
                currentArr[2] = i;
                for (int j = 0; j < 3; j++) {
                    currentArr[3] = j;
                    if (game.board.isPlacementValid(temp, hex, j)) {
                        if(!validTilePlacement.contains(currentArr)) {
                            validTilePlacement.add(currentArr);
                        }
                        else if (validTilePlacement.contains(currentArr)) {
                            validTilePlacement.remove(currentArr);
                        }
                    }
                }
            }
        }
    }

    private void updateBuildSettlement() {
        for (Hex hex : game.board.playedHexes) {
            int[] currentArr = new int[2];
            currentArr[0] = hex.indexX;
            currentArr[1] = hex.indexY;
            if (game.build.isBuildSettlementValid(hex, game.player1.getMeepleCount())) {
                if(!buildSettlement.contains(currentArr)) {
                    buildSettlement.add(currentArr);
                }
                else if(buildSettlement.contains(currentArr)) {
                    buildSettlement.remove(currentArr);
                }
            }
        }
    }

    private void updateExpandSettlement(){
        for (Settlement s : game.board.settlementList) {
            int[] currentArr = new int[3];
            Hex hex = s.hexesInSettlement.get(0);
            currentArr[0] = hex.indexX;
            currentArr[1] = hex.indexY;
            for(int i = 0; i < 4; i++) {
                currentArr[2] = i;
                if (game.build.isExpandSettlementValid(game.board, hex.getSettlementID(), hex.getTerrain(), game.player1.getMeepleCount())) {
                    if(!expandSettlement.contains(currentArr)) {
                        expandSettlement.add(currentArr);
                    }
                    else if(expandSettlement.contains(currentArr)) {
                        expandSettlement.remove(currentArr);
                    }
                }
            }
        }

    }

    private void updateBuildTotoro() {
        for (Hex hex : game.board.playedHexes) {
            int[] currentArr = new int[2];
            currentArr[0] = hex.indexX;
            currentArr[1] = hex.indexY;
            if(game.build.isBuildTotoroSanctuaryValid(game.board, hex, game.player1)) {
                if(!buildTotoro.contains(currentArr)) {
                    buildTotoro.add(currentArr);
                }
                else if(buildTotoro.contains(currentArr)) {
                    buildTotoro.remove(currentArr);
                }
            }
        }
    }

    private void updateBuildTiger() {
        for (Hex hex : game.board.playedHexes) {
            int[] currentArr = new int[2];
            currentArr[0] = hex.indexX;
            currentArr[1] = hex.indexY;
            if(game.build.isBuildTigerSanctuaryValid(game.board, hex, game.player1)) {
                if(!buildTiger.contains(currentArr)) {
                    buildTiger.add(currentArr);
                }
                else if(buildTiger.contains(currentArr)) {
                    buildTiger.remove(currentArr);
                }
            }
        }
    }

    private void resetLists(){
        buildSettlement = new Vector<int[]>();
        expandSettlement = new Vector<int[]>();
        buildTotoro= new Vector<int[]>();
        buildTiger = new Vector<int[]>();
        validTilePlacement = new Vector<int[]>();
    }

    public String playMove(Tile tile, int time) {
        Random rand = new Random();
        int n = rand.nextInt(validTilePlacement.size());
        int x = validTilePlacement.get(n)[0];
        x = x - game.board.rootHex.indexX;
        int y = validTilePlacement.get(n)[1];
        y = y - game.board.rootHex.indexY;
        int ornt = validTilePlacement.get(n)[2];
        game.board.placeTile(tile, game.board.hexArr[validTilePlacement.get(n)[0]][validTilePlacement.get(n)[1]], ornt);
       // chooseTileToPlace();
        updateLists();
        int xb = -1, yb = -1 , bNum = -1;
        Terrain t = null;
        n = rand.nextInt(4);
        switch (n){
            case 0: n = rand.nextInt(buildSettlement.size());
                xb = buildSettlement.get(n)[0];
                xb = xb - game.board.rootHex.indexX;
                yb = buildSettlement.get(n)[1];
                yb = yb - game.board.rootHex.indexY;
                bNum = 0;
                game.playMove(0, buildSettlement.get(n)[0], buildSettlement.get(n)[1]);
                break;
            case 1: n = rand.nextInt(expandSettlement.size());
                xb = expandSettlement.get(n)[0];
                xb = xb - game.board.rootHex.indexX;
                yb = expandSettlement.get(n)[1];
                yb = yb - game.board.rootHex.indexY;
                bNum = 1;
                t = Terrain.values()[expandSettlement.get(n)[2]];
                game.playMove(1, expandSettlement.get(n)[0], expandSettlement.get(n)[1], t);
                break;
            case 2: n = rand.nextInt(buildTotoro.size());
                xb = buildTotoro.get(n)[0];
                xb = xb - game.board.rootHex.indexX;
                yb = buildTotoro.get(n)[1];
                yb = yb - game.board.rootHex.indexY;
                bNum = 2;
                game.playMove(2, buildTotoro.get(n)[0], buildTotoro.get(n)[1]);
                break;
            case 3: n = rand.nextInt(buildTiger.size());
                xb = buildTiger.get(n)[0];
                xb = xb - game.board.rootHex.indexX;
                yb = buildTiger.get(n)[1];
                yb = yb - game.board.rootHex.indexY;
                bNum = 3;
                game.playMove(2, buildTiger.get(n)[0], buildTotoro.get(n)[1]);
                break;
        }
       // chooseBuildOption(); // int array
       // int x = x + game.board.rootHex.indexX;
       // int y = y + game.board.rootHex.indexY;
        //game.playBuildOption(x, y);
        updateLists();

        String temp = tile.getTileTypeString() + " " + x + " " + y + " " + ornt + " ";
        temp += bNum + " " + xb + " " + yb;
        if(bNum == 1){
            temp+= " " + t.getTerrainText();
        }
        //% size of array - 1

        // x = x - game.board.rootHex.indexX

        return temp;//+ ;
    }

    public void updateEnemyMove(Tile t, int x, int y, int connectingHex, int buildOptionNumber, int xb, int yb) {
        x = x + game.board.rootHex.indexX;
        y = y + game.board.rootHex.indexY;
        xb = xb + game.board.rootHex.indexX;
        yb = yb + game.board.rootHex.indexY;
        game.board.placeTile(t, game.board.hexArr[x][y], connectingHex);
        game.setBuildOption(buildOptionNumber, xb, yb);
    }

    public void updateEnemyMove(Tile t, int x, int y, int connectingHex, int buildOptionNumber, int xb, int yb, Terrain terrainType) {
        game.board.placeTile(t, game.board.hexArr[x][y], connectingHex);
        game.setBuildOption(buildOptionNumber, xb, yb, terrainType);
    }

    public void endGame() {
        gameEnd = true;
    }

}