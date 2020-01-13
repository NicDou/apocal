package com.jd.apocal.model.sharding;

/**
 * @author DOU
 * @date 2019-10-12 18:18
 */
//public final class TableRangeShardingAlgorithm implements RangeShardingAlgorithm<Long> {
//
//  @Override
//  public Collection<String> doSharding(Collection<String> collection,
//      RangeShardingValue<Long> rangeShardingValue) {
//    int size = collection.size();
//    Collection<String> collect = new ArrayList<>();
//    Range<Long> valueRange = rangeShardingValue.getValueRange();
//    for (Long i = valueRange.lowerEndpoint(); i <= valueRange.upperEndpoint(); i++) {
//      for (String each : collection) {
//        if (each.endsWith(i % size + "")) {
//          collect.add(each);
//        }
//      }
//    }
//    return collect;
//  }
//}