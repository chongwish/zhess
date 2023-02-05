package me.chongwish.zhess.appearance.pokemon;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

import me.chongwish.zhess.bot.codename.Apple;
import me.chongwish.zhess.game.classic.Game;
import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.imagination.Thinking;
import me.chongwish.zhess.stuff.virtual.Move;
import me.chongwish.zhess.stuff.virtual.Player;

public class Main extends SimpleApplication {
    private Game game;
    private Model model;
    private Piece[] pieces;
    private Board board;

    private Piece currentPiece;
    private Player currentPlayer;

    private Apple appleBot;

    private static float RATIO = 1.5f;

    private Material selectedMaterial;
    private Material movabMaterial;
    private Material defauMaterial;

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Pokemon Chinese Chess");

        Main app = new Main();
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initColor();
        initPointer();
        initKeys(); 
        initGame();
        initView();
    }

    private void initColor() {
        viewPort.setBackgroundColor(ColorRGBA.LightGray);

        selectedMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        movabMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        defauMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        defauMaterial.setColor("Color", ColorRGBA.Gray);
        selectedMaterial.setColor("Color", ColorRGBA.Yellow);
        movabMaterial.setColor("Color", ColorRGBA.Cyan);
    }

    private void initPointer() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setColor(ColorRGBA.Green);
        ch.setText("*");
        ch.setLocalTranslation(settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

    private void initKeys() {
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(new ActionListener() {
            public void onAction(String name, boolean isPressed, float tpf) {
                if (game.isEnd() || !currentPlayer.isHuman() || !isPressed) {
                    return ;
                }

                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                rootNode.collideWith(ray, results);
                if (results.size() <= 0) {
                    return ;
                }

                Geometry target = results.getClosestCollision().getGeometry();

                if ("Board".equals(target.getName())) {
                    int id = target.getUserData("id");
                    movePiece(id);
                } else if ("Piece".equals(target.getParent().getName())) {
                    int id = target.getParent().getUserData("id");
                    if (pieces[id].getBoard() != null) {
                        selectPiece(id);
                    }
                }
            };
        }, "Click");
    }

    private void movePiece(int position) {
        if (currentPiece == null) {
            return ;
        }
        for (int p: currentPiece.getNextPositions()) {
            if (p == position) {
                Piece oldPiece = board.atPoint(position);

                Move move = currentPlayer.thought();
                move.setPieceId(currentPiece.getId());
                move.setPosition(position);
                currentPlayer.move(move);
                currentPlayer = currentPlayer.getCompetitor();
                currentPiece = null;

                cleanBoard();

                if (oldPiece != null) {
                    rootNode.detachChild(model.getPet(oldPiece.getId()));
                }

                model.getPet(move.getPieceId()).setLocalTranslation(move.getPosition() % 9 * RATIO, 0, move.getPosition() / 9 * RATIO);
                return ;
            }
        }
    }

    private void selectPiece(int id) {
        if (pieces[id].getColor().equals(currentPlayer.getPieceColor())) {
            cleanBoard();

            currentPiece = pieces[id];
            int position = currentPiece.getPosition();

            model.getFloor(position).setMaterial(selectedMaterial);

            for (int p : currentPiece.getNextPositions()) {
                model.getFloor(p).setMaterial(movabMaterial);
            }
        } else {
            if (currentPiece != null) {
                movePiece(pieces[id].getPosition());
            }
        }
    }

    private void initGame() {
        game = new Game();
        model = new Model(assetManager);

        game.hookInit(this::getReadyForGame);

        appleBot = new Apple(pieces);

        game.hookPlayerInit((redPlayer, blackPlayer) -> {
            redPlayer.thinkLike(Thinking.Human(this::humanAction));
            blackPlayer.unsetHumanFlag();
            blackPlayer.thinkLike(Thinking.Bot(this::botAction, 500));

            currentPlayer = redPlayer;
        });
    }

    private void initView() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1f,-1f,0).normalizeLocal());
        rootNode.addLight(sun);

        rootNode.setLocalTranslation(-4 * RATIO, -3 * RATIO, -8 * RATIO);
    }

    private void getReadyForGame(Piece[] pieces, Board board) {
        this.pieces = pieces;
        this.board = board;

        for (int i = 0; i < 90; ++i) {
            rootNode.attachChild(model.getFloor(i));
        }

        for (Piece piece: pieces) {
            Spatial spatial = model.getPet(piece.getId());
            spatial.setLocalTranslation(piece.getPosition() % 9 * RATIO, 0, piece.getPosition() / 9 * RATIO);
            rootNode.attachChild(spatial);
        }
    }

    private Move humanAction(Player player) {
        return new Move();
    }

    private Move botAction(Player player) {
        return appleBot.sync(player.getPieces()).think(player.getPieceColor());
    }

    private void cleanBoard() {
        for (Geometry floor: model.getFloors()) {
            floor.setMaterial(defauMaterial);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!currentPlayer.isHuman()) {
            Move move = currentPlayer.thought();
            Piece oldPiece = board.atPoint(move.getPosition());
            currentPlayer.move(move);
            currentPlayer = currentPlayer.getCompetitor();

            cleanBoard();

            if (oldPiece != null) {
                rootNode.detachChild(model.getPet(oldPiece.getId()));
            }

            model.getPet(move.getPieceId()).setLocalTranslation(move.getPosition() % 9 * RATIO, 0, move.getPosition() / 9 * RATIO);
        }
    }
}

