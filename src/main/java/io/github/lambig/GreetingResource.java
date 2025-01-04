package io.github.lambig;


import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.lambig.tuplite._2.Tuple2._2mapWith;
import static io.github.lambig.tuplite._2.Tuple2.tuple2;

@Path("/hello")
public class GreetingResource {

    final SecureRandom random = new SecureRandom();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        return Multi.createFrom().range(1, 1_000_000)
                .onItem().transformToUniAndMerge(i -> asyncUniOf(() -> tuple2(i, random.nextInt(i))))
                .map(_2mapWith((index, rand) -> {
                    if (index % 100 == 0) {
                        throw new RuntimeException("たまに発生する適当なエラー");
                    }
                    return index + rand;
                }))
                .map(n -> n % 10)
                .onFailure().recoverWithItem(0)
                .collect().with(Collectors.summingInt(i -> i))
                .map(String::valueOf);
    }

    public static <E> Uni<E> asyncUniOf(Supplier<E> supplier) {
        return Uni.createFrom().completionStage(CompletableFuture.supplyAsync(supplier));
    }
}
