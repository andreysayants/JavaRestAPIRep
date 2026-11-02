package rest.api.project;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TwoSum {

public int[] twoSumPerebor(int[] nums, int target) {
    for (int i = 0; i < nums.length; i++) {
        for (int j = i + 1; j < nums.length; j++) {
            System.out.println("Проверяем пару индексов " + i + " и " + j + " затем складываем числа " + nums[i] + 
            " и " + nums[j] + " Получаем число " + (nums[i]+nums[j]));
            if(nums[i] + nums[j] == target) {
                System.out.println("Пара найдена: " + i + " и " + j);
                return new int[] {i, j};
            }            
        }        
    }
    throw new IllegalArgumentException("No two sum solution");
}


    public int[] twoSumHasMap(int[] nums, int target) {
        // Храним значение как ключ, а его индекс как значение
        Map<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            
            // Если нужное число уже есть в мапе, возвращаем индексы
            if(map.containsKey(complement)) {
                System.out.println("Нашли числа, возвращаем индекс числа "+ nums[i-1] +" уже добавленного в мапу: " + map.get(complement) + " и текущий индекс: " + i);
                return new int[] {map.get(complement), i };
            }
            
            // Иначе сохраняем текущее число и его индекс
            map.put(nums[i], i);
            System.out.println("Не нашли числа, поэтому добавляем текущее число " + nums[i] +" и его индекс " + i + " в HashMap и идем дальше");
        }
        
        // По условию решение всегда есть, но компилятор требует return
        throw new IllegalArgumentException("No two sum solution");
    }



@Test 
@DisplayName ("Сумма чисел через перебор")
public void testTwoSumPerebor() {
    int[] inputNumbers = {1, 15, 8, 2, 7};
    int target = 9;

    int[] result = twoSumPerebor(inputNumbers, target);
    assertArrayEquals(new int[] {0, 2}, result);
}

@Test 
@DisplayName ("Сумма чисел через HashMap")
public void testTwoSumHashMap() {
    int[] inputNumbers = {3, 15, 8, 2, 7};
    int target = 9;

    int[] result = twoSumHasMap(inputNumbers, target);
    assertArrayEquals(new int[] {3, 4}, result);
}

}
