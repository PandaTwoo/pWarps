package br.com.pandatwo.pwarps.concurrent;

import br.com.pandatwo.pwarps.pWarps;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.*;
import java.util.function.Supplier;

/*
    Essa classe foi pega de um dos códigos do Sasuke :)
 */

public class AsyncTask {

    private static Plugin PLUGIN = pWarps.getInstance();

    private static Executor executor = new ThreadPoolExecutor(
            0, 2, 2000, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );

    public static Executor getExecutor() {
        return executor;
    }

    public static CompletableFuture<Void> completeFuture(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    public static void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable, executor);
    }

    public static BukkitTask asyncContext(Runnable runnable) {
        return Bukkit.getScheduler()
                .runTaskAsynchronously(PLUGIN, runnable);
    }

    public static BukkitTask syncContext(Runnable runnable) {
        return Bukkit.getScheduler()
                .runTask(PLUGIN, runnable);
    }

    public static <V> Future<V> syncMethod(Callable<V> callable){
        return Bukkit.getScheduler().callSyncMethod(PLUGIN , callable);
    }

    public static BukkitTask repeatingTask(Runnable runnable, boolean async, long delay) {
        if (async) {
            return Bukkit.getScheduler()
                    .runTaskTimerAsynchronously(PLUGIN, runnable, 0, delay);
        } else {
            return Bukkit.getScheduler()
                    .runTaskTimer(PLUGIN, runnable, 0, delay);
        }
    }


}