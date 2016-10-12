package obs;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import obs.commands.PerfTestHystrixCommand;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import rx.Observable;

import java.util.concurrent.TimeUnit;

public class ObservableZipBenchmark {

    @Benchmark
    @Fork(0)
    @Measurement(iterations = 20, time = 3, timeUnit = TimeUnit.SECONDS)
    public String testZip() {
        try {
            PerfTestHystrixCommand command1 = new PerfTestHystrixCommand(HystrixCommand.Setter.withGroupKey(
                    HystrixCommandGroupKey.Factory.asKey("part1"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("part1"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withCircuitBreakerForceOpen(true)));

            PerfTestHystrixCommand command2 = new PerfTestHystrixCommand(HystrixCommand.Setter.withGroupKey(
                    HystrixCommandGroupKey.Factory.asKey("part2"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("part2")));

            Observable<String> part1Obs = command1.toObservable();
            Observable<String> part2Obs = command2.toObservable();

            return Observable.zip(part1Obs, part2Obs, (part1, part2) -> part1 + part2)
                    .toBlocking()
                    .single();
        } catch (Exception e) {
            return null;
        }
    }

}
