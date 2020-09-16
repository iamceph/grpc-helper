package cz.iamceph.grpchelper.utils;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import lombok.experimental.UtilityClass;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 13:41
 */
@UtilityClass
public class FutureUtils {
    public static <T> CompletableFuture<T> toCompletableFuture(ListenableFuture<T> listenableFuture) {
        final var completableFuture = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean cancelled = listenableFuture.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);

                return cancelled;
            }
        };

        Futures.addCallback(listenableFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(@Nullable T result) {
                completableFuture.complete(result);
            }

            @Override
            public void onFailure(@NotNull Throwable t) {
                completableFuture.completeExceptionally(t);
            }
        }, completableFuture.defaultExecutor());
        return completableFuture;
    }
}
