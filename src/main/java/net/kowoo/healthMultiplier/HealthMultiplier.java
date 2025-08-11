package net.kowoo.healthMultiplier;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ThreadLocalRandom;

public final class HealthMultiplier extends JavaPlugin implements Listener,CommandExecutor {
    private int val1 = 1;  // 기본값
    private int val2 = 1; // 기본값
    @Override
    public void onEnable() {
        // 플러그인이 활성화됐을때
        getLogger().info("플러그인이 활성화됐습니다!"); //활성화 메시지
        getServer().getPluginManager().registerEvents(this, this); //이벹트 등록
        getServer().getPluginCommand("health").setExecutor(this); //health 커맨드 등록
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length != 2) {
            commandSender.sendMessage(ChatColor.RED + "사용법 : /health <수1> <수2>"); //수 2개가 완성 안됐을때 메시지
            return true;
        }
        String num1 = strings[0];
        String num2 = strings[1];
        int inputVal1 = Integer.parseInt(num1);
        int inputVal2 = Integer.parseInt(num2);

        if (!num1.matches("^[1-9]\\d*$")
                || !num2.matches("^[1-9]\\d*$")) {
            commandSender.sendMessage(ChatColor.RED + "두 값은 모두 0보다 큰 숫자여야 해요!");
            return true;
        }
        if (inputVal1 > inputVal2) {
            commandSender.sendMessage(ChatColor.RED + "첫번째 값이 두번째 값보다 클 수 없어요!");
            return true;
        }
        if (inputVal1 == inputVal2) {
            commandSender.sendMessage(ChatColor.RED + "두 값이 같을 수 없어요!");
            return true;
        }

        commandSender.sendMessage(ChatColor.GREEN + "체력 랜덤 배율을 " + inputVal1 + "과 " + inputVal2 + " 사이로 설정했습니다.");
        val1 = inputVal1;
        val2 = inputVal2;
        return true;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("플러그인이 비활성화됐습니다!");
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
