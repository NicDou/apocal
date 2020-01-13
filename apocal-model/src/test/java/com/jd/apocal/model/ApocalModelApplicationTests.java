package com.jd.apocal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ApocalModelApplicationTests {

  //@Test
  public void contextLoads() {

  }

  public static void main(String[] args) {

    //List<Integer> duplicates = findDuplicates(new int[]{2, 4, 5, 7, 3, 2, 6, 4});
    String cbacdcbc = removeDuplicateLetters("cbacdcbc");
    System.out.println(cbacdcbc);

  }

  /**
   * 给定一个仅包含小写字母的字符串，去除字符串中重复的字母，使得每个字母只出现一次。需保证返回结果的字典序最小（要求不能打乱其他字符的相对位置）。
   * <p>
   * 示例 1:
   * <p>
   * 输入: "bcabc" 输出: "abc" 示例 2:
   * <p>
   * 输入: "cbacdcbc" 输出: "acdb"
   * <p>
   *
   * @param s
   * @return
   */
  public static String removeDuplicateLetters(String s) {
    int[] charsCount = new int[26];//计算26字母数量
    boolean[] visited = new boolean[26];//标记字母是否已经入栈
    int len = s.length();
    char[] sChars = s.toCharArray();
    for (char c : sChars) {
      charsCount[c - 'a']++;
    }
    Stack<Character> stack = new Stack<>();
    int index = 0;//最终字符的长度
    for (int count : charsCount) {
      if (count > 0) {
        index++;
      }
    }
    char[] res = new char[index];
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      //有小字符的且满足其前面的字符在小字符后还有同样字符的，则出栈
      while (!stack.isEmpty() && c < stack.peek() && charsCount[stack.peek() - 'a'] > 1 && !visited[
          c - 'a']) {
        Character pop = stack.pop();
        visited[pop - 'a'] = false;
        charsCount[pop - 'a']--;
      }
      if (visited[c - 'a']) {
        charsCount[c - 'a']--;//重复的字符根据游标往后移动，数量减一
        continue;
      }
      stack.push(c);
      visited[c - 'a'] = true;
    }

    while (!stack.isEmpty()) {
      res[--index] = stack.pop();
    }
    return String.valueOf(res);
  }

  /**
   * 给定一个整数数组 a，其中1 ≤ a[i] ≤ n （n为数组长度）, 其中有些元素出现两次而其他元素出现一次。 找到所有出现两次的元素。
   * 你可以不用到任何额外空间并在O(n)时间复杂度内解决这个问题吗？
   */
  public static List<Integer> findDuplicates(int[] nums) {
    List<Integer> rs = new ArrayList<>();
    for (int i = 0; i < nums.length; i++) {
      if (nums[Math.abs(nums[i]) - 1] < 0) {
        rs.add(Math.abs(nums[i]));
      } else {
        nums[Math.abs(nums[i]) - 1] *= -1;
      }
    }

    return rs;
  }

  /**
   * K 大值
   */

  static class KthLarge {

    Queue<Integer> queue;


    public KthLarge(int k, Integer[] elements) {

      this.queue = new PriorityQueue<>(k);

      for (Integer ele : elements) {

        if (this.queue.size() < k) {

          this.queue.offer(ele);

        } else if (this.queue.peek() < ele) {
          //queue中以有k个元素时需要比较
          this.queue.poll();
          this.queue.offer(ele);

        }

      }


    }

    public Integer get() {

      return queue.peek();

    }


  }

}


