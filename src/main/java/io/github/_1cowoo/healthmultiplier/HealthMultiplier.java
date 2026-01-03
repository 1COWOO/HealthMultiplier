package io.github._1cowoo.healthmultiplier;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.Nullable;

public final class HealthMultiplier extends JavaPlugin implements Listener,CommandExecutor, TabExecutor {

    private int val1 = 1;  // 기본값
    private int val2 = 1; // 기본값
    @Override
    public void onEnable() {
        // 플러그인이 활성화됐을때
        getLogger().info("플러그인이 활성화됐습니다!"); // 플러그인 활성화 메시지
        getServer().getPluginManager().registerEvents(this, this); // 이벤트 등록
        getServer().getPluginCommand("health").setExecutor(this); // /health 커맨드 등록
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // 인자가 없는 경우
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "사용법: /health <수1> <수2> 또는 /health reset");
            return true;
        }

        // reset 명령어
        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            val1 = 1;
            val2 = 1;
            sender.sendMessage(ChatColor.GREEN + "체력 배율이 기본값으로 초기화됐어요!");

            return true;
        }

        // 숫자 2개가 아닌 경우
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "값 2개를 입력하거나 /health reset을 사용하세요!");
            return true;
        }

        String num1 = args[0];
        String num2 = args[1];

        // 숫자 체크
        if (!num1.matches("^[1-9]\\d*$") || !num2.matches("^[1-9]\\d*$")) {
            sender.sendMessage(ChatColor.RED + "두 값은 모두 0보다 큰 숫자여야 해요!");
            return true;
        }

        int inputVal1 = Integer.parseInt(num1);
        int inputVal2 = Integer.parseInt(num2);

        if (inputVal1 > inputVal2) {
            sender.sendMessage(ChatColor.RED + "첫번째 값이 두번째 값보다 클 수 없어요!");
            return true;
        }
        if (inputVal1 == inputVal2) {
            sender.sendMessage(ChatColor.RED + "두 값이 같을 수 없어요!");
            return true;
        }

        val1 = inputVal1;
        val2 = inputVal2;
        sender.sendMessage(ChatColor.GREEN + "체력 랜덤 배율을 " + val1 + "과 " + val2 + " 사이로 설정했습니다.");
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reset");
        }
        return List.of();
    }
    @Override
    public void onDisable() { 
        getLogger().info("플러그인이 비활성화됐습니다!"); // 플러그인 비활성화 메시지
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            int randomValue = ThreadLocalRandom.current().nextInt(val1, val2 + 1);
            livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(livingEntity.getAttribute(Attribute.MAX_HEALTH).getBaseValue() * randomValue);
            livingEntity.setHealth(livingEntity.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
        }
    }
    }
