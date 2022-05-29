package com.github.unchama.seichiassist

import com.github.unchama.seichiassist.minestack.{GroupedMineStackObj, MineStackObj}
import com.github.unchama.seichiassist.minestack.MineStackObjectCategory._
import com.github.unchama.seichiassist.util.StaticGachaPrizeFactory
import org.bukkit.Material

object MineStackObjectList {

  private def leftElems[A](elems: A*): List[Either[A, Nothing]] = elems.toList.map(Left.apply)
  private def rightElems[B](elems: B*): List[Either[Nothing, B]] = elems.toList.map(Right.apply)

  // @formatter:off
  
  // 採掘可能ブロック
  private val minestacklistmine: List[Either[MineStackObj, GroupedMineStackObj]] = leftElems(
    new MineStackObj(ORES, "coal_ore", "石炭鉱石", 1, Material.COAL_ORE, 0),
    new MineStackObj(ORES, "coal", "石炭", 1, Material.COAL, 0),
    new MineStackObj(ORES, "coal_block", "石炭ブロック", 1, Material.COAL_BLOCK, 0),
    new MineStackObj(ORES, "coal_1", "木炭", 1, Material.COAL, 1),
    new MineStackObj(ORES, "iron_ore", "鉄鉱石", 1, Material.IRON_ORE, 0),
    new MineStackObj(ORES, "iron_ingot", "鉄インゴット", 1, Material.IRON_INGOT, 0),
    new MineStackObj(ORES, "iron_block", "鉄ブロック", 1, Material.IRON_BLOCK, 0),
    new MineStackObj(ORES, "quartz_ore", "ネザー水晶鉱石", 1, Material.NETHER_QUARTZ_ORE, 0),
    new MineStackObj(ORES, "quartz", "ネザー水晶", 1, Material.QUARTZ, 0),
    new MineStackObj(ORES, "gold_ore", "金鉱石", 1, Material.GOLD_ORE, 0),
    new MineStackObj(ORES, "gold_ingot", "金インゴット", 1, Material.GOLD_INGOT, 0),
    new MineStackObj(ORES, "gold_block", "金ブロック", 1, Material.GOLD_BLOCK, 0),
    new MineStackObj(ORES, "redstone_ore", "レッドストーン鉱石", 1, Material.REDSTONE_ORE, 0),
    new MineStackObj(ORES, "lapis_ore", "ラピスラズリ鉱石", 1, Material.LAPIS_ORE, 0),
    new MineStackObj(ORES, "lapis_lazuli", "ラピスラズリ", 1, Material.LAPIS_LAZULI, 4),
    new MineStackObj(ORES, "lapis_block", "ラピスラズリブロック", 1, Material.LAPIS_BLOCK, 0),
    new MineStackObj(ORES, "diamond_ore", "ダイヤモンド鉱石", 1, Material.DIAMOND_ORE, 0),
    new MineStackObj(ORES, "diamond", "ダイヤモンド", 1, Material.DIAMOND, 0),
    new MineStackObj(ORES, "diamond_block", "ダイヤモンドブロック", 1, Material.DIAMOND_BLOCK, 0),
    new MineStackObj(ORES, "emerald_ore", "エメラルド鉱石", 1, Material.EMERALD_ORE, 0),
    new MineStackObj(ORES, "emerald", "エメラルド", 1, Material.EMERALD, 0),
    new MineStackObj(ORES, "emerald_block", "エメラルドブロック", 1, Material.EMERALD_BLOCK, 0)
  )

  // モンスター+動物ドロップ
  private val minestacklistdrop: List[Either[MineStackObj,GroupedMineStackObj]] = leftElems(
    new MineStackObj(MOB_DROP, "ender_pearl", "エンダーパール", 1, Material.ENDER_PEARL, 0),
    new MineStackObj(MOB_DROP, "ender_eye", "エンダーアイ", 1, Material.ENDER_EYE, 0),
    new MineStackObj(MOB_DROP, "slime_ball", "スライムボール", 1, Material.SLIME_BALL, 0),
    new MineStackObj(MOB_DROP, "slime", "スライムブロック", 1, Material.SLIME_BLOCK, 0),
    new MineStackObj(MOB_DROP, "rotten_flesh", "腐った肉", 1, Material.ROTTEN_FLESH, 0),
    new MineStackObj(MOB_DROP, "bone", "骨", 1, Material.BONE, 0),
    new MineStackObj(MOB_DROP, "sulphur", "火薬", 1, Material.GUNPOWDER, 0),
    new MineStackObj(MOB_DROP, "arrow", "矢", 1, Material.ARROW, 0),
    new MineStackObj(MOB_DROP, "tipped_arrow", "鈍化の矢", 1, Material.TIPPED_ARROW, 0),
    new MineStackObj(MOB_DROP, "spider_eye", "蜘蛛の目", 1, Material.SPIDER_EYE, 0),
    new MineStackObj(MOB_DROP, "string", "糸", 1, Material.STRING, 0),
    new MineStackObj(MOB_DROP, "name_tag", "名札", 1, Material.NAME_TAG, 0),
    new MineStackObj(MOB_DROP, "lead", "リード", 1, Material.LEAD, 0),
    new MineStackObj(MOB_DROP, "glass_bottle", "ガラス瓶", 1, Material.GLASS_BOTTLE, 0),
    new MineStackObj(MOB_DROP, "gold_nugget", "金塊", 1, Material.GOLD_NUGGET, 0),
    new MineStackObj(MOB_DROP, "blaze_rod", "ブレイズロッド", 1, Material.BLAZE_ROD, 0),
    new MineStackObj(MOB_DROP, "blaze_powder", "ブレイズパウダー", 1, Material.BLAZE_POWDER, 0),
    new MineStackObj(MOB_DROP, "ghast_tear", "ガストの涙", 1, Material.GHAST_TEAR, 0),
    new MineStackObj(MOB_DROP, "magma_cream", "マグマクリーム", 1, Material.MAGMA_CREAM, 0),
    new MineStackObj(MOB_DROP, "prismarine_shard", "プリズマリンの欠片", 1, Material.PRISMARINE_SHARD, 0),
    new MineStackObj(MOB_DROP, "prismarine_crystals", "プリズマリンクリスタル", 1, Material.PRISMARINE_CRYSTALS, 0),
    new MineStackObj(MOB_DROP, "feather", "羽", 1, Material.FEATHER, 0),
    new MineStackObj(MOB_DROP, "leather", "革", 1, Material.LEATHER, 0),
    new MineStackObj(MOB_DROP, "rabbit_hide", "ウサギの皮", 1, Material.RABBIT_HIDE, 0),
    new MineStackObj(MOB_DROP, "rabbit_foot", "ウサギの足", 1, Material.RABBIT_FOOT, 0),
    new MineStackObj(MOB_DROP, "dragon_egg", "エンドラの卵", 1, Material.DRAGON_EGG, 0),
    new MineStackObj(MOB_DROP, "shulker_shell", "シュルカーの殻", 1, Material.SHULKER_SHELL, 0),
    new MineStackObj(MOB_DROP, "totem_of_undying", "不死のトーテム", 1, Material.TOTEM_OF_UNDYING, 0),
    new MineStackObj(MOB_DROP, "dragon_head", "エンダードラゴンの頭", 1, Material.DRAGON_HEAD, 5),
    new MineStackObj(MOB_DROP, "wither_skeleton_skull", "ウィザースケルトンの頭", 1, Material.WITHER_SKELETON_SKULL, 1),
    new MineStackObj(MOB_DROP, "stick", "棒", 1, Material.STICK, 0)
  )

  // 採掘で入手可能な農業系ブロック
  private val minestacklistfarm: List[Either[MineStackObj, GroupedMineStackObj]] = leftElems(
    new MineStackObj(AGRICULTURAL, "seeds", "種", 1, Material.WHEAT_SEEDS, 0),
    new MineStackObj(AGRICULTURAL, "apple", "リンゴ", 1, Material.APPLE, 0),
    new MineStackObj(AGRICULTURAL, "long_grass1", "草", 1, Material.GRASS, 1),
    new MineStackObj(AGRICULTURAL, "long_grass2", "シダ", 1, Material.FERN, 2),
    new MineStackObj(AGRICULTURAL, "dead_bush", "枯れ木", 1, Material.DEAD_BUSH, 0),
    new MineStackObj(AGRICULTURAL, "cactus", "サボテン", 1, Material.CACTUS, 0),
    new MineStackObj(AGRICULTURAL, "vine", "ツタ", 1, Material.VINE, 0),
    new MineStackObj(AGRICULTURAL, "water_lily", "スイレンの葉", 1, Material.LILY_PAD, 0),
    new MineStackObj(AGRICULTURAL, "yellow_flower", "タンポポ", 1, Material.DANDELION, 0),
    new MineStackObj(AGRICULTURAL, "red_rose0", "ポピー", 1, Material.POPPY, 0),
    new MineStackObj(AGRICULTURAL, "red_rose1", "ヒスイラン", 1, Material.BLUE_ORCHID, 1),
    new MineStackObj(AGRICULTURAL, "red_rose2", "アリウム", 1, Material.ALLIUM, 2),
    new MineStackObj(AGRICULTURAL, "red_rose3", "ヒナソウ", 1, Material.AZURE_BLUET, 3),
    new MineStackObj(AGRICULTURAL, "red_rose4", "赤色のチューリップ", 1, Material.RED_TULIP, 4),
    new MineStackObj(AGRICULTURAL, "red_rose5", "橙色のチューリップ", 1, Material.ORANGE_TULIP, 5),
    new MineStackObj(AGRICULTURAL, "red_rose6", "白色のチューリップ", 1, Material.WHITE_TULIP, 6),
    new MineStackObj(AGRICULTURAL, "red_rose7", "桃色のチューリップ", 1, Material.PINK_TULIP, 7),
    new MineStackObj(AGRICULTURAL, "red_rose8", "フランスギク", 1, Material.OXEYE_DAISY, 8),
    new MineStackObj(AGRICULTURAL, "leaves", "オークの葉", 1, Material.OAK_LEAVES, 0),
    new MineStackObj(AGRICULTURAL, "leaves1", "マツの葉", 1, Material.SPRUCE_LEAVES, 1),
    new MineStackObj(AGRICULTURAL, "leaves2", "シラカバの葉", 1, Material.BIRCH_LEAVES, 2),
    new MineStackObj(AGRICULTURAL, "leaves3", "ジャングルの葉", 1, Material.JUNGLE_LEAVES, 3),
    new MineStackObj(AGRICULTURAL, "leaves_2", "アカシアの葉", 1, Material.ACACIA_LEAVES, 0),
    new MineStackObj(AGRICULTURAL, "leaves_21", "ダークオークの葉", 1, Material.DARK_OAK_LEAVES, 1),
    new MineStackObj(AGRICULTURAL, "double_plant0", "ヒマワリ", 1, Material.SUNFLOWER, 0),
    new MineStackObj(AGRICULTURAL, "double_plant1", "ライラック", 1, Material.LILAC, 1),
    new MineStackObj(AGRICULTURAL, "double_plant2", "高い草", 1, Material.TALL_GRASS, 2),
    new MineStackObj(AGRICULTURAL, "double_plant3", "大きなシダ", 1, Material.LARGE_FERN, 3),
    new MineStackObj(AGRICULTURAL, "double_plant4", "バラの低木", 1, Material.ROSE_BUSH, 4),
    new MineStackObj(AGRICULTURAL, "double_plant5", "ボタン", 1, Material.PEONY, 5),
    new MineStackObj(AGRICULTURAL, "sugar_cane", "サトウキビ", 1, Material.SUGAR_CANE, 0),
    new MineStackObj(AGRICULTURAL, "pumpkin", "カボチャ", 1, Material.PUMPKIN, 0),
    new MineStackObj(AGRICULTURAL, "ink_sack3", "カカオ豆", 1, Material.COCOA_BEANS, 3),
    new MineStackObj(AGRICULTURAL, "huge_mushroom_1", "キノコ", 1, Material.BROWN_MUSHROOM, 0),
    new MineStackObj(AGRICULTURAL, "huge_mushroom_2", "キノコ", 1, Material.RED_MUSHROOM, 0),
    new MineStackObj(AGRICULTURAL, "melon", "スイカ", 1, Material.MELON, 0),
    new MineStackObj(AGRICULTURAL, "melon_block", "スイカ", 1, Material.MELON, 0),
    new MineStackObj(AGRICULTURAL, "brown_mushroom", "キノコ", 1, Material.BROWN_MUSHROOM, 0),
    new MineStackObj(AGRICULTURAL, "red_mushroom", "キノコ", 1, Material.RED_MUSHROOM, 0),
    new MineStackObj(AGRICULTURAL, "sapling", "オークの苗木", 1, Material.OAK_SAPLING, 0),
    new MineStackObj(AGRICULTURAL, "sapling1", "マツの苗木", 1, Material.SPRUCE_SAPLING, 1),
    new MineStackObj(AGRICULTURAL, "sapling2", "シラカバの苗木", 1, Material.BIRCH_SAPLING, 2),
    new MineStackObj(AGRICULTURAL, "sapling3", "ジャングルの苗木", 1, Material.JUNGLE_SAPLING, 3),
    new MineStackObj(AGRICULTURAL, "sapling4", "アカシアの苗木", 1, Material.ACACIA_SAPLING, 4),
    new MineStackObj(AGRICULTURAL, "sapling5", "ダークオークの苗木", 1, Material.DARK_OAK_SAPLING, 5),
    new MineStackObj(AGRICULTURAL, "beetroot", "ビートルート", 1, Material.BEETROOT, 0),
    new MineStackObj(AGRICULTURAL, "beetroot_seeds", "ビートルートの種", 1, Material.BEETROOT_SEEDS, 0),
    new MineStackObj(AGRICULTURAL, "carrot_item", "ニンジン", 1, Material.CARROT, 0),
    new MineStackObj(AGRICULTURAL, "potato_item", "ジャガイモ", 1, Material.POTATO, 0),
    new MineStackObj(AGRICULTURAL, "poisonous_potato", "青くなったジャガイモ", 1, Material.POISONOUS_POTATO, 0),
    new MineStackObj(AGRICULTURAL, "wheat", "小麦", 1, Material.WHEAT, 0),
    new MineStackObj(AGRICULTURAL, "pumpkin_seeds", "カボチャの種", 1, Material.PUMPKIN_SEEDS, 0),
    new MineStackObj(AGRICULTURAL, "melon_seeds", "スイカの種", 1, Material.MELON_SEEDS, 0),
    new MineStackObj(AGRICULTURAL, "nether_stalk", "ネザーウォート", 1, Material.NETHER_WART, 0),
    new MineStackObj(AGRICULTURAL, "chorus_fruit", "コーラスフルーツ", 1, Material.CHORUS_FRUIT, 0),
    new MineStackObj(AGRICULTURAL, "chorus_flower", "コーラスフラワー", 1, Material.CHORUS_FLOWER, 0),
    new MineStackObj(AGRICULTURAL, "popped_chorus_fruit", "焼いたコーラスフルーツ", 1, Material.POPPED_CHORUS_FRUIT, 0),
    new MineStackObj(AGRICULTURAL, "egg", "卵", 1, Material.EGG, 0),
    new MineStackObj(AGRICULTURAL, "pork", "生の豚肉", 1, Material.PORKCHOP, 0),
    new MineStackObj(AGRICULTURAL, "cooked_porkchop", "焼き豚", 1, Material.COOKED_PORKCHOP, 0),
    new MineStackObj(AGRICULTURAL, "raw_chicken", "生の鶏肉", 1, Material.CHICKEN, 0),
    new MineStackObj(AGRICULTURAL, "cooked_chicken", "焼き鳥", 1, Material.COOKED_CHICKEN, 0),
    new MineStackObj(AGRICULTURAL, "mutton", "生の羊肉", 1, Material.MUTTON, 0),
    new MineStackObj(AGRICULTURAL, "cooked_mutton", "焼いた羊肉", 1, Material.COOKED_MUTTON, 0),
    new MineStackObj(AGRICULTURAL, "raw_beef", "生の牛肉", 1, Material.BEEF, 0),
    new MineStackObj(AGRICULTURAL, "cooked_beaf", "ステーキ", 1, Material.COOKED_BEEF, 0),
    new MineStackObj(AGRICULTURAL, "rabbit", "生の兎肉", 1, Material.RABBIT, 0),
    new MineStackObj(AGRICULTURAL, "cooked_rabbit", "焼き兎肉", 1, Material.COOKED_RABBIT, 0),
    new MineStackObj(AGRICULTURAL, "raw_fish0", "生魚", 1, Material.COD, 0),
    new MineStackObj(AGRICULTURAL, "cooked_fish0", "焼き魚", 1, Material.COOKED_COD, 0),
    new MineStackObj(AGRICULTURAL, "raw_fish1", "生鮭", 1, Material.SALMON, 1),
    new MineStackObj(AGRICULTURAL, "cooked_fish1", "焼き鮭", 1, Material.COOKED_SALMON, 1),
    new MineStackObj(AGRICULTURAL, "raw_fish2", "クマノミ", 1, Material.TROPICAL_FISH, 2),
    new MineStackObj(AGRICULTURAL, "raw_fish3", "フグ", 1, Material.PUFFERFISH, 3),
    new MineStackObj(AGRICULTURAL, "bread", "パン", 1, Material.BREAD, 0),
    new MineStackObj(AGRICULTURAL, "sugar", "砂糖", 1, Material.SUGAR, 0),
    new MineStackObj(AGRICULTURAL, "baked_potato", "ベイクドポテト", 1, Material.BAKED_POTATO, 0),
    new MineStackObj(AGRICULTURAL, "cake", "ケーキ", 1, Material.CAKE, 0),
    new MineStackObj(AGRICULTURAL, "mushroom_stew", "キノコシチュー", 1, Material.MUSHROOM_STEW, 0),
    new MineStackObj(AGRICULTURAL, "rabbit_stew", "ウサギシチュー", 1, Material.RABBIT_STEW, 0),
    new MineStackObj(AGRICULTURAL, "beetroot_soup", "ビートルートスープ", 1, Material.BEETROOT_SOUP, 0),
    new MineStackObj(AGRICULTURAL, "bowl", "ボウル", 1, Material.BOWL, 0),
    new MineStackObj(AGRICULTURAL, "milk_bucket", "牛乳", 1, Material.MILK_BUCKET, 0)
  )
  
  // 建築系ブロック
  private val minestacklistbuild: List[Either[MineStackObj, GroupedMineStackObj]] = leftElems(
    new MineStackObj(BUILDING, "log", "オークの原木", 1, Material.OAK_LOG, 0),
    new MineStackObj(BUILDING, "wood", "オークの木材", 1, Material.OAK_WOOD, 0),
    new MineStackObj(BUILDING, "wood_step0", "オークの木材ハーフブロック", 1, Material.OAK_SLAB, 0),
    new MineStackObj(BUILDING, "oak_stairs", "オークの木の階段", 1, Material.OAK_STAIRS, 0),
    new MineStackObj(BUILDING, "fence", "オークのフェンス", 1, Material.OAK_FENCE, 0),
    new MineStackObj(BUILDING, "log1", "マツの原木", 1, Material.SPRUCE_LOG, 1),
    new MineStackObj(BUILDING, "wood_1", "マツの木材", 1, Material.SPRUCE_WOOD, 1),
    new MineStackObj(BUILDING, "wood_step1", "マツの木材ハーフブロック", 1, Material.SPRUCE_SLAB, 1),
    new MineStackObj(BUILDING, "spruce_stairs", "マツの木の階段", 1, Material.SPRUCE_STAIRS, 0),
    new MineStackObj(BUILDING, "spruce_fence", "マツのフェンス", 1, Material.SPRUCE_FENCE, 0),
    new MineStackObj(BUILDING, "log2", "シラカバの原木", 1, Material.BIRCH_LOG, 2),
    new MineStackObj(BUILDING, "wood_2", "シラカバの木材", 1, Material.BIRCH_WOOD, 2),
    new MineStackObj(BUILDING, "wood_step2", "シラカバの木材ハーフブロック", 1, Material.BIRCH_SLAB, 2),
    new MineStackObj(BUILDING, "birch_stairs", "シラカバの木の階段", 1, Material.BIRCH_STAIRS, 0),
    new MineStackObj(BUILDING, "birch_fence", "シラカバのフェンス", 1, Material.BIRCH_FENCE, 0),
    new MineStackObj(BUILDING, "log3", "ジャングルの原木", 1, Material.JUNGLE_LOG, 3),
    new MineStackObj(BUILDING, "wood_3", "ジャングルの木材", 1, Material.JUNGLE_WOOD, 3),
    new MineStackObj(BUILDING, "wood_step3", "ジャングルの木材ハーフブロック", 1, Material.JUNGLE_SLAB, 3),
    new MineStackObj(BUILDING, "jungle_stairs", "ジャングルの木の階段", 1, Material.JUNGLE_STAIRS, 0),
    new MineStackObj(BUILDING, "jungle_fence", "ジャングルのフェンス", 1, Material.JUNGLE_FENCE, 0),
    new MineStackObj(BUILDING, "log_2", "アカシアの原木", 1, Material.ACACIA_LOG, 0),
    new MineStackObj(BUILDING, "wood_4", "アカシアの木材", 1, Material.ACACIA_WOOD, 4),
    new MineStackObj(BUILDING, "wood_step4", "アカシアの木材ハーフブロック", 1, Material.ACACIA_SLAB, 4),
    new MineStackObj(BUILDING, "acacia_stairs", "アカシアの木の階段", 1, Material.ACACIA_STAIRS, 0),
    new MineStackObj(BUILDING, "acacia_fence", "アカシアのフェンス", 1, Material.ACACIA_FENCE, 0),
    new MineStackObj(BUILDING, "log_21", "ダークオークの原木", 1, Material.DARK_OAK_LOG, 1),
    new MineStackObj(BUILDING, "wood_5", "ダークオークの木材", 1, Material.DARK_OAK_WOOD, 5),
    new MineStackObj(BUILDING, "wood_step5", "ダークオークの木材ハーフブロック", 1, Material.DARK_OAK_SLAB, 5),
    new MineStackObj(BUILDING, "dark_oak_stairs", "ダークオークの木の階段", 1, Material.DARK_OAK_STAIRS, 0),
    new MineStackObj(BUILDING, "dark_oak_fence", "ダークオークのフェンス", 1, Material.DARK_OAK_FENCE, 0),
    new MineStackObj(BUILDING, "cobblestone", "丸石", 1, Material.COBBLESTONE, 0),
    new MineStackObj(BUILDING, "step3", "丸石ハーフブロック", 1, Material.COBBLESTONE_SLAB, 3),
    new MineStackObj(BUILDING, "stone_stairs", "丸石の階段", 1, Material.COBBLESTONE_STAIRS, 0),
    new MineStackObj(BUILDING, "cobblestone_wall_0", "丸石の壁", 1, Material.COBBLESTONE_WALL, 0),
    new MineStackObj(BUILDING, "mossy_cobblestone", "苔石", 1, Material.MOSSY_COBBLESTONE, 0),
    new MineStackObj(BUILDING, "cobblestone_wall_1", "苔石の壁", 1, Material.MOSSY_COBBLESTONE_WALL, 1),
    new MineStackObj(BUILDING, "stone", "石", 1, Material.STONE, 0),
    new MineStackObj(BUILDING, "step0", "石ハーフブロック", 1, Material.STONE_SLAB, 0),
    new MineStackObj(BUILDING, "smooth_brick0", "石レンガ", 1, Material.STONE_BRICKS, 0),
    new MineStackObj(BUILDING, "step5", "石レンガハーフブロック", 1, Material.STONE_BRICK_SLAB, 5),
    new MineStackObj(BUILDING, "smooth_stairs", "石レンガの階段", 1, Material.STONE_BRICK_STAIRS, 0),
    new MineStackObj(BUILDING, "smooth_brick3", "模様入り石レンガ", 1, Material.CHISELED_STONE_BRICKS, 3),
    new MineStackObj(BUILDING, "smooth_brick1", "苔石レンガ", 1, Material.MOSSY_STONE_BRICKS, 1),
    new MineStackObj(BUILDING, "smooth_brick2", "ひびの入った石レンガ", 1, Material.CRACKED_STONE_BRICKS, 2),
    new MineStackObj(BUILDING, "sand", "砂", 1, Material.SAND, 0),
    new MineStackObj(BUILDING, "sandstone", "砂岩", 1, Material.SANDSTONE, 0),
    new MineStackObj(BUILDING, "step1", "砂岩ハーフブロック", 1, Material.SANDSTONE_SLAB, 1),
    new MineStackObj(BUILDING, "standstone_stairs", "砂岩の階段", 1, Material.SANDSTONE_STAIRS, 0),
    new MineStackObj(BUILDING, "sandstone1", "模様入りの砂岩", 1, Material.SANDSTONE, 1),
    new MineStackObj(BUILDING, "sandstone2", "なめらかな砂岩", 1, Material.SANDSTONE, 2),
    new MineStackObj(BUILDING, "red_sand", "赤い砂", 1, Material.SAND, 1),
    new MineStackObj(BUILDING, "red_sandstone", "赤い砂岩", 1, Material.RED_SANDSTONE, 0),
    new MineStackObj(BUILDING, "stone_slab20", "赤い砂岩ハーフブロック", 1, Material.RED_SANDSTONE_SLAB, 0),
    new MineStackObj(BUILDING, "red_sandstone_stairs", "赤い砂岩の階段", 1, Material.RED_SANDSTONE_STAIRS, 0),
    new MineStackObj(BUILDING, "red_sandstone1", "模様入りの赤い砂岩", 1, Material.RED_SANDSTONE, 1),
    new MineStackObj(BUILDING, "red_sandstone2", "なめらかな赤い砂岩", 1, Material.RED_SANDSTONE, 2),
    new MineStackObj(BUILDING, "clay_ball", "粘土", 1, Material.CLAY_BALL, 0),
    new MineStackObj(BUILDING, "clay", "粘土(ブロック)", 1, Material.CLAY, 0),
    new MineStackObj(BUILDING, "brick_item", "レンガ", 1, Material.BRICK, 0),
    new MineStackObj(BUILDING, "brick", "レンガ(ブロック)", 1, Material.BRICKS, 0),
    new MineStackObj(BUILDING, "step4", "レンガハーフブロック", 1, Material.BRICK_SLAB, 4),
    new MineStackObj(BUILDING, "brick_stairs", "レンガの階段", 1, Material.BRICK_STAIRS, 0),
    new MineStackObj(BUILDING, "quartz_block", "ネザー水晶ブロック", 1, Material.QUARTZ_BLOCK, 0),
    new MineStackObj(BUILDING, "step7", "ネザー水晶ハーフブロック", 1, Material.QUARTZ_SLAB, 7),
    new MineStackObj(BUILDING, "quartz_stairs", "ネザー水晶の階段", 1, Material.QUARTZ_STAIRS, 0),
    new MineStackObj(BUILDING, "quartz_block1", "模様入りネザー水晶ブロック", 1, Material.QUARTZ_BLOCK, 1),
    new MineStackObj(BUILDING, "quartz_block2", "柱状ネザー水晶ブロック", 1, Material.QUARTZ_BLOCK, 2),
    new MineStackObj(BUILDING, "netherrack", "ネザーラック", 1, Material.NETHERRACK, 0),
    new MineStackObj(BUILDING, "nether_brick_item", "ネザーレンガ", 1, Material.NETHER_BRICK, 0),
    new MineStackObj(BUILDING, "nether_brick", "ネザーレンガ(ブロック)", 1, Material.NETHER_BRICKS, 0),
    new MineStackObj(BUILDING, "step6", "ネザーレンガハーフブロック", 1, Material.NETHER_BRICK_SLAB, 6),
    new MineStackObj(BUILDING, "nether_brick_stairs", "ネザーレンガの階段", 1, Material.NETHER_BRICK_STAIRS, 0),
    new MineStackObj(BUILDING, "nether_brick_fence", "ネザーレンガのフェンス", 1, Material.NETHER_BRICK_FENCE, 0),
    new MineStackObj(BUILDING, "red_nether_brick", "赤いネザーレンガ", 1, Material.RED_NETHER_BRICKS, 0),
    new MineStackObj(BUILDING, "nether_wart_block", "ネザ－ウォートブロック", 1, Material.NETHER_WART_BLOCK, 0),
    new MineStackObj(BUILDING, "ender_stone", "エンドストーン", 1, Material.END_STONE, 0),
    new MineStackObj(BUILDING, "end_bricks", "エンドストーンレンガ", 1, Material.END_STONE_BRICKS, 0),
    new MineStackObj(BUILDING, "purpur_block", "プルパーブロック", 1, Material.PURPUR_BLOCK, 0),
    new MineStackObj(BUILDING, "purpur_pillar", "柱状プルパーブロック", 1, Material.PURPUR_PILLAR, 0),
    new MineStackObj(BUILDING, "purpur_slab", "プルパーハーフブロック", 1, Material.PURPUR_SLAB, 0),
    new MineStackObj(BUILDING, "purpur_stairs", "プルパーの階段", 1, Material.PURPUR_STAIRS, 0),
    new MineStackObj(BUILDING, "prismarine0", "プリズマリン", 1, Material.PRISMARINE, 0),
    new MineStackObj(BUILDING, "prismarine1", "プリズマリンレンガ", 1, Material.PRISMARINE, 1),
    new MineStackObj(BUILDING, "prismarine2", "ダークプリズマリン", 1, Material.PRISMARINE, 2),
    new MineStackObj(BUILDING, "sea_lantern", "シーランタン", 1, Material.SEA_LANTERN, 0),
    new MineStackObj(BUILDING, "granite", "花崗岩", 1, Material.STONE, 1),
    new MineStackObj(BUILDING, "polished_granite", "磨かれた花崗岩", 1, Material.STONE, 2),
    new MineStackObj(BUILDING, "diorite", "閃緑岩", 1, Material.STONE, 3),
    new MineStackObj(BUILDING, "polished_diorite", "磨かれた閃緑岩", 1, Material.STONE, 4),
    new MineStackObj(BUILDING, "andesite", "安山岩", 1, Material.STONE, 5),
    new MineStackObj(BUILDING, "polished_andesite", "磨かれた安山岩", 1, Material.STONE, 6),
    new MineStackObj(BUILDING, "dirt", "土", 1, Material.DIRT, 0),
    new MineStackObj(BUILDING, "grass", "草ブロック", 1, Material.GRASS, 0),
    new MineStackObj(BUILDING, "gravel", "砂利", 1, Material.GRAVEL, 0),
    new MineStackObj(BUILDING, "flint", "火打石", 1, Material.FLINT, 0),
    new MineStackObj(BUILDING, "flint_and_steel", "火打石と打ち金", 1, Material.FLINT_AND_STEEL, 0),
    new MineStackObj(BUILDING, "dirt1", "粗い土", 1, Material.DIRT, 1),
    new MineStackObj(BUILDING, "dirt2", "ポドゾル", 1, Material.DIRT, 2),
    new MineStackObj(BUILDING, "snow_block", "雪", 1, Material.SNOW_BLOCK, 0),
    new MineStackObj(BUILDING, "snow_layer", "雪タイル", 1, Material.SNOW, 0),
    new MineStackObj(BUILDING, "snow_ball", "雪玉", 1, Material.SNOWBALL, 0),
    new MineStackObj(BUILDING, "ice", "氷", 1, Material.ICE, 0),
    new MineStackObj(BUILDING, "packed_ice", "氷塊", 1, Material.PACKED_ICE, 0),
    new MineStackObj(BUILDING, "mycel", "菌糸", 1, Material.MYCELIUM, 0),
    new MineStackObj(BUILDING, "bone_block", "骨ブロック", 1, Material.BONE_BLOCK, 0),
    new MineStackObj(BUILDING, "sponge", "スポンジ", 1, Material.SPONGE, 0),
    new MineStackObj(BUILDING, "wet_sponge", "濡れたスポンジ", 1, Material.SPONGE, 1),
    new MineStackObj(BUILDING, "soul_sand", "ソウルサンド", 1, Material.SOUL_SAND, 0),
    new MineStackObj(BUILDING, "magma", "マグマブロック", 1, Material.MAGMA_BLOCK, 0),
    new MineStackObj(BUILDING, "obsidian", "黒曜石", 1, Material.OBSIDIAN, 0),
    new MineStackObj(BUILDING, "glowstone_dust", "グロウストーンダスト", 1, Material.GLOWSTONE_DUST, 0),
    new MineStackObj(BUILDING, "glowstone", "グロウストーン", 1, Material.GLOWSTONE, 0),
    new MineStackObj(BUILDING, "torch", "松明", 1, Material.TORCH, 0),
    new MineStackObj(BUILDING, "jack_o_lantern", "ジャック・オ・ランタン", 1, Material.JACK_O_LANTERN, 0),
    new MineStackObj(BUILDING, "end_rod", "エンドロッド", 1, Material.END_ROD, 0),
    new MineStackObj(BUILDING, "bucket", "バケツ", 1, Material.BUCKET, 0),
    new MineStackObj(BUILDING, "water_bucket", "水入りバケツ", 1, Material.WATER_BUCKET, 0),
    new MineStackObj(BUILDING, "lava_bucket", "溶岩入りバケツ", 1, Material.LAVA_BUCKET, 0),
    new MineStackObj(BUILDING, "web", "クモの巣", 1, Material.COBWEB, 0),
    new MineStackObj(BUILDING, "rails", "レール", 1, Material.RAIL, 0),
    new MineStackObj(BUILDING, "furnace", "かまど", 1, Material.FURNACE, 0),
    new MineStackObj(BUILDING, "chest", "チェスト", 1, Material.CHEST, 0),
    new MineStackObj(BUILDING, "book", "本", 1, Material.BOOK, 0),
    new MineStackObj(BUILDING, "bookshelf", "本棚", 1, Material.BOOKSHELF, 0),
    new MineStackObj(BUILDING, "iron_bars", "鉄格子", 1, Material.IRON_BARS, 0),
    new MineStackObj(BUILDING, "anvil", "金床", 1, Material.ANVIL, 0),
    new MineStackObj(BUILDING, "cauldron", "大釜", 1, Material.CAULDRON, 0),
    new MineStackObj(BUILDING, "brewing_stand", "醸造台", 1, Material.BREWING_STAND, 0),
    new MineStackObj(BUILDING, "flower_pot", "植木鉢", 1, Material.FLOWER_POT, 0),
    new MineStackObj(BUILDING, "hay_block", "干し草の俵", 1, Material.HAY_BLOCK, 0),
    new MineStackObj(BUILDING, "ladder", "はしご", 1, Material.LADDER, 0),
    new MineStackObj(BUILDING, "sign", "看板", 1, Material.SIGN, 0),
    new MineStackObj(BUILDING, "item_frame", "額縁", 1, Material.ITEM_FRAME, 0),
    new MineStackObj(BUILDING, "painting", "絵画", 1, Material.PAINTING, 0),
    new MineStackObj(BUILDING, "beacon", "ビーコン", 1, Material.BEACON, 0),
    new MineStackObj(BUILDING, "armor_stand", "アーマースタンド", 1, Material.ARMOR_STAND, 0),
    new MineStackObj(BUILDING, "end_crystal", "エンドクリスタル", 1, Material.END_CRYSTAL, 0),
    new MineStackObj(BUILDING, "enchanting_table", "エンチャントテーブル", 1, Material.ENCHANTING_TABLE, 0),
    new MineStackObj(BUILDING, "jukebox", "ジュークボックス", 1, Material.JUKEBOX, 0),
    new MineStackObj(BUILDING, "hard_clay", "テラコッタ", 1, Material.TERRACOTTA, 0),
    new MineStackObj(BUILDING, "workbench", "作業台", 1, Material.CRAFTING_TABLE, 0)
  ) ++ rightElems(
    GroupedMineStackObj(
      new MineStackObj(BUILDING, "bed", "白色のベッド", 1, Material.WHITE_BED, 0),
      List(
        new MineStackObj(BUILDING, "bed_1", "橙色のベッド", 1, Material.ORANGE_BED, 1),
        new MineStackObj(BUILDING, "bed_2", "赤紫色のベッド", 1, Material.MAGENTA_BED, 2),
        new MineStackObj(BUILDING, "bed_3", "空色のベッド", 1, Material.LIGHT_BLUE_BED, 3),
        new MineStackObj(BUILDING, "bed_4", "黄色のベッド", 1, Material.YELLOW_BED, 4),
        new MineStackObj(BUILDING, "bed_5", "黄緑色のベッド", 1, Material.LIME_BED, 5),
        new MineStackObj(BUILDING, "bed_6", "桃色のベッド", 1, Material.PINK_BED, 6),
        new MineStackObj(BUILDING, "bed_7", "灰色のベッド", 1, Material.GRAY_BED, 7),
        new MineStackObj(BUILDING, "bed_8", "薄灰色のベッド", 1, Material.LIGHT_GRAY_BED, 8),
        new MineStackObj(BUILDING, "bed_9", "青緑色のベッド", 1, Material.CYAN_BED, 9),
        new MineStackObj(BUILDING, "bed_10", "紫色のベッド", 1, Material.PURPLE_BED, 10),
        new MineStackObj(BUILDING, "bed_11", "青色のベッド", 1, Material.BLUE_BED, 11),
        new MineStackObj(BUILDING, "bed_12", "茶色のベッド", 1, Material.BROWN_BED, 12),
        new MineStackObj(BUILDING, "bed_13", "緑色のベッド", 1, Material.GREEN_BED, 13),
        new MineStackObj(BUILDING, "bed_14", "赤色のベッド", 1, Material.RED_BED, 14),
        new MineStackObj(BUILDING, "bed_15", "黒色のベッド", 1, Material.BLACK_BED, 15)
      )
    ),
    GroupedMineStackObj(
      new MineStackObj(BUILDING, "stained_clay", "白色のテラコッタ", 1, Material.WHITE_TERRACOTTA, 0),
      List(
        new MineStackObj(BUILDING, "stained_clay1", "橙色のテラコッタ", 1, Material.ORANGE_TERRACOTTA, 1),
        new MineStackObj(BUILDING, "stained_clay2", "赤紫色のテラコッタ", 1, Material.MAGENTA_TERRACOTTA, 2),
        new MineStackObj(BUILDING, "stained_clay3", "空色のテラコッタ", 1, Material.LIGHT_BLUE_TERRACOTTA, 3),
        new MineStackObj(BUILDING, "stained_clay4", "黄色のテラコッタ", 1, Material.YELLOW_TERRACOTTA, 4),
        new MineStackObj(BUILDING, "stained_clay5", "黄緑色のテラコッタ", 1, Material.LIME_TERRACOTTA, 5),
        new MineStackObj(BUILDING, "stained_clay6", "桃色のテラコッタ", 1, Material.PINK_TERRACOTTA, 6),
        new MineStackObj(BUILDING, "stained_clay7", "灰色のテラコッタ", 1, Material.GRAY_TERRACOTTA, 7),
        new MineStackObj(BUILDING, "stained_clay8", "薄灰色のテラコッタ", 1, Material.LIGHT_GRAY_TERRACOTTA, 8),
        new MineStackObj(BUILDING, "stained_clay9", "青緑色のテラコッタ", 1, Material.CYAN_TERRACOTTA, 9),
        new MineStackObj(BUILDING, "stained_clay10", "紫色のテラコッタ", 1, Material.PURPLE_TERRACOTTA, 10),
        new MineStackObj(BUILDING, "stained_clay11", "青色のテラコッタ", 1, Material.BLUE_TERRACOTTA, 11),
        new MineStackObj(BUILDING, "stained_clay12", "茶色のテラコッタ", 1, Material.BROWN_TERRACOTTA, 12),
        new MineStackObj(BUILDING, "stained_clay13", "緑色のテラコッタ", 1, Material.GREEN_TERRACOTTA, 13),
        new MineStackObj(BUILDING, "stained_clay14", "赤色のテラコッタ", 1, Material.RED_TERRACOTTA, 14),
        new MineStackObj(BUILDING, "stained_clay15", "黒色のテラコッタ", 1, Material.BLACK_TERRACOTTA, 15)
      )
    ),
    GroupedMineStackObj(
      new MineStackObj(BUILDING, "concrete", "白色のコンクリート", 1, Material.WHITE_CONCRETE, 0),
      List(
        new MineStackObj(BUILDING, "concrete1", "橙色のコンクリート", 1, Material.ORANGE_CONCRETE, 1),
        new MineStackObj(BUILDING, "concrete2", "赤紫色のコンクリート", 1, Material.MAGENTA_CONCRETE, 2),
        new MineStackObj(BUILDING, "concrete3", "空色のコンクリート", 1, Material.LIGHT_BLUE_CONCRETE, 3),
        new MineStackObj(BUILDING, "concrete4", "黄色のコンクリート", 1, Material.YELLOW_CONCRETE, 4),
        new MineStackObj(BUILDING, "concrete5", "黄緑色のコンクリート", 1, Material.LIME_CONCRETE, 5),
        new MineStackObj(BUILDING, "concrete6", "桃色のコンクリート", 1, Material.PINK_CONCRETE, 6),
        new MineStackObj(BUILDING, "concrete7", "灰色のコンクリート", 1, Material.GRAY_CONCRETE, 7),
        new MineStackObj(BUILDING, "concrete8", "薄灰色のコンクリート", 1, Material.LIGHT_GRAY_CONCRETE, 8),
        new MineStackObj(BUILDING, "concrete9", "青緑色のコンクリート", 1, Material.CYAN_CONCRETE, 9),
        new MineStackObj(BUILDING, "concrete10", "紫色のコンクリート", 1, Material.PURPLE_CONCRETE, 10),
        new MineStackObj(BUILDING, "concrete11", "青色のコンクリート", 1, Material.BLUE_CONCRETE, 11),
        new MineStackObj(BUILDING, "concrete12", "茶色のコンクリート", 1, Material.BROWN_CONCRETE, 12),
        new MineStackObj(BUILDING, "concrete13", "緑色のコンクリート", 1, Material.GREEN_CONCRETE, 13),
        new MineStackObj(BUILDING, "concrete14", "赤色のコンクリート", 1, Material.RED_CONCRETE, 14),
        new MineStackObj(BUILDING, "concrete15", "黒色のコンクリート", 1, Material.BLACK_CONCRETE, 15)
      )
    ),
    GroupedMineStackObj(
      new MineStackObj(BUILDING, "concrete_powder", "白色のコンクリートパウダー", 1, Material.WHITE_CONCRETE_POWDER, 0),
      List(
        new MineStackObj(BUILDING, "concrete_powder1", "橙色のコンクリートパウダー", 1, Material.ORANGE_CONCRETE_POWDER, 1),
        new MineStackObj(BUILDING, "concrete_powder2", "赤紫色のコンクリートパウダー", 1, Material.MAGENTA_CONCRETE_POWDER, 2),
        new MineStackObj(BUILDING, "concrete_powder3", "空色のコンクリートパウダー", 1, Material.LIGHT_BLUE_CONCRETE_POWDER, 3),
        new MineStackObj(BUILDING, "concrete_powder4", "黄色のコンクリートパウダー", 1, Material.YELLOW_CONCRETE_POWDER, 4),
        new MineStackObj(BUILDING, "concrete_powder5", "黄緑色のコンクリートパウダー", 1, Material.LIME_CONCRETE_POWDER, 5),
        new MineStackObj(BUILDING,"concrete_powder6","桃色のコンクリートパウダー",1,Material.PINK_CONCRETE_POWDER,6),
        new MineStackObj(BUILDING,"concrete_powder7","灰色のコンクリートパウダー",1,Material.GRAY_CONCRETE_POWDER,7),
        new MineStackObj(BUILDING,"concrete_powder8","薄灰色のコンクリートパウダー",1,Material.LIGHT_GRAY_CONCRETE_POWDER,8),
        new MineStackObj(BUILDING,"concrete_powder9","青緑色のコンクリートパウダー",1,Material.CYAN_CONCRETE_POWDER,9),
        new MineStackObj(BUILDING,"concrete_powder10","紫色のコンクリートパウダー",1,Material.PURPLE_CONCRETE_POWDER,10),
        new MineStackObj(BUILDING,"concrete_powder11","青色のコンクリートパウダー",1,Material.BLUE_CONCRETE_POWDER,11),
        new MineStackObj(BUILDING,"concrete_powder12","茶色のコンクリートパウダー",1,Material.BROWN_CONCRETE_POWDER,12),
        new MineStackObj(BUILDING,"concrete_powder13","緑色のコンクリートパウダー",1,Material.GREEN_CONCRETE_POWDER,13),
        new MineStackObj(BUILDING,"concrete_powder14","赤色のコンクリートパウダー",1,Material.RED_CONCRETE_POWDER,14),
        new MineStackObj(BUILDING,"concrete_powder15","黒色のコンクリートパウダー",1,Material.BLACK_CONCRETE_POWDER,15)
      )
    ),
    GroupedMineStackObj(
      new MineStackObj(BUILDING,"white_glazed_terracotta","白色の彩釉テラコッタ",1,Material.WHITE_GLAZED_TERRACOTTA,0),
      List(
        new MineStackObj(BUILDING,"orange_glazed_terracotta","橙色の彩釉テラコッタ",1,Material.ORANGE_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"magenta_glazed_terracotta","赤紫色の彩釉テラコッタ",1,Material.MAGENTA_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"light_blue_glazed_terracotta","空色の彩釉テラコッタ",1,Material.LIGHT_BLUE_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"yellow_glazed_terracotta","黄色の彩釉テラコッタ",1,Material.YELLOW_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"lime_glazed_terracotta","黄緑色の彩釉テラコッタ",1,Material.LIME_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"pink_glazed_terracotta","桃色の彩釉テラコッタ",1,Material.PINK_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"gray_glazed_terracotta","灰色の彩釉テラコッタ",1,Material.GRAY_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"silver_glazed_terracotta","薄灰色の彩釉テラコッタ",1,Material.LIGHT_GRAY_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"cyan_glazed_terracotta","青緑色の彩釉テラコッタ",1,Material.CYAN_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"purple_glazed_terracotta","紫色の彩釉テラコッタ",1,Material.PURPLE_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"blue_glazed_terracotta","青色の彩釉テラコッタ",1,Material.BLUE_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"brown_glazed_terracotta","茶色の彩釉テラコッタ",1,Material.BROWN_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"green_glazed_terracotta","緑色の彩釉テラコッタ",1,Material.GREEN_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"red_glazed_terracotta","赤色の彩釉テラコッタ",1,Material.RED_GLAZED_TERRACOTTA,0),
        new MineStackObj(BUILDING,"black_glazed_terracotta","黒色の彩釉テラコッタ",1,Material.BLACK_GLAZED_TERRACOTTA,0)
      )
    ),
    GroupedMineStackObj(
      new MineStackObj(BUILDING, "wool_0", "羊毛", 1, Material.WHITE_WOOL, 0),
      List(
        new MineStackObj(BUILDING, "wool_1", "橙色の羊毛", 1, Material.ORANGE_WOOL, 1),
        new MineStackObj(BUILDING, "wool_2", "赤紫色の羊毛", 1,Material.MAGENTA_WOOL, 2),
        new MineStackObj(BUILDING, "wool_3", "空色の羊毛", 1, Material.LIGHT_BLUE_WOOL, 3),
        new MineStackObj(BUILDING, "wool_4", "黄色の羊毛", 1, Material.YELLOW_WOOL, 4),
        new MineStackObj(BUILDING, "wool_5", "黄緑色の羊毛", 1,Material.LIME_WOOL, 5),
        new MineStackObj(BUILDING, "wool_6", "桃色の羊毛", 1, Material.PINK_WOOL, 6),
        new MineStackObj(BUILDING, "wool_7", "灰色の羊毛", 1, Material.GRAY_WOOL, 7),
        new MineStackObj(BUILDING, "wool_8", "薄灰色の羊毛", 1,Material.LIGHT_GRAY_WOOL, 8),
        new MineStackObj(BUILDING, "wool_9", "青緑色の羊毛", 1,Material.CYAN_WOOL, 9),
        new MineStackObj(BUILDING, "wool_10", "紫色の羊毛", 1,Material.PURPLE_WOOL, 10),
        new MineStackObj(BUILDING, "wool_11", "青色の羊毛", 1,Material.BLUE_WOOL, 11),
        new MineStackObj(BUILDING, "wool_12", "茶色の羊毛", 1,Material.BROWN_WOOL, 12),
        new MineStackObj(BUILDING, "wool_13", "緑色の羊毛", 1,Material.GREEN_WOOL, 13),
        new MineStackObj(BUILDING, "wool_14", "赤色の羊毛", 1,Material.RED_WOOL, 14),
        new MineStackObj(BUILDING, "wool_15", "黒色の羊毛", 1,Material.BLACK_WOOL, 15)
      )
    ),
    GroupedMineStackObj(
      new MineStackObj(BUILDING, "carpet_0", "カーペット", 1, Material.WHITE_CARPET, 0),
      List(
        new MineStackObj(BUILDING, "carpet_1", "橙色のカーペット", 1, Material.ORANGE_CARPET, 1),
        new MineStackObj(BUILDING, "carpet_2", "赤紫色のカーペット", 1, Material.MAGENTA_CARPET, 2),
        new MineStackObj(BUILDING, "carpet_3", "空色のカーペット", 1, Material.LIGHT_BLUE_CARPET, 3),
        new MineStackObj(BUILDING, "carpet_4", "黄色のカーペット", 1, Material.YELLOW_CARPET, 4),
        new MineStackObj(BUILDING, "carpet_5", "黄緑色のカーペット", 1, Material.LIME_CARPET, 5),
        new MineStackObj(BUILDING, "carpet_6", "桃色のカーペット", 1, Material.PINK_CARPET, 6),
        new MineStackObj(BUILDING, "carpet_7", "灰色のカーペット", 1, Material.GRAY_CARPET, 7),
        new MineStackObj(BUILDING, "carpet_8", "薄灰色のカーペット", 1, Material.LIGHT_GRAY_CARPET, 8),
        new MineStackObj(BUILDING, "carpet_9", "青緑色のカーペット", 1, Material.CYAN_CARPET, 9),
        new MineStackObj(BUILDING, "carpet_10", "紫色のカーペット", 1, Material.PURPLE_CARPET, 10),
        new MineStackObj(BUILDING, "carpet_11", "青色のカーペット", 1, Material.BLUE_CARPET, 11),
        new MineStackObj(BUILDING, "carpet_12", "茶色のカーペット", 1, Material.BROWN_CARPET, 12),
        new MineStackObj(BUILDING, "carpet_13", "緑色のカーペット", 1, Material.GREEN_CARPET, 13),
        new MineStackObj(BUILDING, "carpet_14", "赤色のカーペット", 1, Material.RED_CARPET, 14),
        new MineStackObj(BUILDING, "carpet_15", "黒色のカーペット", 1, Material.BLACK_CARPET, 15)
      )
    ),
    GroupedMineStackObj(
      new MineStackObj(BUILDING, "glass", "ガラス", 1, Material.GLASS, 0),
      List(
        new MineStackObj(BUILDING, "stained_glass_0", "白色の色付きガラス", 1, Material.WHITE_STAINED_GLASS, 0),
        new MineStackObj(BUILDING, "stained_glass_1", "橙色の色付きガラス", 1, Material.ORANGE_STAINED_GLASS, 1),
        new MineStackObj(BUILDING, "stained_glass_2", "赤紫色の色付きガラス", 1, Material.MAGENTA_STAINED_GLASS, 2),
        new MineStackObj(BUILDING, "stained_glass_3", "空色の色付きガラス", 1, Material.LIGHT_BLUE_STAINED_GLASS, 3),
        new MineStackObj(BUILDING, "stained_glass_4", "黄色の色付きガラス", 1, Material.YELLOW_STAINED_GLASS, 4),
        new MineStackObj(BUILDING, "stained_glass_5", "黄緑色の色付きガラス", 1, Material.LIME_STAINED_GLASS, 5),
        new MineStackObj(BUILDING, "stained_glass_6", "桃色の色付きガラス", 1, Material.PINK_STAINED_GLASS, 6),
        new MineStackObj(BUILDING, "stained_glass_7", "灰色の色付きガラス", 1, Material.GRAY_STAINED_GLASS, 7),
        new MineStackObj(BUILDING, "stained_glass_8", "薄灰色の色付きガラス", 1, Material.LIGHT_GRAY_STAINED_GLASS, 8),
        new MineStackObj(BUILDING, "stained_glass_9", "青緑色の色付きガラス", 1, Material.CYAN_STAINED_GLASS, 9),
        new MineStackObj(BUILDING, "stained_glass_10", "紫色の色付きガラス", 1, Material.PURPLE_STAINED_GLASS, 10),
        new MineStackObj(BUILDING, "stained_glass_11", "青色の色付きガラス", 1, Material.BLUE_STAINED_GLASS, 11),
        new MineStackObj(BUILDING, "stained_glass_12", "茶色の色付きガラス", 1, Material.BROWN_STAINED_GLASS, 12),
        new MineStackObj(BUILDING, "stained_glass_13", "緑色の色付きガラス", 1, Material.GREEN_STAINED_GLASS, 13),
        new MineStackObj(BUILDING, "stained_glass_14", "赤色の色付きガラス", 1, Material.RED_STAINED_GLASS, 14),
        new MineStackObj(BUILDING, "stained_glass_15", "黒色の色付きガラス", 1, Material.BLACK_STAINED_GLASS, 15)
      )
    ),
    GroupedMineStackObj(
      new MineStackObj(BUILDING, "glass_panel", "板ガラス", 1, Material.GLASS_PANE, 0),
        List(
          new MineStackObj(BUILDING,"glass_panel_0","白色の色付きガラス板",1,Material.WHITE_STAINED_GLASS_PANE,0),
          new MineStackObj(BUILDING,"glass_panel_1","橙色の色付きガラス板",1,Material.ORANGE_STAINED_GLASS_PANE,1),
          new MineStackObj(BUILDING,"glass_panel_2","赤紫色の色付きガラス板",1,Material.MAGENTA_STAINED_GLASS_PANE,2),
          new MineStackObj(BUILDING,"glass_panel_3","空色の色付きガラス板",1,Material.LIGHT_BLUE_STAINED_GLASS_PANE,3),
          new MineStackObj(BUILDING,"glass_panel_4","黄色の色付きガラス板",1,Material.YELLOW_STAINED_GLASS_PANE,4),
          new MineStackObj(BUILDING,"glass_panel_5","黄緑色の色付きガラス板",1,Material.LIME_STAINED_GLASS_PANE,5),
          new MineStackObj(BUILDING,"glass_panel_6","桃色の色付きガラス板",1,Material.PINK_STAINED_GLASS_PANE,6),
          new MineStackObj(BUILDING,"glass_panel_7","灰色の色付きガラス板",1,Material.GRAY_STAINED_GLASS_PANE,7),
          new MineStackObj(BUILDING,"glass_panel_8","薄灰色の色付きガラス板",1,Material.LIGHT_GRAY_STAINED_GLASS_PANE,8),
          new MineStackObj(BUILDING,"glass_panel_9","青緑色の色付きガラス板",1,Material.CYAN_STAINED_GLASS_PANE,9),
          new MineStackObj(BUILDING,"glass_panel_10","紫色の色付きガラス板",1,Material.PURPLE_STAINED_GLASS_PANE,10),
          new MineStackObj(BUILDING,"glass_panel_11","青色の色付きガラス板",1,Material.BLUE_STAINED_GLASS_PANE,11),
          new MineStackObj(BUILDING,"glass_panel_12","茶色の色付きガラス板",1,Material.BROWN_STAINED_GLASS_PANE,12),
          new MineStackObj(BUILDING,"glass_panel_13","緑色の色付きガラス板",1,Material.GREEN_STAINED_GLASS_PANE,13),
          new MineStackObj(BUILDING,"glass_panel_14","赤色の色付きガラス板",1,Material.RED_STAINED_GLASS_PANE,14),
          new MineStackObj(BUILDING,"glass_panel_15","黒色の色付きガラス板",1,Material.BLACK_STAINED_GLASS_PANE,15)
        )
    ),
    GroupedMineStackObj(
      new MineStackObj(BUILDING, "dye_1", "赤色の染料", 1, Material.ROSE_RED, 1),
        List(
          new MineStackObj(BUILDING, "dye_2", "緑色の染料", 1, Material.CACTUS_GREEN, 2),
          new MineStackObj(BUILDING, "dye_5", "紫色の染料", 1, Material.PURPLE_DYE, 5),
          new MineStackObj(BUILDING, "dye_6", "青緑色の染料", 1, Material.CYAN_DYE, 6),
          new MineStackObj(BUILDING, "dye_7", "薄灰色の染料", 1, Material.LIGHT_GRAY_DYE, 7),
          new MineStackObj(BUILDING, "dye_8", "灰色の染料", 1, Material.GRAY_DYE, 8),
          new MineStackObj(BUILDING, "dye_9", "桃色の染料", 1, Material.PINK_DYE, 9),
          new MineStackObj(BUILDING, "dye_10", "黄緑色の染料", 1, Material.LIME_DYE, 10),
          new MineStackObj(BUILDING, "dye_11", "黄色の染料", 1, Material.DANDELION_YELLOW, 11),
          new MineStackObj(BUILDING, "dye_12", "空色の染料", 1, Material.LIGHT_BLUE_DYE, 12),
          new MineStackObj(BUILDING, "dye_13", "赤紫色の染料", 1, Material.MAGENTA_DYE, 13),
          new MineStackObj(BUILDING, "dye_14", "橙色の染料", 1, Material.ORANGE_DYE, 14),
          new MineStackObj(BUILDING, "dye_15", "骨粉", 1, Material.BONE_MEAL, 15),
          new MineStackObj(BUILDING, "ink_sack0", "イカスミ", 1, Material.INK_SAC, 0)
        )
    )
  )

  // レッドストーン系ブロック
  private val minestacklistrs: List[Either[MineStackObj, GroupedMineStackObj]] = leftElems(
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"redstone","レッドストーン",1,Material.REDSTONE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"stone_button","石のボタン",1,Material.STONE_BUTTON,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"wood_button","木のボタン",1,Material.OAK_BUTTON,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"stone_plate","石の感圧版",1,Material.STONE_PRESSURE_PLATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"wood_plate","木の感圧版",1,Material.OAK_PRESSURE_PLATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"fence_gate","オークのフェンスゲート",1,Material.OAK_FENCE_GATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"spruce_fence_gate","マツのフェンスゲート",1,Material.SPRUCE_FENCE_GATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"birch_fence_gate","シラカバのフェンスゲート",1,Material.BIRCH_FENCE_GATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"jungle_fence_gate","ジャングルのフェンスゲート",1,Material.JUNGLE_FENCE_GATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"dark_oak_fence_gate","ダークオークのフェンスゲート",1,Material.DARK_OAK_FENCE_GATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"acacia_fence_gate","アカシアのフェンスゲート",1,Material.ACACIA_FENCE_GATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"redstone_block","レッドストーンブロック",1,Material.REDSTONE_BLOCK,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION, "lever", "レバー", 1, Material.LEVER, 0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"redstone_torch_on","レッドストーントーチ",1,Material.REDSTONE_TORCH,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"trap_door","木のトラップドア",1,Material.OAK_TRAPDOOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"iron_trapdoor","鉄のトラップドア",1,Material.IRON_TRAPDOOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"gold_plate","重量感圧版 (軽) ",1,Material.LIGHT_WEIGHTED_PRESSURE_PLATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"iron_plate","重量感圧版 (重) ",1,Material.HEAVY_WEIGHTED_PRESSURE_PLATE,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"wood_door","オークのドア",1,Material.OAK_DOOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"spruce_door_item","マツのドア",1,Material.SPRUCE_DOOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"birch_door_item","シラカバのドア",1,Material.BIRCH_DOOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"jungle_door_item","ジャングルのドア",1,Material.JUNGLE_DOOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"acacia_door_item","アカシアのドア",1,Material.ACACIA_DOOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"dark_oak_door_item","ダークオークのドア",1,Material.DARK_OAK_DOOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"note_block","音符ブロック",1,Material.NOTE_BLOCK,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"redstone_lamp_off","レッドストーンランプ",1,Material.REDSTONE_LAMP,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"tripwire_hook","トリップワイヤーフック",1,Material.TRIPWIRE_HOOK,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION, "dropper", "ドロッパー", 1, Material.DROPPER, 0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"piston_sticky_base","粘着ピストン",1,Material.STICKY_PISTON,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"piston_base","ピストン",1,Material.PISTON,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION, "tnt", "TNT", 1, Material.TNT, 0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"trapped_chest","トラップチェスト",1,Material.TRAPPED_CHEST,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"daylight_detector","日照センサー",1,Material.DAYLIGHT_DETECTOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"iron_door","鉄のドア",1,Material.IRON_DOOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"diode","レッドストーンリピーター",1,Material.REPEATER,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"dispenser","ディスペンサー",1,Material.DISPENSER,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION, "hopper", "ホッパー", 1, Material.HOPPER, 0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"redstone_comparator","レッドストーンコンパレーター",1,Material.COMPARATOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"powered_rail","パワードレール",1,Material.POWERED_RAIL,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"detector_rail","ディテクターレール",1,Material.DETECTOR_RAIL,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"activator_rail","アクティベーターレール",1,Material.ACTIVATOR_RAIL,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION, "boat", "オークのボート", 1, Material.OAK_BOAT, 0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"spruce_boat","マツのボート",1,Material.SPRUCE_BOAT,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"birch_boat","シラカバのボート",1,Material.BIRCH_BOAT,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"jungle_boat","ジャングルのボート",1,Material.JUNGLE_BOAT,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"acacia_boat","アカシアのボート",1,Material.ACACIA_BOAT,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"dark_oak_boat","ダークオークのボート",1,Material.DARK_OAK_BOAT,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION, "saddle", "サドル", 1, Material.SADDLE, 0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION, "minecart", "トロッコ", 1, Material.MINECART, 0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"chest_minecart","チェスト付きトロッコ",1,Material.CHEST_MINECART,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"furnace_minecart","かまど付きトロッコ",1,Material.FURNACE_MINECART,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"hopper_minecart","ホッパー付きトロッコ",1,Material.HOPPER_MINECART,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"iron_horse_armor","鉄の馬鎧",1,Material.IRON_HORSE_ARMOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"golden_horse_armor","金の馬鎧",1,Material.GOLDEN_HORSE_ARMOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"diamond_horse_armor","ダイヤの馬鎧",1,Material.DIAMOND_HORSE_ARMOR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_13","レコード",1,Material.MUSIC_DISC_13,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_cat","レコード",1,Material.MUSIC_DISC_CAT,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_blocks","レコード",1,Material.MUSIC_DISC_BLOCKS,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_chirp","レコード",1,Material.MUSIC_DISC_CHIRP,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_far","レコード",1,Material.MUSIC_DISC_FAR,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_mall","レコード",1,Material.MUSIC_DISC_MALL,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_mellohi","レコード",1,Material.MUSIC_DISC_MELLOHI,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_stal","レコード",1,Material.MUSIC_DISC_STAL,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_strad","レコード",1,Material.MUSIC_DISC_STRAD,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_ward","レコード",1,Material.MUSIC_DISC_WARD,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_11","レコード",1,Material.MUSIC_DISC_11,0),
    new MineStackObj(REDSTONE_AND_TRANSPORTATION,"record_wait","レコード",1,Material.MUSIC_DISC_WAIT,0)
  )

  /**
   * デフォルトでガチャの内容に含まれている景品。
   */
  private val minestackBuiltinGachaPrizes: List[Either[MineStackObj, GroupedMineStackObj]] = leftElems(
    new MineStackObj("gachaimo",None,1,StaticGachaPrizeFactory.getGachaRingo,true,-1,GACHA_PRIZES),
    new MineStackObj("exp_bottle",Some("エンチャントの瓶"),1,Material.EXPERIENCE_BOTTLE,0,false,-1,GACHA_PRIZES)
  )

  // @formatter:on

  private var gachaPrizesObjects: List[MineStackObj] = Nil

  def setGachaPrizesList(mineStackObj: List[MineStackObj]): Unit = {
    gachaPrizesObjects = mineStackObj
  }

  private val allMineStackObjects = List(
    minestacklistbuild,
    minestacklistdrop,
    minestacklistfarm,
    minestacklistmine,
    minestacklistrs,
    minestackBuiltinGachaPrizes
  )

  def getAllMineStackObjects: List[MineStackObj] = {
    allMineStackObjects.flatten.flatMap {
      case Left(mineStackObj) => List(mineStackObj)
      case Right(group)       => List(group.representative) ++ group.coloredVariants
    } ++ gachaPrizesObjects
  }

  def getAllRepresentativeMineStackObjects: List[MineStackObj] = {
    allMineStackObjects.flatten.flatMap {
      case Right(group) => List(group.representative)
      case Left(_)      => Nil
    }
  }

  def getColoredVariantsMineStackObjectsByRepresentative(
    representative: MineStackObj
  ): List[MineStackObj] = {
    allMineStackObjects.flatten.flatMap {
      case Right(group) =>
        if (group.representative == representative) {
          List(group.representative) ++ group.coloredVariants
        } else {
          Nil
        }
      case Left(_) => Nil
    }
  }

  def getMineStackObjectExceptColoredVariants: List[MineStackObj] = {
    allMineStackObjects.flatten.flatMap {
      case Right(group) =>
        List(group.representative)
      case Left(mineStackObj) =>
        List(mineStackObj)
    } ++ gachaPrizesObjects
  }

  /**
   * 指定した名前のマインスタックオブジェクトを返す
   * @param name internal name
   * @return Some if the associated object was found, otherwise None
   */
  def findByName(name: String): Option[MineStackObj] =
    getAllMineStackObjects.find(_.mineStackObjName == name)
}
