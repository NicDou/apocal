package com.jd.apocal.model.sharding;

/**
 * @author DOU
 * @date 2019-10-12 18:16
 */
//public final class TablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
//
//  @Override
//  public String doSharding(final Collection<String> availableTargetNames,
//      final PreciseShardingValue<Long> shardingValue) {
//    int size = availableTargetNames.size();
//    for (String each : availableTargetNames) {
//      if (each.endsWith(shardingValue.getValue() % size + "")) {
//        return each;
//      }
//    }
//    throw new UnsupportedOperationException();
//  }
//}
