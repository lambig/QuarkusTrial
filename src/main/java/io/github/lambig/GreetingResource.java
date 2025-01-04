package io.github.lambig;


import io.github.lambig.textescape.TextEscape;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.lambig.tuplite._2.Tuple2.tuple2;

@Path("/hello")
public class GreetingResource {

    final Random random = ThreadLocalRandom.current();
    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {

        return Multi.createFrom().range(1, 1_000_000)
                .onItem().transformToUniAndMerge(i ->
                        asyncUniOf(() -> tuple2(i, random.nextInt(10))).onFailure().recoverWithItem(tuple2(i, 0))
                )
                .onItem().transformToUniAndMerge(t -> Uni.createFrom().item(() -> t.map((index, rand) -> {
                    if (index % 10 == 0) {
                        throw new RuntimeException("たまに発生する適当なエラー");
                    }
                    return rand;
                })).onFailure().recoverWithItem(0))  // 要素ごとにデフォルトしないとMultiが止まる
                .collect().with(Collectors.summingInt(i -> i))
                .map(sum ->
                {
                    Runtime runtime = Runtime.getRuntime();
                    return TextEscape.escape("""
                                    計算結果: ${sum}
                                    メモリ使用量 (バイト): ${usage}
                                    現在のヒープサイズ (バイト): ${currentHeap}
                                    最大ヒープサイズ (バイト): ${maxHeap}
                                    """)
                            .where(Map.of(
                                    "sum", sum,
                                    "usage", runtime.totalMemory() - runtime.freeMemory(),
                                    "currentHeap", runtime.totalMemory(),
                                    "maxHeap", runtime.maxMemory()
                            ))
                            .compile();
                });
    }

    public static <E> Uni<E> asyncUniOf(Supplier<E> supplier) {
        return Uni.createFrom().completionStage(CompletableFuture.supplyAsync(supplier, executor));
    }
}
