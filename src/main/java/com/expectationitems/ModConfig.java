package com.expectationitems;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "config/expectationitems.json";
    
    // 剑的设置
    public static int swordDamage = 9999;
    public static boolean swordFlightEnabled = true;
    public static boolean swordInvincibleEnabled = true;
    public static boolean swordClearEnabled = true;
    public static int swordClearRange = 32;
    
    // 工具方块的设置
    public static boolean toolBlockPeaceEnabled = true;
    public static int toolBlockRadius = 32;
    public static boolean toolBlockForceLoad = true;
    public static boolean toolBlockBiogenesis = false; // 默认禁止生物生成
    
    public static void saveConfig() {
        try {
            Path configPath = Paths.get(CONFIG_FILE);
            Files.createDirectories(configPath.getParent());
            
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(new ConfigData(), writer);
            }
            System.out.println("✅ 配置已保存到: " + CONFIG_FILE);
        } catch (Exception e) {
            System.out.println("❌ 保存配置失败: " + e.getMessage());
        }
    }
    
    public static void loadConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    ConfigData data = GSON.fromJson(reader, ConfigData.class);
                    if (data != null) {
                        data.applyToConfig();
                    }
                }
            }
            System.out.println("✅ 配置已加载");
        } catch (Exception e) {
            System.out.println("❌ 加载配置失败: " + e.getMessage());
        }
    }
    
    // 配置数据类
    private static class ConfigData {
        int swordDamage = 9999;
        boolean swordFlightEnabled = true;
        boolean swordInvincibleEnabled = true;
        boolean swordClearEnabled = true;
        int swordClearRange = 32;
        boolean toolBlockPeaceEnabled = true;
        int toolBlockRadius = 32;
        boolean toolBlockForceLoad = true;
        boolean toolBlockBiogenesis = false;
        
        ConfigData() {
            this.swordDamage = ModConfig.swordDamage;
            this.swordFlightEnabled = ModConfig.swordFlightEnabled;
            this.swordInvincibleEnabled = ModConfig.swordInvincibleEnabled;
            this.swordClearEnabled = ModConfig.swordClearEnabled;
            this.swordClearRange = ModConfig.swordClearRange;
            this.toolBlockPeaceEnabled = ModConfig.toolBlockPeaceEnabled;
            this.toolBlockRadius = ModConfig.toolBlockRadius;
            this.toolBlockForceLoad = ModConfig.toolBlockForceLoad;
            this.toolBlockBiogenesis = ModConfig.toolBlockBiogenesis;
        }
        
        void applyToConfig() {
            ModConfig.swordDamage = this.swordDamage;
            ModConfig.swordFlightEnabled = this.swordFlightEnabled;
            ModConfig.swordInvincibleEnabled = this.swordInvincibleEnabled;
            ModConfig.swordClearEnabled = this.swordClearEnabled;
            ModConfig.swordClearRange = this.swordClearRange;
            ModConfig.toolBlockPeaceEnabled = this.toolBlockPeaceEnabled;
            ModConfig.toolBlockRadius = this.toolBlockRadius;
            ModConfig.toolBlockForceLoad = this.toolBlockForceLoad;
            ModConfig.toolBlockBiogenesis = this.toolBlockBiogenesis;
        }
    }
}
