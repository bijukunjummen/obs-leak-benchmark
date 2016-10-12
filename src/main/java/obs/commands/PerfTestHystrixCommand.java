package obs.commands;

import com.netflix.hystrix.HystrixCommand;

public class PerfTestHystrixCommand extends HystrixCommand<String> {

    public PerfTestHystrixCommand(Setter setter) {
        super(setter);
    }

    @Override
    protected String run() throws Exception {
        return "perftest";
    }
}
