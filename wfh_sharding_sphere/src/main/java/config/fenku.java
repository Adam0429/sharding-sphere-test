package config;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

public class fenku implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> shardingValue) {
        double value = shardingValue.getValue() * 1.0 / 50;
        // 年龄在[0, 50)之间的，入youngerDb库
        if (value < 1) {
            return "young";
            // 年龄在[50, 100)之间的，入olderDb库
        } else if (value < 2) {
            return "old";
            // 年龄在>=100的，入otherDb库
        } else {
            return "other";
        }
    }
}