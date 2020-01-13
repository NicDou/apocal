package com.jd.apocal.model.sharding;

/**
 * @author DOU
 * @date 2019-10-12 17:16
 */
//public class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {
//
//  @Override
//  public String doSharding(final Collection<String> availableTargetNames,
//      final PreciseShardingValue<Integer> shardingValue) {
//    int size = availableTargetNames.size();
//    for (String each : availableTargetNames) {
//      if (each.endsWith(shardingValue.getValue() % size + "")) {
//        return each;
//      }
//    }
//    throw new UnsupportedOperationException();
//  }
//}