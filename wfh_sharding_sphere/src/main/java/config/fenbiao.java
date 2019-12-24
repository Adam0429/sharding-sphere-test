package config;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

public class fenbiao implements PreciseShardingAlgorithm<String> {

    public String doSharding(Collection<String> availableTargetNames,
                             PreciseShardingValue<String> shardingValue) {
        // 根据本人的真实数据节点信息， 如果可用表里面包含staff，说明一定用的是otherDb数据源，
        // 而该数据库里面只有一张表staff， 这里直接返回即可
//        for (String each : availableTargetNames) {
//            //判断值是否在设计表中的哪一个
//              System.out.println(availableTargetNames);
//        }

        if (availableTargetNames.contains("staff")) {
            return "staff";
        }
        // 如果是youngerDb数据源 或 olderDb数据源 的话，会走到下面的逻辑
        String tmpGender = shardingValue.getValue();
        if ("男".equals(tmpGender)) {
            return "staff_man";
        } else if ("女".equals(tmpGender)) {
            return "staff_woman";
        }
        throw new UnsupportedOperationException();
    }
}