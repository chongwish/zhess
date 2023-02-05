package me.chongwish.zhess.appearance.pokemon;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class Model {
    public Model(AssetManager assetManager) {
        pets[0] = assetManager.loadModel(CharizardPath);
        pets[1] = assetManager.loadModel(InfernapePath);
        pets[2] = assetManager.loadModel(InfernapePath);
        pets[3] = assetManager.loadModel(SnorlaxPath);
        pets[4] = assetManager.loadModel(SnorlaxPath);
        pets[5] = assetManager.loadModel(DragonitePath);
        pets[6] = assetManager.loadModel(DragonitePath);
        pets[7] = assetManager.loadModel(GengarPath);
        pets[8] = assetManager.loadModel(GengarPath);
        pets[9] = assetManager.loadModel(LucarioPath);
        pets[10] = assetManager.loadModel(LucarioPath);
        pets[11] = assetManager.loadModel(PikachuPath);
        pets[12] = assetManager.loadModel(PikachuPath);
        pets[13] = assetManager.loadModel(PikachuPath);
        pets[14] = assetManager.loadModel(PikachuPath);
        pets[15] = assetManager.loadModel(PikachuPath);

        pets[16] = assetManager.loadModel(RayquazaPath);
        pets[17] = assetManager.loadModel(GroudonPath);
        pets[18] = assetManager.loadModel(GroudonPath);
        pets[19] = assetManager.loadModel(KyogrePath);
        pets[20] = assetManager.loadModel(KyogrePath);
        pets[21] = assetManager.loadModel(KyuremPath);
        pets[22] = assetManager.loadModel(KyuremPath);
        pets[23] = assetManager.loadModel(ZekromPath);
        pets[24] = assetManager.loadModel(ZekromPath);
        pets[25] = assetManager.loadModel(ReshiramPath);
        pets[26] = assetManager.loadModel(ReshiramPath);
        pets[27] = assetManager.loadModel(MewPath);
        pets[28] = assetManager.loadModel(MewPath);
        pets[29] = assetManager.loadModel(MewPath);
        pets[30] = assetManager.loadModel(MewPath);
        pets[31] = assetManager.loadModel(MewPath);

        for (int i = 16; i < 32; ++i) {
            pets[i].rotate(0, FastMath.PI, 0);
        }

        for (int i = 0; i < 32; ++i) {
            pets[i].setName("Piece");
            pets[i].setUserData("id", i);
        }
        
        Material material = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Gray);
        Box box = new Box(0.4f, 0.1f, 0.4f);

        for (int x = 0; x < 9; ++x) {
            for (int z = 0; z < 10; ++z) {
                int i = z * 9 + x;
                floors[i] = new Geometry("Box", box);
                floors[i].setMaterial(material);
                floors[i].setLocalTranslation(x * RATIO, -0.1f * RATIO, z * RATIO);
            }
        }

        for (int i = 0; i < 90; ++i) {
            floors[i].setName("Board");
            floors[i].setUserData("id", i);
        }
    }

    private static float RATIO = 1.5f;

    /*
     * 帅 006 Charizard 喷火龙
     * 仕 392 Infernape 烈焰猴
     * 相 143 Snorlax 卡比兽
     * 俥 149 Dragonite 快龙
     * 傌 094 Gengar 耿鬼
     * 炮 448 Lucario 路卡利欧
     * 兵 025 Pikachu 皮卡丘
     * 将 384 Rayquaza 烈空坐
     * 士 383 Groudon 固拉多
     * 象 382 Kyogre 盖欧卡
     * 车 646 Kyurem 酋雷姆
     * 马 644 Zekrom 捷克罗姆
     * 砲 643 Reshiram 莱希拉姆
     * 卒 151 Mew 梦
     */

    // Red
    final private static String CharizardPath = "Models/Charizard/Charizard_Normal.obj";
    final private static String InfernapePath = "Models/Infernape/Infernape.obj";
    final private static String SnorlaxPath = "Models/Snorlax/Snorlax.obj";
    final private static String DragonitePath = "Models/Dragonite/Dragonite.obj";
    final private static String GengarPath = "Models/Gengar/Gengar_Normal.obj";
    final private static String LucarioPath = "Models/Lucario/Lucario_Normal.obj";
    final private static String PikachuPath = "Models/Pikachu/Pikachu_M.obj";
    // God
    final private static String RayquazaPath = "Models/Rayquaza/Rayquaza_Normal.obj";
    final private static String GroudonPath = "Models/Groudon/Groudon_Normal.obj";
    final private static String KyogrePath = "Models/Kyogre/Kyogre_Normal.obj";
    final private static String KyuremPath = "Models/Kyurem/Kyurem_Normal.obj";
    final private static String ZekromPath = "Models/Zekrom/Zekrom.obj";
    final private static String ReshiramPath = "Models/Reshiram/Reshiram.obj";
    final private static String MewPath = "Models/Mew/Mew.obj";

    private Spatial[] pets = new Spatial[32];
    private Geometry[] floors = new Geometry[90];

    public Spatial getPet(int i) {
        return pets[i];
    }

    public Spatial[] getPets() {
        return pets;
    }

    public Geometry getFloor(int i) {
        return floors[i];
    }

    public Geometry[] getFloors() {
        return floors;
    }
}
